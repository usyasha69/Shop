package com.example.pk.shop;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

public class CreateQueueAsyncTask extends AsyncTask<Void, ProductModel, Void> {
    private ShopModel shopModel;
    private ShopManager shopManager;
    private Activity activity;
    private TextView queueLabel;

    public CreateQueueAsyncTask(ShopModel shopModel, ShopManager shopManager, Activity activity, TextView queueLabel) {
        this.shopModel = shopModel;
        this.shopManager = shopManager;
        this.activity = activity;
        this.queueLabel = queueLabel;
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
                    Thread.sleep(AsyncTaskManager.TIME_DELAY_BEETWEEN_PURCHASES);
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
            String textQueue = queueLabel.getText().toString();
            if (productModels[0] != null) {
                queueLabel.setText(textQueue + " \"" + productModels[0].getName()
                        + "" + productModels[0].getCount() + "\"");
            }
        }
    }
}
