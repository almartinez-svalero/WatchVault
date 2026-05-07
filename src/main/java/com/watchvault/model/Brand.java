package com.watchvault.model;

public class Brand {

    private String name;

    private String country;

    public Brand(
            String name,
            String country
    ) {

        this.name = name;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }
}