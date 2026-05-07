package com.watchvault.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class MainController {

    @FXML
    private StackPane contentPane;

    @FXML
    private void openWatches() throws Exception {
        loadView("/fxml/watches.fxml");
    }

    @FXML
    private void openBrands() throws Exception {
        loadView("/fxml/brands.fxml");
    }

    @FXML
    private void openCollections() throws Exception {
        loadView("/fxml/collections.fxml");
    }

    private void loadView(String path) throws Exception {
        Parent view = FXMLLoader.load(getClass().getResource(path));
        contentPane.getChildren().setAll(view);
    }
}