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


public class SudokuFieldTest {
    public SudokuFieldTest(){}

    @Test
    public void testConstructor(){
        SudokuField field = new SudokuField();
        assertEquals(field.getFieldValue(), 0);
    }

    @Test
    public void testSetter(){
        SudokuField field = new SudokuField();
        field.setFieldValue(1);
        assertEquals(field.getFieldValue(), 1);
        field.setFieldValue(-1);
        assertNotEquals(field.getFieldValue(), -1);
        field.setFieldValue(10);
        assertNotEquals(field.getFieldValue(), 10);
    }

    @Test
    public void equalTest(){
        SudokuField f1 = new SudokuField();
        f1.setFieldValue(1);
        SudokuField f2 = new SudokuField();
        f2.setFieldValue(1);
        assertNotEquals(f1, null);
        assertNotEquals(f1, "smoli ma malego");
        assertEquals(f1, f1);
        assertEquals(f1, f2);
    }

    @Test
    public void toStringTest(){
        SudokuField f1 = new SudokuField();
        assertNotNull(f1.toString());
    }


    @Test
    public void hashTest(){
        SudokuField f1 = new SudokuField();
        assertNotNull(f1.hashCode());
    }

    @Test
    public void integrationTest(){
        SudokuField f1 = new SudokuField();
        f1.setFieldValue(1);
        SudokuField f2 = new SudokuField();
        f2.setFieldValue(1);

        assertEquals(f1.hashCode(), f2.hashCode());
        assertEquals(f1, f2);

        f2.setFieldValue(2);

        assertNotEquals(f1.hashCode(), f2.hashCode());
        assertNotEquals(f1, f2);

    }

    @Test
    public void cloneTest(){
        SudokuField f1 = new SudokuField();
        f1.setFieldValue(2);
        SudokuField f2 = f1.clone();

        assertEquals(f1.getFieldValue(), f2.getFieldValue());

        f1.setFieldValue(3);

        assertNotEquals(f1.getFieldValue(), f2.getFieldValue());

    }

    @Test
    public void comparableTest() {
        SudokuField f1 = new SudokuField();
        SudokuField f2 = new SudokuField();
        f1.setFieldValue(1);
        f2.setFieldValue(2);
        assertEquals(1, f2.compareTo(f1));
        assertEquals(-1, f1.compareTo(f2));
        f2.setFieldValue(1);
        assertEquals(0, f2.compareTo(f1));
        assertEquals(0, f1.compareTo(f2));

        assertThrows(NullPointerException.class, () ->{ f1.compareTo(null);}  );
    }
}
