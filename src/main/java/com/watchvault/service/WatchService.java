package com.watchvault.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.watchvault.model.Watch;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WatchService {

    private static final String FILE_PATH =
            "src/main/resources/data/watches.json";

    private final Gson gson =
            new GsonBuilder().setPrettyPrinting().create();

    public List<Watch> loadWatches() {

        try {

            FileReader reader =
                    new FileReader(FILE_PATH);

            Type listType =
                    new TypeToken<List<Watch>>() {}.getType();

            List<Watch> watches =
                    gson.fromJson(reader, listType);

            reader.close();

            if (watches == null) {
                return new ArrayList<>();
            }

            return watches;

        } catch (Exception e) {

            return new ArrayList<>();
        }
    }

    public void saveWatches(List<Watch> watches) {

        try {

            FileWriter writer =
                    new FileWriter(FILE_PATH);

            gson.toJson(watches, writer);

            writer.flush();

            writer.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}