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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pl.first.firstjava.view.exceptions.FormException;

public class App extends Application {

    private static Scene scene;

    private static Logger logger = LogManager.getLogger(App.class.getName());


    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"),
                    ResourceBundle.getBundle("pl.first.firstjava.view.bundles.bundle"));
            scene = new Scene(root, 700, 900);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeRoot(Parent root) {
        scene.setRoot(root);
    }

    public static void changeLanguage(String locale) throws FormException {
        Locale.setDefault(new Locale(locale));

        FXMLLoader loader = new FXMLLoader(App.class.getResource("primary.fxml"),
                ResourceBundle.getBundle("pl.first.firstjava.view.bundles.bundle"));
        try {
            scene.setRoot(loader.load());
            logger.debug("Language changed to " + locale);
        } catch (IOException e) {
            throw new FormException("FormException", e);
        }

    }


    public static void main(String[] args) {
        Locale.setDefault(new Locale("pl"));
        launch();
    }

}