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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardToSaveTest {
    @Test
    public void copyTest(){

        SudokuBoard b = new SudokuBoard(new BacktrackingSudokuSolver());

        b.solveGame();
        b.setFieldValue(0, 0, 0);

        BoardToSave b2 = new BoardToSave(b);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                assertEquals(b.getFieldValue(i, j), b2.getFieldValue(i, j));
                assertEquals(b2.getClean().getFieldValue(i, j), b2.getFieldValue(i, j));
            }
        }

        b2.setFieldValue(0, 0, 1);

        assertNotEquals(b.getFieldValue(0, 0), b2.getFieldValue(0, 0));
        assertNotEquals(b2.getClean().getFieldValue(0, 0), b2.getFieldValue(0, 0));

    }
}
