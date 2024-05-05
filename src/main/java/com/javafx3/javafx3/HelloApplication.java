package com.javafx3.javafx3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 400);
        stage.setTitle("xmr");
        stage.setScene(scene);
        stage.show();

        HelloController controller = loader.getController();

        stage.setOnCloseRequest(event -> {
            controller.stopMoniterIfNeeded();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}