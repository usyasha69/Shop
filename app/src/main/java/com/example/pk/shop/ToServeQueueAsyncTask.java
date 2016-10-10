package com.example.pk.shop;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

public class ToServeQueueAsyncTask extends AsyncTask<Void, Void, Void> {
    private Shop shop;
    private ShopManager shopManager;
    private Activity activity;
    private TextView queueLabel;
    private RecyclerViewAdapter recyclerViewAdapter;

    public ToServeQueueAsyncTask(Shop shop, ShopManager shopManager, Activity activity, TextView queueLabel
            , RecyclerViewAdapter recyclerViewAdapter) {
        this.shop = shop;
        this.shopManager = shopManager;
        this.activity = activity;
        this.queueLabel = queueLabel;
        this.recyclerViewAdapter = recyclerViewAdapter;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        final int PRODUCT_QUEUE_SIZE = shopManager.getProductQueue().size();

        for (int i = 0; i < PRODUCT_QUEUE_SIZE; i++) {
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

            for (int j = 0; j < shop.getProducts().size(); j++) {
                if (shop.getProducts().get(j).getName().equals(name)) {

                    shop.getProducts().get(j)
                            .setCount(shop.getProducts().get(j).getCount() - count);
                    break;
                }
            }

            publishProgress();

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

        updateQueueLabel();
        recyclerViewAdapter.notifyDataSetChanged();
    }


    /**
     * This method update queue label.
     */
    private void updateQueueLabel() {
        if (activity != null) {
            queueLabel.setText(shopManager.deleteTextFromQueueLabel(queueLabel.getText().toString()));
        }
    }
}
