package com.watchvault.model;

public class CollectionItem {

    private String name;

    private int totalWatches;

    public CollectionItem(
            String name,
            int totalWatches
    ) {

        this.name = name;
        this.totalWatches = totalWatches;
    }

    public String getName() {
        return name;
    }

    public int getTotalWatches() {
        return totalWatches;
    }
}