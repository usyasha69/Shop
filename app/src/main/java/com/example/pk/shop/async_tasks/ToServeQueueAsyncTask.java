package com.example.pk.shop.async_tasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.pk.shop.MainActivity;
import com.example.pk.shop.managers.AsyncTaskManager;
import com.example.pk.shop.managers.ShopManager;
import com.example.pk.shop.models.ProductModel;
import com.example.pk.shop.models.ShopModel;

public class ToServeQueueAsyncTask extends AsyncTask<Void, Void, Void> {
    private ShopModel shopModel;
    private ShopManager shopManager;
    private Activity activity;

    public ToServeQueueAsyncTask(ShopModel shopModel, ShopManager shopManager, Activity activity) {
        this.shopModel = shopModel;
        this.shopManager = shopManager;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final int PRODUCT_QUEUE_SIZE = shopManager.getProductModelQueue().size();

        for (int i = 0; i < PRODUCT_QUEUE_SIZE; i++) {
            //if async task is cancelled, break of cycle
            if (isCancelled()) {
                break;
            }

            ProductModel productModel;
            String name = "";
            int count = 0;

            if (!shopManager.getProductModelQueue().isEmpty()) {
                productModel = shopManager.getProductModelQueue().poll();
                name = productModel.getName();
                count = productModel.getCount();
            }

            //set number of productModel in shopModel
            for (int j = 0; j < shopModel.getProductModels().size(); j++) {
                if (shopModel.getProductModels().get(j).getName().equals(name)) {
                    shopModel.getProductModels().get(j).setCount(shopModel.getProductModels().get(j).getCount() - count);
                    break;
                }
            }

            //update queue report in main activity
            publishProgress();

            try {
                Thread.sleep(AsyncTaskManager.TIME_DELAY_BETWEEN_PURCHASES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

        if (activity != null) {
            MainActivity mainActivity = (MainActivity) activity;

            mainActivity.updateQueueReport();
            mainActivity.updateAdapter();
        }
    }
}
