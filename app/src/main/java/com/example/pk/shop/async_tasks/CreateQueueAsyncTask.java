package com.example.pk.shop.async_tasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.pk.shop.MainActivity;
import com.example.pk.shop.managers.AsyncTaskManager;
import com.example.pk.shop.managers.ShopManager;
import com.example.pk.shop.models.ProductModel;
import com.example.pk.shop.models.ShopModel;

public class CreateQueueAsyncTask extends AsyncTask<Void, ProductModel, Void> {
    private ShopModel shopModel;
    private ShopManager shopManager;
    private Activity activity;

    public CreateQueueAsyncTask(ShopModel shopModel, ShopManager shopManager, Activity activity) {
        this.shopModel = shopModel;
        this.shopManager = shopManager;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (true) {
            //if async task is cancelled, break of cycle.
            if (isCancelled()) {
                break;
            }

            //create queue in close shopModel
            if (!shopManager.isEmptyProducts(shopModel) && !shopModel.isOpen()) {
                int randomProduct = (int) (Math.random() * shopModel.getProductModels().size());
                ProductModel productModel = null;

                //products number in shopModel and queue
                int productsNumberInQueue =
                        shopManager.checkProductsNumber(shopModel.getProductModels().get(randomProduct).getName());
                int productsNumberInShop = shopModel.getProductModels().get(randomProduct).getCount();

                if (productsNumberInQueue < productsNumberInShop) {
                    //number of sale productModel
                    final int PRODUCT_NUMBER = 1;

                    productModel = new ProductModel();
                    productModel.setName(shopModel.getProductModels().get(randomProduct).getName());
                    productModel.setCount(PRODUCT_NUMBER);

                    shopManager.getProductModelQueue().add(productModel);
                }

                //update queue report text view in main activity
                publishProgress(productModel);

                try {
                    Thread.sleep(AsyncTaskManager.TIME_DELAY_BETWEEN_PURCHASES);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } else {
                break;
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(ProductModel... productModels) {
        super.onProgressUpdate(productModels);

        if (activity != null) {
            MainActivity mainActivity = (MainActivity) activity;

            mainActivity.addTextToQueueReport(productModels[0]);
        }
    }
}
