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

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SudokuBoard implements Serializable, Cloneable {

    private SudokuField[][] board = new SudokuField[9][9];

    public SudokuSolver getSolver() {
        return solver;
    }

    private final SudokuSolver solver;

    public SudokuBoard(SudokuSolver i) {
        this.solver = i;

        for (int j = 0; j < 9; j++) {
            for (int k = 0; k < 9; k++) {
                this.board[j][k] = new SudokuField();
            }
        }

    }

    public void solveGame() {
        this.solver.solve(this);
    }


    public int getFieldValue(int x, int y) {
        return this.board[x][y].getFieldValue();
    }

    public void setFieldValue(int x, int y, int value) {
        this.board[x][y].setFieldValue(value);
    }

    public SudokuRow getRow(int y) {
        SudokuField[] row = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            row[i] = this.board[i][y].clone();
        }
        return new SudokuRow(row);

    }

    public SudokuColumn getCollumn(int x) {
        SudokuField[] collumn = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            collumn[i] = board[x][i].clone();
        }
        return new SudokuColumn(collumn);
    }

    public SudokuBox getBox(int x, int y) {
        SudokuField[] box = new SudokuField[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                box[i + (3 * j)] = this.board[i + (3 * x)][j + (3 * y)].clone();
            }
        }
        return new SudokuBox(box);
    }


    public boolean checkBoard(int col, int row, int number) {
        if (number > 9 || number < 0) {
            return false;
        }
        int pom = getFieldValue(col, row);
        setFieldValue(col, row, number);
        boolean result = getCollumn(col).verify()
                && getRow(row).verify() && getBox(col / 3, row / 3).verify();
        setFieldValue(col, row, pom);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SudokuBoard that = (SudokuBoard) o;
        return new EqualsBuilder().append(this.board, that.board).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 31)
                .append(this.board)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("board", this.board)
                .toString();
    }


    @Override
    public SudokuBoard clone() {
        try {
            SudokuBoard clone = (SudokuBoard) super.clone();

            clone.board = new SudokuField[9][9];
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    clone.board[i][j] = this.board[i][j].clone();
                }
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
