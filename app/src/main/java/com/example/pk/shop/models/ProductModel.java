package com.example.pk.shop.models;

import android.support.annotation.NonNull;

public class ProductModel implements Comparable<ProductModel> {
    private String name;
    private int count;

    public ProductModel() {

    }

    public ProductModel(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProductModel productModel = (ProductModel) o;

        if (!name.equals(productModel.name)) return false;
        return count == productModel.count;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + count;
        return result;
    }

    @Override
    public String toString() {
        return "ProductModel{" +
                "name='" + name + '\'' +
                ", count='" + count + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull ProductModel productModel) {
        int result = productModel.getName().compareTo(name);

        if (result == 0) {
            result = productModel.getCount() - count;
        }
        return result;
    }
}
