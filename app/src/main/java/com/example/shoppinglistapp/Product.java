package com.example.shoppinglistapp;

public class Product {
    private final long id;
    private final String name;
    private final int quantity;
    private final String category;

    public Product(long id, String name, int quantity, String category) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.category = category;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }
}