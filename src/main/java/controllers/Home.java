package controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Home extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Main.fxml"));
            Scene scene = new Scene(root);

            stage.setTitle("Gestion Médicament");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println("Erreur de lecture : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}