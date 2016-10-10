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
            if (isCancelled()) {
                break;
            }

            if (shop.isOpen() && !shopManager.isEmptyProducts(shop)) {
                int randomProduct = (int) (Math.random() * shop.getProducts().size());

                if (shop.getProducts().get(randomProduct).getCount() > 0) {
                    shop.getProducts().get(randomProduct).setCount(
                            shop.getProducts().get(randomProduct).getCount() - 1);
                }

                publishProgress();
            } else {
                break;
            }

            try {
                Thread.sleep(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

        if (!shop.isOpen() && activity != null) {
            shopState.setText(R.string.ma_shop_state_close);
        }

        if (activity != null)
            recyclerViewAdapter.notifyDataSetChanged();
    }
}
