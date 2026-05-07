package com.watchvault.ui;

import com.watchvault.model.CollectionItem;
import com.watchvault.service.CollectionService;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class CollectionsController {

    @FXML
    private TableView<CollectionItem> table;

    @FXML
    private TableColumn<CollectionItem, String> colName;

    @FXML
    private TableColumn<CollectionItem, Integer> colTotal;

    @FXML
    private TextField searchField;

    @FXML
    private Button deleteButton;

    private final ObservableList<CollectionItem> collections =
            FXCollections.observableArrayList();

    private final CollectionService service =
            new CollectionService();

    @FXML
    public void initialize() {

        colName.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getName()
                )
        );

        colTotal.setCellValueFactory(
                data -> new SimpleIntegerProperty(
                        data.getValue().getTotalWatches()
                ).asObject()
        );

        deleteButton.setDisable(true);

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {

                    deleteButton.setDisable(
                            newSel == null
                    );
                });

        collections.addAll(
                service.loadCollections()
        );

        FilteredList<CollectionItem> filteredData =
                new FilteredList<>(collections, b -> true);

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    filteredData.setPredicate(collection -> {

                        if (newValue == null
                                || newValue.isEmpty()) {
                            return true;
                        }

                        return collection.getName()
                                .toLowerCase()
                                .contains(
                                        newValue.toLowerCase()
                                );
                    });
                }
        );

        SortedList<CollectionItem> sortedData =
                new SortedList<>(filteredData);

        sortedData.comparatorProperty()
                .bind(table.comparatorProperty());

        table.setItems(sortedData);
    }

    @FXML
    private void addCollection() {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle("Añadir colección");

        TextField nameField =
                new TextField();

        nameField.setPromptText(
                "Nombre colección"
        );

        TextField totalField =
                new TextField();

        totalField.setPromptText(
                "Cantidad relojes"
        );

        VBox content = new VBox(
                10,
                nameField,
                totalField
        );

        dialog.getDialogPane()
                .setContent(content);

        dialog.getDialogPane()
                .getButtonTypes()
                .addAll(
                        ButtonType.OK,
                        ButtonType.CANCEL
                );

        dialog.showAndWait().ifPresent(response -> {

            if (response == ButtonType.OK) {

                try {

                    String name =
                            nameField.getText().trim();

                    int total =
                            Integer.parseInt(
                                    totalField.getText()
                            );

                    if (!name.isEmpty()) {

                        collections.add(
                                new CollectionItem(
                                        name,
                                        total
                                )
                        );

                        service.saveCollections(
                                collections
                        );
                    }

                } catch (Exception ignored) {
                }
            }
        });
    }

    @FXML
    private void deleteCollection() {

        CollectionItem selected =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selected != null) {

            collections.remove(selected);

            service.saveCollections(
                    collections
            );
        }
    }
}