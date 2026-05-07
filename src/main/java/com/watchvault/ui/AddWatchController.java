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
    private TextField imageField;

    @FXML
    private CheckBox favoriteBox;

    private Watch result;

    private Watch editingWatch;

    @FXML
    private void saveWatch() {

        try {

            String model =
                    modelField.getText().trim();

            String brand =
                    brandField.getText().trim();

            String color =
                    colorField.getText().trim();

            String movement =
                    movementField.getText().trim();

            String image =
                    imageField.getText().trim();

            String priceText =
                    priceField.getText().trim();

            if (model.isEmpty()
                    || brand.isEmpty()
                    || color.isEmpty()
                    || movement.isEmpty()
                    || image.isEmpty()
                    || priceText.isEmpty()) {

                showError(
                        "Todos los campos son obligatorios."
                );

                return;
            }

            double price =
                    Double.parseDouble(priceText);

            if (price < 0) {

                showError(
                        "El precio no puede ser negativo."
                );

                return;
            }

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
                        favorite,
                        image
                );

            } else {

                result = new Watch(
                        editingWatch.getId(),
                        model,
                        brand,
                        color,
                        movement,
                        price,
                        favorite,
                        image
                );
            }

            close();

        } catch (NumberFormatException e) {

            showError(
                    "El precio debe ser numérico."
            );

        } catch (Exception e) {

            showError(
                    "Error al guardar el reloj."
            );
        }
    }

    public Watch getResult() {
        return result;
    }

    public void setWatch(Watch watch) {

        editingWatch = watch;

        modelField.setText(
                watch.getModel()
        );

        brandField.setText(
                watch.getBrand()
        );

        colorField.setText(
                watch.getColor()
        );

        movementField.setText(
                watch.getMovement()
        );

        priceField.setText(
                String.valueOf(
                        watch.getPrice()
                )
        );

        imageField.setText(
                watch.getImage()
        );

        favoriteBox.setSelected(
                watch.isFavorite()
        );
    }

    private void showError(String message) {

        Alert alert = new Alert(
                Alert.AlertType.ERROR
        );

        alert.setTitle("Error");

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    private void close() {

        ((Stage) modelField
                .getScene()
                .getWindow())
                .close();
    }
}