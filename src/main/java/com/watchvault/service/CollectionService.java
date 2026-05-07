package com.watchvault.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.watchvault.model.CollectionItem;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CollectionService {

    private static final String FILE_PATH =
            "src/main/resources/data/collections.json";

    private final Gson gson =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

    public List<CollectionItem> loadCollections() {

        try {

            FileReader reader =
                    new FileReader(FILE_PATH);

            Type listType =
                    new TypeToken<List<CollectionItem>>() {}.getType();

            List<CollectionItem> collections =
                    gson.fromJson(reader, listType);

            reader.close();

            if (collections == null) {
                return new ArrayList<>();
            }

            return collections;

        } catch (Exception e) {

            return new ArrayList<>();
        }
    }

    public void saveCollections(
            List<CollectionItem> collections
    ) {

        try {

            FileWriter writer =
                    new FileWriter(FILE_PATH);

            gson.toJson(collections, writer);

            writer.flush();

            writer.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}