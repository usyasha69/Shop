package com.example.pk.shop;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

public class ToServeQueueAsyncTask extends AsyncTask<Void, Void, Void> {
    private Shop shop;
    private ShopManager shopManager;
    private Activity activity;
    private TextView queueReport;
    private RecyclerViewAdapter recyclerViewAdapter;

    public ToServeQueueAsyncTask(Shop shop, ShopManager shopManager, Activity activity, TextView queueReport
            , RecyclerViewAdapter recyclerViewAdapter) {
        this.shop = shop;
        this.shopManager = shopManager;
        this.activity = activity;
        this.queueReport = queueReport;
        this.recyclerViewAdapter = recyclerViewAdapter;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final int PRODUCT_QUEUE_SIZE = shopManager.getProductQueue().size();

        for (int i = 0; i < PRODUCT_QUEUE_SIZE; i++) {
            //if async task is cancelled, break of cycle
            if (isCancelled()) {
                break;
            }

            Product product;
            String name = "";
            int count = 0;

            if (!shopManager.getProductQueue().isEmpty()) {
                product = shopManager.getProductQueue().poll();
                name = product.getName();
                count = product.getCount();
            }

            //set number of product in shop
            for (int j = 0; j < shop.getProducts().size(); j++) {
                if (shop.getProducts().get(j).getName().equals(name)) {
                    shop.getProducts().get(j).setCount(shop.getProducts().get(j).getCount() - count);
                    break;
                }
            }

            //update queue report in main activity
            publishProgress();

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
            updateQueueReport();
            recyclerViewAdapter.notifyDataSetChanged();
        }
    }


    /**
     * This method update queue report.
     */
    private void updateQueueReport() {
        queueReport.setText(shopManager.deleteTextFromQueueLabel(queueReport.getText().toString()));

    }
}
