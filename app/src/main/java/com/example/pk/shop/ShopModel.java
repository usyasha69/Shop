package com.example.pk.shop;

import java.util.ArrayList;

public class ShopModel {
    private ArrayList<ProductModel> productModels;
    private boolean isOpen;

    public ShopModel() {
    }

    public ShopModel(ArrayList<ProductModel> productModels, boolean isOpen) {
        this.productModels = productModels;
        this.isOpen = isOpen;
    }

    public ArrayList<ProductModel> getProductModels() {
        return productModels;
    }

    public void setProductModels(ArrayList<ProductModel> productModels) {
        this.productModels = productModels;
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

        ShopModel shopModel = (ShopModel) o;

        if (isOpen != shopModel.isOpen) return false;
        return productModels.equals(shopModel.productModels);

    }

    @Override
    public int hashCode() {
        int result = productModels.hashCode();
        result = 31 * result + (isOpen ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ShopModel{" +
                "productModels=" + productModels +
                ", isOpen=" + isOpen +
                '}';
    }
}
