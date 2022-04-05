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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BacktrackingSudokuSolver implements SudokuSolver {

    @Override
    public void solve(SudokuBoard board) {
        solveGame(board);
    }


    public boolean solveGame(SudokuBoard board) {
        Integer[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        List<Integer> intList = Arrays.asList(numbers);
        Collections.shuffle(intList);
        intList.toArray(numbers);


        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board.getFieldValue(col, row) == 0) {

                    for (int n : numbers) {

                        if (board.checkBoard(col, row, n)) {
                            board.setFieldValue(col, row, n);

                            if (solveGame(board)) {
                                return true;
                            } else {
                                board.setFieldValue(col, row, 0);
                            }
                        }
                    }
                    return false;

                }

            }
        }
        return true;
    }
}
