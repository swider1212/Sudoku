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

package pl.first.firstjava.view;

import java.util.Random;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public enum Poziom {
    LATWY(14),
    SREDNI(27),
    TRUDNY(36);

    private int liczba;

    Poziom(int i) {
        this.liczba = i;
    }

    void start(GridPane board) {
        ObservableList<Node> childrens = board.getChildren();
        for (int i = 0; i < liczba; i++) {
            Random rand = new Random();
            int x = rand.nextInt(81);
            if (((TextField) childrens.get(x)).isEditable()) {
                i--;
            } else {
                childrens.get(x).setStyle("-fx-control-inner-background: #FFFFFF;");
                ((TextField) childrens.get(x)).setText("");
                ((TextField) childrens.get(x)).setEditable(true);
            }
        }
    }

}


