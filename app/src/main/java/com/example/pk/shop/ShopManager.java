package com.example.pk.shop;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class ShopManager {
    /**
     * Async task for imitation shop work.
     */
    private AsyncTask<Void, Void, Void> saleAsyncTask;
    private AsyncTask<Void, Product, Void> createQueueAsyncTask;
    private AsyncTask<Void, Void, Void> toServeQueueAsyncTask;

    /**
     * Product queue, is formed if shop is close.
     */
    private PriorityQueue<Product> productQueue;

    public AsyncTask<Void, Void, Void> getSaleAsyncTask() {
        return saleAsyncTask;
    }

    public void setSaleAsyncTask(AsyncTask<Void, Void, Void> saleAsyncTask) {
        this.saleAsyncTask = saleAsyncTask;
    }

    public AsyncTask<Void, Product, Void> getCreateQueueAsyncTask() {
        return createQueueAsyncTask;
    }

    public void setCreateQueueAsyncTask(AsyncTask<Void, Product, Void> createQueueAsyncTask) {
        this.createQueueAsyncTask = createQueueAsyncTask;
    }

    public AsyncTask<Void, Void, Void> getToServeQueueAsyncTask() {
        return toServeQueueAsyncTask;
    }

    public void setToServeQueueAsyncTask(AsyncTask<Void, Void, Void> toServeQueueAsyncTask) {
        this.toServeQueueAsyncTask = toServeQueueAsyncTask;
    }

    public PriorityQueue<Product> getProductQueue() {
        return productQueue;
    }

    public void setProductQueue(PriorityQueue<Product> productQueue) {
        this.productQueue = productQueue;
    }

    /**
     * This method calculate number of specified product in product queue.
     *
     * @param name         - name of product
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
}