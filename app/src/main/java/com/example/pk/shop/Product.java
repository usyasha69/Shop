package com.example.pk.shop;

public class Product {
    private String name;
    private int count;

    public Product() {

    }

    public Product(String name, int count) {
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

        Product product = (Product) o;

        if (!name.equals(product.name)) return false;
        return count == product.count;

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + count;
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
