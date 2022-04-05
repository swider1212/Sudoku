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
import pl.first.firstjava.model.exceptions.DaoReadException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class FileSudokuBoardDaoTest {

    @BeforeAll
    public static void setup(){
        Locale.setDefault(new Locale("pl"));
    }

    @Test
    public void testWriteRead() throws Throwable {

        BacktrackingSudokuSolver solver1 = new BacktrackingSudokuSolver();
        SudokuBoard instance1 = new SudokuBoard(solver1);
        SudokuBoard dao1;

        try(FileSudokuBoardDao sFile = (FileSudokuBoardDao) SudokuBoardDaoFactory.getFileDao("testFile")){
                sFile.write(instance1);
        }

        try(FileSudokuBoardDao sFile = (FileSudokuBoardDao) SudokuBoardDaoFactory.getFileDao("testFile")){
                dao1 = sFile.read();
        }


        assertEquals(instance1, dao1);
        Files.deleteIfExists(Paths.get(System.getProperty("user.dir") + File.separator + "testFile"));

    }

    @Test
    public void testHashCode() {
        FileSudokuBoardDao file1 = new FileSudokuBoardDao("testFile1");
        FileSudokuBoardDao file2 = new FileSudokuBoardDao("testFile2");
        FileSudokuBoardDao file3 = new FileSudokuBoardDao("testFile2");

        assertNotEquals(file1, file2);
        assertNotEquals(file1.hashCode(), file2.hashCode());
        assertEquals(file2.hashCode(), file3.hashCode());
        assertEquals(file2, file3);
    }

    @Test
    public void testToString(){
        FileSudokuBoardDao file1 = new FileSudokuBoardDao("testFile1");
        assertTrue(file1.toString() != null);
    }

    @Test
    public void testEquals(){
        FileSudokuBoardDao file1 = new FileSudokuBoardDao("testFile1");
        FileSudokuBoardDao file2 = new FileSudokuBoardDao("testFile2");
        FileSudokuBoardDao file3 = new FileSudokuBoardDao("testFile2");

        assertNotEquals(file1, null);
        assertNotEquals(file1, 'a');
        assertEquals(file1, file1);
        assertNotEquals(file1, file2);
        assertEquals(file2, file3);

        file1.close();
        file2.close();
        file3.close();
    }

    @Test
    public void testAutoCloseable(){
        try{
            FileSudokuBoardDao file1 = new FileSudokuBoardDao("testFile1");
            try{
                file1.read();
            }finally {
                if(file1 != null){
                    file1.close();
                }
            }
        }catch (DaoReadException e){
        }
    }
}

