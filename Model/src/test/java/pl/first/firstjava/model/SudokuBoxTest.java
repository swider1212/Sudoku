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

public class SudokuBoxTest {
    public SudokuBoxTest(){}

    @Test
    public void testConstructor() {
        SudokuField[] fields = new SudokuField[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j <3; j++) {
                fields[i] = new SudokuField();
            }
        }
        SudokuBox box = new SudokuBox(fields);
    }

    @Test
    public void testVerify() {
        SudokuField[] fields = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            fields[i] = new SudokuField();
            fields[i].setFieldValue(i);

        }
        SudokuBox row = new SudokuBox(fields);
        assertTrue(row.verify());

        fields[2].setFieldValue(1);

        SudokuBox row2 = new SudokuBox(fields);
        assertFalse(row2.verify());

        fields[1].setFieldValue(0);
        fields[2].setFieldValue(0);
        fields[3].setFieldValue(0);

        SudokuBox row3 = new SudokuBox(fields);
        assertTrue(row3.verify());
    }

    @Test
    void equalsTest() {

        SudokuField[] fields = new SudokuField[9];
        SudokuField[] fields2 = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            fields[i] = new SudokuField();
            fields[i].setFieldValue(i);
            fields2[i] = new SudokuField();
            fields2[i].setFieldValue(i);

        }
        SudokuBox box = new SudokuBox(fields);
        SudokuBox box2 = new SudokuBox(fields2);
        assertNotEquals(box, null);
        assertNotEquals(box, ":)");
        assertEquals(box, box);
        assertEquals(box, box2);

    }

    @Test
    void hashTest() {
        SudokuField[] fields = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            fields[i] = new SudokuField();
            fields[i].setFieldValue(i);

        }
        SudokuBox box = new SudokuBox(fields);

        assertNotNull(box.hashCode());
    }

    @Test
    void toStringTest() {
        SudokuField[] fields = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            fields[i] = new SudokuField();
            fields[i].setFieldValue(i);

        }
        SudokuBox box = new SudokuBox(fields);

        assertNotNull(box.toString());
    }


    @Test
    public void integrationTest(){

        SudokuField[] fields = new SudokuField[9];
        SudokuField[] fields2 = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            fields[i] = new SudokuField();
            fields[i].setFieldValue(i);
            fields2[i] = new SudokuField();
            fields2[i].setFieldValue(i);

        }
        SudokuBox box = new SudokuBox(fields);
        SudokuBox box2 = new SudokuBox(fields2);
        assertEquals(box.hashCode(), box2.hashCode());
        assertEquals(box, box2);

        fields2[0].setFieldValue(9);
        box2 = new SudokuBox(fields2);

        assertNotEquals(box.hashCode(), box2.hashCode());
        assertNotEquals(box, box2);
    }
    @Test
    public void cloneTest() {
        SudokuField[] fields = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            fields[i] = new SudokuField();
            fields[i].setFieldValue(i);
        }
        SudokuBox box = new SudokuBox(fields);
        SudokuBox box2 = (SudokuBox) box.clone();
        assertEquals(box, box2);

    }

}
