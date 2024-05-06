package com.javafx3.javafx3;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(loader.load(), 800, 400);
        stage.setTitle("xmr");
        stage.setScene(scene);
        stage.show();

        MainController controller = loader.getController();
        controller.setStage(stage);
        stage.setOnCloseRequest(event -> {
            controller.stopMoniterIfNeeded();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}