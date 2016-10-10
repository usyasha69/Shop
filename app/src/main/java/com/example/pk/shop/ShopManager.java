package com.example.pk.shop;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class ShopManager {
    /**
     * Product queue, is formed if shop is close.
     */
    private PriorityQueue<Product> productQueue;

    public PriorityQueue<Product> getProductQueue() {
        return productQueue;
    }

    public void setProductQueue(PriorityQueue<Product> productQueue) {
        this.productQueue = productQueue;
    }

    /**
     * This method calculate number of specified product in product queue.
     *
     * @param name - name of product
     * @return - number of products
     */
    public int checkProductsNumber(String name) {
        int count = 0;

        for (Product product : productQueue) {
            if (product.getName().equals(name)) {
                count++;
            }
        }

        return count;
    }

    /**
     * This method checked product queue is empty.
     *
     * @return - is empty
     */
    public boolean isEmptyProductQueue() {
        boolean result = true;

        if (productQueue != null) {
            if (!productQueue.isEmpty()) {
                result = false;
            }
        }

        return result;
    }

    /**
     * This method delete last product in queue label.
     */
    public String deleteTextFromQueueLabel(String queueLabel) {
        String[] queueToArray = queueLabel.split(" ");

        String result = "";

        for (int i = 0; i < queueToArray.length - 1; i++) {
            if (i == queueToArray.length - 1) {
                result += queueToArray[i];
            } else {
                result += queueToArray[i] + " ";
            }
        }

        return result;
    }

    /**
     * This method create and filling shop.
     */
    public Shop createAndFillingShop() {
        Shop shop = new Shop();

        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("Chicken", 20));
        products.add(new Product("Meat", 24));
        products.add(new Product("Potatoes", 21));
        products.add(new Product("Apple", 27));
        products.add(new Product("Banana", 23));

        shop.setProducts(products);
        shop.setOpen(true);

        return shop;
    }

    /**
     * This method checked whether there is a products in shop.
     *
     * @return - is empty
     */
    public boolean isEmptyProducts(Shop shop) {
        boolean result = false;
        int count = 0;

        for (int i = 0; i < shop.getProducts().size(); i++) {
            count += shop.getProducts().get(i).getCount();
        }

        if (count == 0) {
            result = true;

            shop.setOpen(false);
        }

        return result;
    }
}
