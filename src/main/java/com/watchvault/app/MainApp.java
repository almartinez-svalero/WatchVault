package com.watchvault.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(
                MainApp.class.getResource("/fxml/main.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("WatchVault");
        stage.setScene(scene);
        stage.setWidth(1200);
        stage.setHeight(700);
        stage.setMinWidth(1000);
        stage.setMinHeight(600);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
