package com.joshuagaillard.smartpantrymanager;

public class PantryItem {

    private final long id;
    private final String ingredientName;
    private final double quantity;
    private final String unit;
    private final String expiryDate;

    public PantryItem(
            long id,
            String ingredientName,
            double quantity,
            String unit,
            String expiryDate) {

        this.id = id;
        this.ingredientName = ingredientName;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public long getId() {
        return id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getExpiryDate() {
        return expiryDate;
    }
}