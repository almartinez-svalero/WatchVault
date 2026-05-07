package com.watchvault.ui;

import com.watchvault.model.Watch;
import com.watchvault.service.WatchService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.PrintWriter;

public class WatchesController {

    @FXML
    private TableView<Watch> table;

    @FXML
    private TableColumn<Watch, String> colModel;

    @FXML
    private TableColumn<Watch, String> colBrand;

    @FXML
    private TableColumn<Watch, String> colColor;

    @FXML
    private TableColumn<Watch, String> colMovement;

    @FXML
    private TableColumn<Watch, Double> colPrice;

    @FXML
    private TableColumn<Watch, Boolean> colFavorite;

    @FXML
    private TextField searchField;

    @FXML
    private Button deleteButton;

    @FXML
    private ImageView previewImage;

    private final ObservableList<Watch> watches =
            FXCollections.observableArrayList();

    private final WatchService service =
            new WatchService();

    @FXML
    public void initialize() {

        colModel.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getModel()
                )
        );

        colBrand.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getBrand()
                )
        );

        colColor.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getColor()
                )
        );

        colMovement.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getMovement()
                )
        );

        colPrice.setCellValueFactory(
                data -> new SimpleDoubleProperty(
                        data.getValue().getPrice()
                ).asObject()
        );

        colFavorite.setCellValueFactory(
                data -> new SimpleBooleanProperty(
                        data.getValue().isFavorite()
                )
        );

        colFavorite.setCellFactory(column ->
                new TableCell<>() {

                    @Override
                    protected void updateItem(
                            Boolean item,
                            boolean empty
                    ) {

                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item ? "●" : "");
                        }
                    }
                }
        );

        deleteButton.setDisable(true);

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {

                    deleteButton.setDisable(
                            newSel == null
                    );

                    if (newSel != null) {

                        try {

                            Image image = new Image(
                                    getClass().getResourceAsStream(
                                            "/images/" +
                                                    newSel.getImage()
                                    )
                            );

                            previewImage.setImage(image);

                        } catch (Exception e) {

                            previewImage.setImage(null);
                        }
                    }
                });

        table.setRowFactory(tv -> {

            TableRow<Watch> row =
                    new TableRow<>();

            row.setOnMouseClicked(event -> {

                if (event.getClickCount() == 2
                        && !row.isEmpty()) {

                    editWatch(row.getItem());
                }
            });

            return row;
        });

        watches.addAll(service.loadWatches());

        FilteredList<Watch> filteredData =
                new FilteredList<>(watches, b -> true);

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    filteredData.setPredicate(watch -> {

                        if (newValue == null
                                || newValue.isEmpty()) {
                            return true;
                        }

                        String search =
                                newValue.toLowerCase();

                        if (watch.getModel()
                                .toLowerCase()
                                .contains(search)) {
                            return true;
                        }

                        if (watch.getBrand()
                                .toLowerCase()
                                .contains(search)) {
                            return true;
                        }

                        return false;
                    });
                }
        );

        SortedList<Watch> sortedData =
                new SortedList<>(filteredData);

        sortedData.comparatorProperty()
                .bind(table.comparatorProperty());

        table.setItems(sortedData);
    }

    @FXML
    private void addWatch() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/add_watch.fxml")
            );

            Parent root = loader.load();

            AddWatchController controller =
                    loader.getController();

            Stage dialog = new Stage();

            dialog.setTitle("Añadir reloj");

            dialog.initModality(
                    Modality.APPLICATION_MODAL
            );

            dialog.setScene(new Scene(root));

            dialog.setResizable(false);

            dialog.showAndWait();

            Watch newWatch =
                    controller.getResult();

            if (newWatch != null) {

                watches.add(newWatch);

                service.saveWatches(watches);
            }

        } catch (Exception e) {

            showError();
        }
    }

    private void editWatch(Watch watch) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/add_watch.fxml")
            );

            Parent root = loader.load();

            AddWatchController controller =
                    loader.getController();

            controller.setWatch(watch);

            Stage dialog = new Stage();

            dialog.setTitle("Editar reloj");

            dialog.initModality(
                    Modality.APPLICATION_MODAL
            );

            dialog.setScene(new Scene(root));

            dialog.setResizable(false);

            dialog.showAndWait();

            Watch updatedWatch =
                    controller.getResult();

            if (updatedWatch != null) {

                int index =
                        watches.indexOf(watch);

                watches.set(index, updatedWatch);

                service.saveWatches(watches);
            }

        } catch (Exception e) {

            showError();
        }
    }

    @FXML
    private void deleteWatch() {

        Watch selected =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selected == null) {
            return;
        }

        Alert confirm = new Alert(
                Alert.AlertType.CONFIRMATION
        );

        confirm.setTitle("Eliminar reloj");

        confirm.setHeaderText(null);

        confirm.setContentText(
                "¿Eliminar el reloj seleccionado?"
        );

        if (confirm.showAndWait()
                .orElse(ButtonType.CANCEL)
                == ButtonType.OK) {

            watches.remove(selected);

            service.saveWatches(watches);

            previewImage.setImage(null);
        }
    }

    @FXML
    private void exportCSV() {

        try {

            PrintWriter writer =
                    new PrintWriter(
                            new FileWriter(
                                    "watches_export.csv"
                            )
                    );

            writer.println(
                    "Modelo,Marca,Color,Movimiento,Precio,Favorito"
            );

            for (Watch watch : watches) {

                writer.println(
                        watch.getModel() + "," +
                                watch.getBrand() + "," +
                                watch.getColor() + "," +
                                watch.getMovement() + "," +
                                watch.getPrice() + "," +
                                watch.isFavorite()
                );
            }

            writer.close();

            Alert alert =
                    new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Exportación");

            alert.setHeaderText(null);

            alert.setContentText(
                    "CSV exportado correctamente."
            );

            alert.showAndWait();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void showError() {

        Alert alert = new Alert(
                Alert.AlertType.ERROR
        );

        alert.setTitle("Error");

        alert.setHeaderText(null);

        alert.setContentText(
                "No se pudo abrir el formulario."
        );

        alert.showAndWait();
    }
}