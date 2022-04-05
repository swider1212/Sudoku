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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.first.firstjava.model.exceptions.DaoReadException;
import pl.first.firstjava.model.exceptions.DaoWriteException;

public class FileSudokuBoardDao implements Dao<SudokuBoard> {

    private final String filename;
    private static final Logger logger = (Logger) LogManager
            .getLogger(FileSudokuBoardDao.class.getName());

    public FileSudokuBoardDao(String filename) {
        this.filename = filename;
    }

    @Override
    public void close() {
    }

    @Override
    public SudokuBoard read() throws DaoReadException {
        SudokuBoard board1;

        try (FileInputStream fileIn = new FileInputStream(this.filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            board1 = (SudokuBoard) in.readObject();
            logger.debug("file loaded: " + this.filename);
        } catch (IOException | ClassNotFoundException | NullPointerException e) {
            throw new DaoReadException("IOException", e);
        }

        return board1;
    }

    @Override
    public void write(SudokuBoard board1) throws DaoWriteException {

        try (FileOutputStream fileOut = new FileOutputStream(this.filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(board1);
            logger.debug("file saved: " + this.filename);

        } catch (IOException | NullPointerException e) {
            throw new DaoWriteException("IOException", e);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("filename ", filename)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }

        FileSudokuBoardDao rhs = (FileSudokuBoardDao) obj;
        return new EqualsBuilder()
                .append(filename, rhs.filename)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(filename)
                .toHashCode();
    }
}
