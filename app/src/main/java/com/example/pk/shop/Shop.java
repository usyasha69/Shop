package com.example.pk.shop;

import java.util.ArrayList;

public class Shop {
    private ArrayList<Product> products;
    private boolean isOpen;

    public Shop() {
    }

    public Shop(ArrayList<Product> products, boolean isOpen) {
        this.products = products;
        this.isOpen = isOpen;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shop shop = (Shop) o;

        if (isOpen != shop.isOpen) return false;
        return products.equals(shop.products);

    }

    @Override
    public int hashCode() {
        int result = products.hashCode();
        result = 31 * result + (isOpen ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "products=" + products +
                ", isOpen=" + isOpen +
                '}';
    }
}
