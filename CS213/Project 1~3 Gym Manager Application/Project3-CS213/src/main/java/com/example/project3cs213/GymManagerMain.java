package com.example.project3cs213;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
/**
 * GymManagerMain class to launch the application and start a JavaFX Scene.
 * @author Songyuan Zhang, Ryan S. Lee, Robert Jimenez
 */
public class GymManagerMain extends Application {
    /**
     * Start method to load GymManagerView.fxml and start the primaryStage with Scene.
     * @param primaryStage acts as the base stage to display
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GymManagerView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 800); // tree structor
        primaryStage.setTitle("Gym Manager");
        primaryStage.setScene(scene); // setScene to pay
        primaryStage.show(); // show the scene
    }
    /**
     * Main method to launch the JavaFX application.
     * @param args potential arguments
     */
    public static void main(String[] args) {
        launch();
    }
}