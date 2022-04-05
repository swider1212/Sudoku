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

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SudokuColumnTest {

    public SudokuColumnTest(){}

    @Test
    public void testConstructor() {
        SudokuField[] fields = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            fields[i] = new SudokuField();
            fields[i].setFieldValue(i);
        }
        SudokuColumn column = new SudokuColumn(fields);
    }

    @Test
    public void testVerify() {
        SudokuField[] fields = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            fields[i] = new SudokuField();
            fields[i].setFieldValue(i);

        }
        SudokuColumn column1 = new SudokuColumn(fields);
        assertEquals(column1.verify(), true);

        fields[2].setFieldValue(1);

        SudokuColumn column2 = new SudokuColumn(fields);
        assertEquals(column2.verify(), false);

        fields[1].setFieldValue(0);
        fields[2].setFieldValue(0);
        fields[3].setFieldValue(0);

        SudokuColumn column3 = new SudokuColumn(fields);
        assertEquals(column3.verify(), true);

    }
    @Test
    public void cloneTest() {
        SudokuField[] fields = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            fields[i] = new SudokuField();
            fields[i].setFieldValue(i);
        }
        SudokuColumn col = new SudokuColumn(fields);
        SudokuColumn col2 = (SudokuColumn) col.clone();
        assertEquals(col, col2);

    }
}
