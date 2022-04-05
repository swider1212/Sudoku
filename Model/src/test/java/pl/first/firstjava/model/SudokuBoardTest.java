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

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class SudokuBoardTest {
    public SudokuBoardTest() {
    }

    @RepeatedTest(10)
    public void testIsArraysEqual() /*throws InterruptedException*/ {

        SudokuSolver interface1 = new BacktrackingSudokuSolver();

        SudokuBoard board = new SudokuBoard(interface1);
        SudokuBoard board1 = new SudokuBoard(interface1);

        board.solveGame();
        board1.solveGame();


        int[] Array = new int[81];
        int[] Array1 = new int[81];
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                Array[y * 9 + x] = board.getFieldValue(x, y);
                Array1[y * 9 + x] = board1.getFieldValue(x, y);
            }

        }
        assertFalse(Arrays.equals(Array, Array1));
    }

    @Test
    public void TestBoardCorrectness() {

        SudokuSolver interface1 = new BacktrackingSudokuSolver();

        SudokuBoard board = new SudokuBoard(interface1);

        board.solveGame();

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                //test rows
                for (int t = 0; t < 9; t++) {
                    if (x != t) {
                        assertNotEquals(board.getFieldValue(x, y), board.getFieldValue(t, y));
                    }
                }
                //test columns
                for (int t = 0; t < 9; t++) {
                    if (y != t) {
                        assertNotEquals(board.getFieldValue(x, y), board.getFieldValue(x, t));
                    }
                }
                //test 3x3 boxes
                int r = x - x % 3;
                int c = y - y % 3;
                for (int i = r; i < r + 3; i++) {
                    for (int j = c; j < c + 3; j++) {
                        if (x != i || y != j) {
                            assertNotEquals(board.getFieldValue(i, j), board.getFieldValue(x, y));
                        }
                    }
                }
            }
        }
    }

    @Test
    public void GetingRowTest() {
        SudokuSolver interface1 = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(interface1);
        board.solveGame();
        SudokuRow row = board.getRow(0);
        for (int i = 0; i < 9; i++) {
            assertEquals(row.getField(i).getFieldValue(), board.getFieldValue(i, 0));
        }
        board.setFieldValue(0, 0, (board.getFieldValue(0,0)+1)%10);

        assertNotEquals(row.getField(0).getFieldValue(), board.getFieldValue(0, 0));

    }


    @Test
    public void GetingColumnTest() {
        SudokuSolver interface1 = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(interface1);
        board.solveGame();
        SudokuColumn column = board.getCollumn(0);

        for (int i = 0; i < 9; i++) {
            assertEquals(column.getField(i).getFieldValue(), board.getFieldValue(0, i));
        }
        board.setFieldValue(0, 0, (board.getFieldValue(0,0)+1)%10);

        assertNotEquals(column.getField(0).getFieldValue(), board.getFieldValue(0, 0));

    }


    @Test
    public void GetingBoxTest() {
        SudokuSolver interface1 = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(interface1);
        board.solveGame();
        SudokuBox box = board.getBox(0, 0);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(box.getField((3*i)+j).getFieldValue(), board.getFieldValue(j, i));
            }
        }
        board.setFieldValue(0, 0, (board.getFieldValue(0,0)+1)%10);

        assertNotEquals(box.getField(0).getFieldValue(), board.getFieldValue(0, 0));



    }

    @Test
    public void isOkTest() {
        SudokuSolver interface1 = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(interface1);
        board.solveGame();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board.checkBoard(i, j, 0);
            }
        }
        assertFalse(board.checkBoard(0, 0, 100));
    }

    @Test
    public void equalTest(){
        SudokuSolver interface1 = new BacktrackingSudokuSolver();

        SudokuBoard board = new SudokuBoard(interface1);
        SudokuBoard board2 = new SudokuBoard(interface1);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board.setFieldValue(i, j, 1);
                board2.setFieldValue(i, j, 1);
            }
        }
        assertFalse(board.equals(null));
        assertFalse(board.equals(":("));
        assertTrue(board.equals(board));
        assertTrue(board.equals(board2));
        board2.setFieldValue(0, 0, 2);
        assertFalse(board.equals(board2));
    }

    @Test
    public void toStringTest(){
        SudokuSolver interface1 = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(interface1);

        assertTrue(board.toString() instanceof java.lang.String);

    }

    @Test
    public void hashTest(){
        SudokuSolver interface1 = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(interface1);
        assertNotNull(board.hashCode());
    }


    @Test
    public void integrationTest(){
        SudokuSolver interface1 = new BacktrackingSudokuSolver();

        SudokuBoard board = new SudokuBoard(interface1);
        SudokuBoard board2 = new SudokuBoard(interface1);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board.setFieldValue(i, j, 1);
                board2.setFieldValue(i, j, 1);
            }
        }

        assertEquals(board.equals(board2), board.hashCode() == board2.hashCode());

        board2.setFieldValue(0, 0, 2);

        assertFalse(board.equals(board2));
    }


    @Test
    public void cloneTest(){
        SudokuSolver interface1 = new BacktrackingSudokuSolver();

        SudokuBoard b1 = new SudokuBoard(interface1);
        b1.solveGame();
        SudokuBoard b2 = b1.clone();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertEquals(b1.getFieldValue(i, j), b2.getFieldValue(i, j));
            }
        }
        b1.setFieldValue(0, 0, (b1.getFieldValue(0, 0)+1)%10);

        assertNotEquals(b1.getFieldValue(0, 0), b2.getFieldValue(0, 0));
    }
}
