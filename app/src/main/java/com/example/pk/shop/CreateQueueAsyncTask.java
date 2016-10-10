package com.example.pk.shop;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

public class CreateQueueAsyncTask extends AsyncTask<Void, Product, Void> {
    private Shop shop;
    private ShopManager shopManager;
    private Activity activity;
    private TextView queueLabel;

    public CreateQueueAsyncTask(Shop shop, ShopManager shopManager, Activity activity, TextView queueLabel) {
        this.shop = shop;
        this.shopManager = shopManager;
        this.activity = activity;
        this.queueLabel = queueLabel;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while (true) {
            if (isCancelled()) {
                break;
            }

            if (!shopManager.isEmptyProducts(shop) && !shop.isOpen()) {
                int randomProduct = (int) (Math.random() * shop.getProducts().size());
                Product product = null;

                if (shopManager.checkProductsNumber(shop.getProducts().get(randomProduct).getName())
                        < shop.getProducts().get(randomProduct).getCount()) {
                    product = new Product();
                    product.setName(shop.getProducts().get(randomProduct).getName());
                    product.setCount(1);
                    shopManager.getProductQueue().add(product);
                }

                publishProgress(product);

                try {
                    Thread.sleep(80);
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
    protected void onProgressUpdate(Product... products) {
        super.onProgressUpdate(products);

        if (activity != null) {
            String textQueue = queueLabel.getText().toString();
            if (products[0] != null) {
                queueLabel.setText(textQueue + " \"" + products[0].getName()
                        + "" + products[0].getCount() + "\"");
            }
        }
    }
}
