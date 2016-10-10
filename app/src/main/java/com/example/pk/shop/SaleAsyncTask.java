package com.example.pk.shop;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

public class SaleAsyncTask extends AsyncTask<Void, Void, Void> {
    private Shop shop;
    private ShopManager shopManager;
    private Activity activity;
    private TextView shopState;
    private RecyclerViewAdapter recyclerViewAdapter;

    public SaleAsyncTask(Shop shop, ShopManager shopManager, Activity activity
            , TextView shopState, RecyclerViewAdapter recyclerViewAdapter) {
        this.shop = shop;
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

            if (shop.isOpen() && !shopManager.isEmptyProducts(shop)) {
                int randomProduct = (int) (Math.random() * shop.getProducts().size());

                int productNumber = shop.getProducts().get(randomProduct).getCount();
                final int NUMBER_OF_SALE = 1;

                if (productNumber > 0) {
                    shop.getProducts().get(randomProduct).setCount(productNumber - NUMBER_OF_SALE);
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
            if (!shop.isOpen()) {
                shopState.setText(R.string.ma_shop_state_close);
            }

            recyclerViewAdapter.notifyDataSetChanged();
        }
    }
}
