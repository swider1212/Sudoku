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

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.first.firstjava.view.exceptions.FormException;

public class PrimaryController {
    public MenuButton menuButton;
    private char dif = '-';
    private Authors authors = new Authors();

    @FXML
    private void switchToSecondaryE() {
        this.dif = 'E';
        menuButton.setText(ResourceBundle.getBundle("pl.first.firstjava.view.bundles.bundle")
                .getObject("lvlE").toString());
    }

    @FXML
    private void switchToSecondaryM() {
        this.dif = 'M';
        menuButton.setText(ResourceBundle.getBundle("pl.first.firstjava.view.bundles.bundle")
                .getObject("lvlM").toString());
    }

    @FXML
    private void switchToSecondaryH() {
        this.dif = 'H';
        menuButton.setText(ResourceBundle.getBundle("pl.first.firstjava.view.bundles.bundle")
                .getObject("lvlH").toString());
    }

    @FXML
    private void startGame() throws FormException {
        if (this.dif != '-') {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("secondary.fxml"),
                    ResourceBundle.getBundle("pl.first.firstjava.view.bundles.bundle"));
            Parent root;
            try {
                root = loader.load();

            } catch (IOException e) {
                throw new FormException("FormException", e);
            }
            Controller c2 = loader.getController();
            c2.setPoziom(this.dif);
            App.changeRoot(root);
        }

    }

    @FXML
    private void onActionButtonAuthors() {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.getChildren().add(
                new Text("Student 1: " + authors.getObject("author1")
                        + "\nStudent 2: " + authors.getObject("author2")));
        Scene dialogScene = new Scene(dialogVbox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    @FXML
    private void onActionChangeLanguage() throws FormException {
        if (Locale.getDefault().equals(new Locale("pl"))) {
            App.changeLanguage("en");
        } else {
            App.changeLanguage("pl");
        }
    }

}