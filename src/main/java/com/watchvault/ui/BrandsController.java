package com.watchvault.ui;

import com.watchvault.model.Brand;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class BrandsController {

    @FXML
    private TableView<Brand> table;

    @FXML
    private TableColumn<Brand, String> colName;

    @FXML
    private TableColumn<Brand, String> colCountry;

    @FXML
    private TextField searchField;

    @FXML
    private Button deleteButton;

    private final ObservableList<Brand> brands =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colName.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getName()
                )
        );

        colCountry.setCellValueFactory(
                data -> new SimpleStringProperty(
                        data.getValue().getCountry()
                )
        );

        deleteButton.setDisable(true);

        table.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldSel, newSel) -> {

                    deleteButton.setDisable(
                            newSel == null
                    );
                });

        loadSampleData();

        FilteredList<Brand> filteredData =
                new FilteredList<>(brands, b -> true);

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) -> {

                    filteredData.setPredicate(brand -> {

                        if (newValue == null
                                || newValue.isEmpty()) {
                            return true;
                        }

                        String search =
                                newValue.toLowerCase();

                        return brand.getName()
                                .toLowerCase()
                                .contains(search);
                    });
                }
        );

        SortedList<Brand> sortedData =
                new SortedList<>(filteredData);

        sortedData.comparatorProperty()
                .bind(table.comparatorProperty());

        table.setItems(sortedData);
    }

    @FXML
    private void addBrand() {

        Dialog<ButtonType> dialog =
                new Dialog<>();

        dialog.setTitle("Añadir marca");

        TextField nameField =
                new TextField();

        nameField.setPromptText("Marca");

        TextField countryField =
                new TextField();

        countryField.setPromptText("País");

        VBox content = new VBox(
                10,
                nameField,
                countryField
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

                String name =
                        nameField.getText().trim();

                String country =
                        countryField.getText().trim();

                if (!name.isEmpty()
                        && !country.isEmpty()) {

                    brands.add(
                            new Brand(
                                    name,
                                    country
                            )
                    );
                }
            }
        });
    }

    @FXML
    private void deleteBrand() {

        Brand selected =
                table.getSelectionModel()
                        .getSelectedItem();

        if (selected != null) {
            brands.remove(selected);
        }
    }

    private void loadSampleData() {

        brands.add(
                new Brand("Rolex", "Suiza")
        );

        brands.add(
                new Brand("Omega", "Suiza")
        );

        brands.add(
                new Brand("Audemars Piguet", "Suiza")
        );
    }
}