/**
 * Copyright 2021 Jakub Świderek, Bartosz Palewicz
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 * THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package pl.first.firstjava.model;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import pl.first.firstjava.model.exceptions.DatabaseDeleteException;
import pl.first.firstjava.model.exceptions.DatabaseReadException;
import pl.first.firstjava.model.exceptions.DatabaseWriteException;
import pl.first.firstjava.model.exceptions.VariablesLoadException;

public class JdbcSudokuBoardDao implements Dao<SudokuBoard> {

    String url;
    private final Properties props = new Properties();
    private final String boardName;
    Dotenv dotenv;
    private static final Logger logger =
            (Logger) LogManager.getLogger(JdbcSudokuBoardDao.class.getName());

    JdbcSudokuBoardDao(String name) {
        super();
        try {
            loadEnvVariables();
        } catch (VariablesLoadException e) {
            logger.debug(e.getLocalizedMessage());
        }
        url = dotenv.get("DB_URL");
        props.setProperty("user", dotenv.get("DB_USERNAME"));
        props.setProperty("password", dotenv.get("DB_PASSWORD"));
        props.setProperty("ssl", "false");
        boardName = name;
    }

    private void loadEnvVariables() throws VariablesLoadException {
        dotenv = Dotenv.configure().directory("../").ignoreIfMissing().load();
        try {
            dotenv = Dotenv.load();
        } catch (DotenvException e) {
            throw new VariablesLoadException("Message", e);
        }
    }


    @Override
    public void close() {
    }


    @Override
    public SudokuBoard read() throws DatabaseReadException {
        SudokuBoard board = new SudokuBoard(new BacktrackingSudokuSolver());
        String q = "SELECT fullcopy FROM Boards WHERE BoardName = ?;"
                + "SELECT * FROM BoardValues WHERE BoardID = "
                + "(SELECT BoardID FROM Boards WHERE BoardName = ?);";
        try (Connection db = DriverManager.getConnection(url, props);
             PreparedStatement pst = db.prepareStatement(q);
        ) {
            pst.setString(1, boardName);
            pst.setString(2, boardName);
            pst.execute();

            ResultSet rs = pst.getResultSet();
            if (!rs.next()) {
                throw new DatabaseReadException("Database exception");
            }
            boolean f = rs.getBoolean(1);
            if (f) {
                board = new BoardToSave(board);
            }
            rs.close();

            pst.getMoreResults();

            ResultSet rs2 = pst.getResultSet();
            while (rs2.next()) {
                board.setFieldValue(rs2.getInt(2), rs2.getInt(3), rs2.getInt(4));
                if (f) {
                    if (rs2.getBoolean(5)) {
                        ((BoardToSave) board).getClean().setFieldValue(rs2.getInt(2),
                                rs2.getInt(3), 0);
                    } else {
                        ((BoardToSave) board).getClean().setFieldValue(rs2.getInt(2),
                                rs2.getInt(3), 1);
                    }
                }
            }
            rs2.close();
        } catch (Exception e) {
            throw new DatabaseReadException("Database exception", e);
        }
        return board;
    }

    @Override
    public void write(SudokuBoard board) throws DatabaseWriteException {
        boolean isFull = false;
        int boardID;
        try {
            if (board.getClass() == BoardToSave.class) {
                isFull = true;
            }
        } catch (NullPointerException e) {
            throw new DatabaseWriteException("Board is null", e);

        }

        String query;
        try (Connection db = DriverManager.getConnection(url, props);
             PreparedStatement pst =
                     db.prepareStatement("SELECT BoardID FROM Boards WHERE BoardName = ?;");
        ) {
            pst.setString(1, boardName);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                boardID = rs.getInt(1);
                try (PreparedStatement pst2 =
                             db.prepareStatement("DELETE FROM BoardValues WHERE BoardID = ?;");
                ) {
                    pst.setString(1, boardName);
                    pst.executeQuery();
                }
                rs.close();

            } else {

                query = "INSERT INTO Boards (BoardName, FullCopy) VALUES (?, ?) RETURNING BoardID";
                try (PreparedStatement pst2 = db.prepareStatement(query);) {
                    pst2.setString(1, boardName);
                    pst2.setBoolean(2, isFull);
                    ResultSet rs2 = pst2.executeQuery();
                    rs2.next();
                    boardID = rs2.getInt(1);
                    rs2.close();
                }
            }
            try (PreparedStatement pst3 =
                         db.prepareStatement("INSERT INTO BoardValues "
                                 + "(BoardID, X, Y, Value, Editable) VALUES (?, ? ,?, ?, ?)");
            ) {
                for (int x = 0; x < 9; x++) {
                    for (int y = 0; y < 9; y++) {
                        pst3.setInt(1, boardID);
                        pst3.setInt(2, x);
                        pst3.setInt(3, y);
                        pst3.setInt(4, board.getFieldValue(x, y));
                        if (isFull) {
                            pst3.setBoolean(5,
                                    ((BoardToSave) board).getClean().getFieldValue(x, y) == 0);
                        } else {
                            pst3.setBoolean(5, true);
                        }
                        pst3.addBatch();

                    }
                }
                pst3.clearParameters();
                pst3.executeBatch();
            }

        } catch (SQLException e) {
            throw new DatabaseWriteException("Database exception", e);
        }


    }

    public boolean delete() throws DatabaseDeleteException {
        String q = "SELECT BoardID FROM Boards WHERE BoardName = ?;";
        try (Connection db = DriverManager.getConnection(url, props);
             PreparedStatement pst = db.prepareStatement(q);

        ) {
            pst.setString(1, boardName);

            ResultSet rs = pst.executeQuery();
            int boardID = 0;
            if (rs.next()) {
                boardID = rs.getInt(1);
            } else {
                throw new DatabaseDeleteException("Nothing to delete");
            }

            rs.close();

            String q2 = "DELETE FROM BoardValues WHERE BoardID = ?;"
                    + "DELETE FROM Boards WHERE BoardID = ?;";

            try (PreparedStatement pst2 = db.prepareStatement(q2);
            ) {

                pst2.setInt(1, boardID);
                pst2.setInt(2, boardID);
                pst2.execute();
            }
        } catch (SQLException e) {
            throw new DatabaseDeleteException("Database exception", e);
        }
        return true;
    }
}
