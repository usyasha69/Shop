package com.example.pk.shop;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

public class SaleAsyncTask extends AsyncTask<Void, Void, Void> {
    private ShopModel shopModel;
    private ShopManager shopManager;
    private Activity activity;
    private TextView shopState;
    private RecyclerViewAdapter recyclerViewAdapter;

    public SaleAsyncTask(ShopModel shopModel, ShopManager shopManager, Activity activity
            , TextView shopState, RecyclerViewAdapter recyclerViewAdapter) {
        this.shopModel = shopModel;
        this.shopManager = shopManager;
        this.activity = activity;
        this.shopState = shopState;
        this.recyclerViewAdapter = recyclerViewAdapter;
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
                Thread.sleep(AsyncTaskManager.TIME_DELAY_BEETWEEN_PURCHASES);
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
            if (!shopModel.isOpen()) {
                shopState.setText(R.string.ma_shop_state_close);
            }

            recyclerViewAdapter.notifyDataSetChanged();
        }
    }
}
