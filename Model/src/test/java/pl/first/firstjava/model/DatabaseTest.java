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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.first.firstjava.model.exceptions.DatabaseDeleteException;
import pl.first.firstjava.model.exceptions.DatabaseReadException;
import pl.first.firstjava.model.exceptions.DatabaseWriteException;


import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTest {
    private static SudokuBoard b1;
    private static SudokuBoard b2;

    @BeforeAll
    public static void setup() {
        Locale.setDefault(new Locale("pl"));
        b1 = new SudokuBoard(new BacktrackingSudokuSolver());
        b1.solveGame();

        b2 = new SudokuBoard(new BacktrackingSudokuSolver());
        b2.solveGame();
        for (int i = 0; i < 3; i += 2) {
            for (int j = 0; j < 4; j++) {
                b1.setFieldValue(i, j, 0);
            }
        }
        b2 = new BoardToSave(b2);
    }

    @Test
    public void writeNormalBoardTest() {
        try (JdbcSudokuBoardDao sFile = (JdbcSudokuBoardDao) SudokuBoardDaoFactory
                .getDatabaseDao("test1")) {
            sFile.write(b1);
        } catch (DatabaseWriteException ignored) {
        }
    }

    @Test
    public void writeDecoratedBoardTest() {
        try (JdbcSudokuBoardDao sFile = (JdbcSudokuBoardDao) SudokuBoardDaoFactory
                .getDatabaseDao("test2")) {
            sFile.write(b2);
        } catch (DatabaseWriteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readNormalBoardTest() {
        SudokuBoard b3 = null;
        try (JdbcSudokuBoardDao sFile = (JdbcSudokuBoardDao) SudokuBoardDaoFactory
                .getDatabaseDao("test1")) {
            b3 = sFile.read();
        } catch (DatabaseReadException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assert b3 != null;
                assertEquals(b1.getFieldValue(i, j), b3.getFieldValue(i, j));
            }
        }
    }

    @Test
    public void readDecoratedBoardTest() {
        SudokuBoard b4 = null;
        try (JdbcSudokuBoardDao sFile = (JdbcSudokuBoardDao) SudokuBoardDaoFactory
                .getDatabaseDao("test2")) {
            b4 = sFile.read();

        } catch (DatabaseReadException e) {
            e.printStackTrace();
        }
        assert b4 != null;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertEquals(b2.getFieldValue(i, j), b4.getFieldValue(i, j));
                if (((BoardToSave) b2).getClean().getFieldValue(i, j) == 0) {
                    assertEquals(((BoardToSave) b4).getClean().getFieldValue(i, j), 0);
                } else {
                    assertEquals(((BoardToSave) b4).getClean().getFieldValue(i, j), 1);
                }
            }
        }
    }

    @Test
    public void deleteTest() {
        try (JdbcSudokuBoardDao sFile = (JdbcSudokuBoardDao) SudokuBoardDaoFactory
                .getDatabaseDao("test1")) {
            assertTrue(sFile.delete());
        } catch (DatabaseDeleteException ignored) {
        }
        try (JdbcSudokuBoardDao sFile = (JdbcSudokuBoardDao) SudokuBoardDaoFactory
                .getDatabaseDao("test2")) {
            assertTrue(sFile.delete());
        } catch (DatabaseDeleteException ignored) {
        }
    }

}
