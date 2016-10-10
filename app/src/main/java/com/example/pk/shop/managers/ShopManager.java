package com.example.pk.shop.managers;

import com.example.pk.shop.models.ProductModel;
import com.example.pk.shop.models.ShopModel;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class ShopManager {
    /**
     * ProductModel queue, is formed if shop is close.
     */
    private PriorityQueue<ProductModel> productModelQueue;

    public PriorityQueue<ProductModel> getProductModelQueue() {
        return productModelQueue;
    }

    public void setProductModelQueue(PriorityQueue<ProductModel> productModelQueue) {
        this.productModelQueue = productModelQueue;
    }

    /**
     * This method calculate number of specified product in product queue.
     *
     * @param name - name of product
     * @return - number of products
     */
    public int checkProductsNumber(String name) {
        int count = 0;

        for (ProductModel productModel : productModelQueue) {
            if (productModel.getName().equals(name)) {
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

        if (productModelQueue != null) {
            if (!productModelQueue.isEmpty()) {
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
    public ShopModel createAndFillingShop() {
        ShopModel shopModel = new ShopModel();

        ArrayList<ProductModel> productModels = new ArrayList<>();
        productModels.add(new ProductModel("Chicken", 20));
        productModels.add(new ProductModel("Meat", 24));
        productModels.add(new ProductModel("Potatoes", 21));
        productModels.add(new ProductModel("Apple", 27));
        productModels.add(new ProductModel("Banana", 23));

        shopModel.setProductModels(productModels);
        shopModel.setOpen(true);

        return shopModel;
    }

    /**
     * This method checked whether there is a products in shopModel.
     *
     * @return - is empty
     */
    public boolean isEmptyProducts(ShopModel shopModel) {
        boolean result = false;
        int count = 0;

        for (int i = 0; i < shopModel.getProductModels().size(); i++) {
            count += shopModel.getProductModels().get(i).getCount();
        }

        if (count == 0) {
            result = true;

            shopModel.setOpen(false);
        }

        return result;
    }
}
