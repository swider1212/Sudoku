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

package pl.first.firstjava.view;

import java.io.IOException;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import pl.first.firstjava.model.BacktrackingSudokuSolver;
import pl.first.firstjava.model.BoardToSave;
import pl.first.firstjava.model.JdbcSudokuBoardDao;
import pl.first.firstjava.model.SudokuBoard;
import pl.first.firstjava.model.SudokuBoardDaoFactory;
import pl.first.firstjava.model.exceptions.DatabaseReadException;
import pl.first.firstjava.model.exceptions.DatabaseWriteException;
import pl.first.firstjava.view.exceptions.FormException;

public class Controller {

    public TextField saveName;
    private Poziom poziom;
    private SudokuBoard sudokuBoard;
    private static Logger logger = (Logger) LogManager.getLogger(App.class.getName());

    @FXML
    GridPane board;

    @FXML
    private void switchToPrimary() throws FormException {
        ResourceBundle resources = ResourceBundle
                .getBundle("pl.first.firstjava.view.bundles.bundle");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"), resources);
        try {
            App.changeRoot(loader.load());

        } catch (IOException e) {
            throw new FormException("FormException", e);
        }

    }

    @FXML
    private void start() {

        sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());

        sudokuBoard.solveGame();

        starter();
        this.poziom.start(board);
        updateBoard();

        sudokuBoard = new BoardToSave(sudokuBoard);

        logger.debug("Game started");
    }

    private void starter() {
        String[] allowed = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

        ObservableList<Node> childrens = board.getChildren();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField t = (TextField) childrens.get((i * 9) + j);
                t.setText(Integer.toString(sudokuBoard.getFieldValue(i, j)));

                if (sudokuBoard.getFieldValue(i, j) == 0) {
                    Platform.runLater(t::clear);
                }
                t.setEditable(false);
                int finalI = i;
                int finalJ = j;
                t.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (Arrays.asList(allowed).contains(newValue)) {
                        sudokuBoard.setFieldValue(finalI, finalJ, Integer.parseInt(newValue));
                        t.setText(Integer.parseInt(newValue) + "");

                    } else {
                        Platform.runLater(t::clear);
                        sudokuBoard.setFieldValue(finalI, finalJ, 0);
                    }
                    //check();
                });
            }
        }

    }

    private void updateBoard() {
        ObservableList<Node> childrens = board.getChildren();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField t = (TextField) childrens.get((i * 9) + j);
                changeColor(i, j, new int[]{255, 255, 255});
                if (t.getText() == "") {
                    sudokuBoard.setFieldValue(i, j, 0);
                }
            }
        }
    }


    public void setPoziom(char level) {
        if (level == 'H') {
            poziom = Poziom.TRUDNY;
        } else if (level == 'M') {
            poziom = Poziom.SREDNI;
        } else if (level == 'E') {
            poziom = Poziom.LATWY;
        }
    }

    @FXML
    public void save() {

        String name = saveName.getText();
        if (sudokuBoard != null && name != "") {
            try (JdbcSudokuBoardDao sFile = (JdbcSudokuBoardDao) SudokuBoardDaoFactory
                    .getDatabaseDao(name)) {
                sFile.write(this.sudokuBoard);
                logger.debug("Gamestate saved to: " + name);
            } catch (DatabaseWriteException e) {
                logger.error(e.getLocalizedMessage());
            }
        }
    }

    @FXML
    public void load() {
        SudokuBoard clean;
        String name = saveName.getText();
        try (JdbcSudokuBoardDao sFile = (JdbcSudokuBoardDao) SudokuBoardDaoFactory
                .getDatabaseDao(name)) {
            BoardToSave b = (BoardToSave) sFile.read();
            if (b == null) {
                logger.debug("No such save name");
                return;
            }
            this.sudokuBoard = b;
            clean = b.getClean();
            logger.debug("Gamestate load from: " + name);
        } catch (DatabaseReadException e) {
            logger.error(e.getLocalizedMessage());
            return;
        }

        starter();

        restore(clean);
    }

    public void restore(SudokuBoard cleanBoard) {
        ObservableList<Node> childrens = board.getChildren();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField t = (TextField) childrens.get((i * 9) + j);
                changeColor(i, j, new int[]{255, 255, 255});
                if (cleanBoard.getFieldValue(i, j) == 0) {
                    t.setEditable(true);
                    t.setStyle("-fx-background-color: #FFFFFF;");
                }
            }
        }
    }

    @FXML
    public void check() {
        if (sudokuBoard != null) {
            logger.debug("Checking");
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    changeColor(i, j, new int[]{255, 255, 255});

                }
            }
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (!sudokuBoard.getCollumn(i).verify()) {
                        for (int k = 0; k < 9; k++) {
                            changeColor(i, k, new int[]{255, 220, 220});
                        }
                    }
                    if (!sudokuBoard.getRow(j).verify()) {
                        for (int k = 0; k < 9; k++) {
                            changeColor(k, j, new int[]{255, 220, 220});
                        }
                    }
                    if (!sudokuBoard.getBox(i / 3, j / 3).verify()) {
                        for (int k = 0; k < 3; k++) {
                            for (int l = 0; l < 3; l++) {
                                changeColor((k + (3 * (i / 3))), (l + (3 * (j / 3))),
                                        new int[]{255, 220, 220});
                            }
                        }
                    }
                }
            }
        }
    }


    private void changeColor(int x, int y, int[] c) {
        ObservableList<Node> childrens = board.getChildren();
        TextField t = (TextField) childrens.get((x * 9) + y);
        if (!t.isEditable()) {
            for (int i = 0; i < 3; i++) {
                c[i] -= 20;
            }
        }
        t.setStyle("-fx-background-color: rgb(" + c[0] + ", " + c[1] + ", " + c[2] + ");");

    }

}