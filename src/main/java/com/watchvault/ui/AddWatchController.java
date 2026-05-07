package com.watchvault.ui;

import com.watchvault.model.Watch;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddWatchController {

    @FXML
    private TextField modelField;

    @FXML
    private TextField brandField;

    @FXML
    private TextField colorField;

    @FXML
    private TextField movementField;

    @FXML
    private TextField priceField;

    @FXML
    private CheckBox favoriteBox;

    private Watch result;

    private Watch editingWatch;

    @FXML
    private void saveWatch() {

        try {

            String model = modelField.getText();
            String brand = brandField.getText();
            String color = colorField.getText();
            String movement = movementField.getText();

            double price =
                    Double.parseDouble(priceField.getText());

            boolean favorite =
                    favoriteBox.isSelected();

            if (editingWatch == null) {

                result = new Watch(
                        0,
                        model,
                        brand,
                        color,
                        movement,
                        price,
                        favorite
                );

            } else {

                result = new Watch(
                        editingWatch.getId(),
                        model,
                        brand,
                        color,
                        movement,
                        price,
                        favorite
                );
            }

            close();

        } catch (Exception e) {

            Alert alert = new Alert(
                    Alert.AlertType.ERROR
            );

            alert.setTitle("Error");

            alert.setHeaderText(null);

            alert.setContentText(
                    "Introduce datos válidos."
            );

            alert.showAndWait();
        }
    }

    public Watch getResult() {
        return result;
    }

    public void setWatch(Watch watch) {

        editingWatch = watch;

        modelField.setText(watch.getModel());

        brandField.setText(watch.getBrand());

        colorField.setText(watch.getColor());

        movementField.setText(watch.getMovement());

        priceField.setText(
                String.valueOf(watch.getPrice())
        );

        favoriteBox.setSelected(
                watch.isFavorite()
        );
    }

    private void close() {

        ((Stage) modelField
                .getScene()
                .getWindow())
                .close();
    }
}