package com.example.pk.shop.async_tasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.example.pk.shop.MainActivity;
import com.example.pk.shop.managers.AsyncTaskManager;
import com.example.pk.shop.managers.ShopManager;
import com.example.pk.shop.models.ShopModel;

public class SaleAsyncTask extends AsyncTask<Void, Void, Void> {
    private ShopModel shopModel;
    private ShopManager shopManager;
    private Activity activity;

    public SaleAsyncTask(ShopModel shopModel, ShopManager shopManager, Activity activity) {
        this.shopModel = shopModel;
        this.shopManager = shopManager;
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (true) {
            //if async task is cancelled, break of cycle
            if (isCancelled()) {
                break;
            }

            if (shopModel.isOpen() && !shopManager.isEmptyProducts(shopModel)) {
                int randomProduct = (int) (Math.random() * shopModel.getProductModels().size());

                int productNumber = shopModel.getProductModels().get(randomProduct).getCount();
                final int NUMBER_OF_SALE = 1;

                if (productNumber > 0) {
                    shopModel.getProductModels().get(randomProduct).setCount(productNumber - NUMBER_OF_SALE);
                }

                //update recycler view in main activity
                publishProgress();
            } else {
                break;
            }

            //set time delay
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

            if (!shopModel.isOpen()) {
                mainActivity.setShopStateText();
            }

            mainActivity.updateAdapter();
        }
    }
}
