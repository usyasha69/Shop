package com.example.pk.shop;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

public class ToServeQueueAsyncTask extends AsyncTask<Void, Void, Void> {
    private ShopModel shopModel;
    private ShopManager shopManager;
    private Activity activity;
    private TextView queueReport;
    private RecyclerViewAdapter recyclerViewAdapter;

    public ToServeQueueAsyncTask(ShopModel shopModel, ShopManager shopManager, Activity activity, TextView queueReport
            , RecyclerViewAdapter recyclerViewAdapter) {
        this.shopModel = shopModel;
        this.shopManager = shopManager;
        this.activity = activity;
        this.queueReport = queueReport;
        this.recyclerViewAdapter = recyclerViewAdapter;
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
