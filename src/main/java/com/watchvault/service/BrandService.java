package com.watchvault.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.watchvault.model.Brand;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BrandService {

    private static final String FILE_PATH =
            "src/main/resources/data/brands.json";

    private final Gson gson =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

    public List<Brand> loadBrands() {

        try {

            FileReader reader =
                    new FileReader(FILE_PATH);

            Type listType =
                    new TypeToken<List<Brand>>() {}.getType();

            List<Brand> brands =
                    gson.fromJson(reader, listType);

            reader.close();

            if (brands == null) {
                return new ArrayList<>();
            }

            return brands;

        } catch (Exception e) {

            return new ArrayList<>();
        }
    }

    public void saveBrands(List<Brand> brands) {

        try {

            FileWriter writer =
                    new FileWriter(FILE_PATH);

            gson.toJson(brands, writer);

            writer.flush();

            writer.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}