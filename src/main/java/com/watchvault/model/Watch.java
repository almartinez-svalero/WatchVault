package com.watchvault.model;

public class Watch {

    private int id;

    private String model;

    private String brand;

    private String color;

    private String movement;

    private double price;

    private boolean favorite;

    private String image;

    public Watch(
            int id,
            String model,
            String brand,
            String color,
            String movement,
            double price,
            boolean favorite,
            String image
    ) {

        this.id = id;
        this.model = model;
        this.brand = brand;
        this.color = color;
        this.movement = movement;
        this.price = price;
        this.favorite = favorite;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getMovement() {
        return movement;
    }

    public double getPrice() {
        return price;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public String getImage() {
        return image;
    }
}