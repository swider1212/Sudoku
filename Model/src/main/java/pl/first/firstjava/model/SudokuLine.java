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
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SudokuLine implements Cloneable {
    SudokuLine(SudokuField[] values) {
        this.fields = Arrays.stream(values).toList();
    }

    private final List<SudokuField> fields;

    public boolean verify() {
        int[] arr = new int[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            int finalI = i;
            if (IntStream.of(arr).anyMatch(x -> x == fields.get(finalI).getFieldValue())
                    && fields.get(i).getFieldValue() != 0) {
                return false;
            }
            arr[i] = fields.get(i).getFieldValue();
        }
        return true;
    }

    public SudokuField getField(int i) {
        return fields.get(i);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SudokuLine that = (SudokuLine) o;
        return new EqualsBuilder().append(this.fields, that.fields).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(23, 33)
                .append(this.fields)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fields", this.fields)
                .toString();
    }

    @Override
    public SudokuLine clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change
            //  the internals of the original
            return (SudokuLine) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

