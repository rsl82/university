package com.example.pr4;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Class that starts the program.
 * @author Ryan S. Lee, Songyuan Zhang
 */
public class PizzaStoreMain extends Application {

    /**
     * Makes the window for the MainView.
     * @param stage Parameters for the window.
     * @throws IOException Required to make new window.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 480);
        stage.setTitle("RU PIZZA");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Starting method.
     * @param args Required for main method.
     */
    public static void main(String[] args) {
        launch();
    }
}