package com.example.pk.shop;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.PriorityQueue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.ma_product_recycler_view)
    RecyclerView productList;
    @BindView(R.id.ma_shop_state)
    TextView shopState;
    @BindView(R.id.ma_queue)
    TextView queueLabel;

    /**
     * Objects for work with shop.
     */
    private Shop shop;
    private ShopManager shopManager;

    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        shopManager = new ShopManager();
        createAndFillingShop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //offing saleAsyncTask if activity on paused
        if (shopManager.getSaleAsyncTask() != null) {
            if (shopManager.getSaleAsyncTask().getStatus() == AsyncTask.Status.RUNNING) {
                shopManager.getSaleAsyncTask().cancel(true);
            }
        }

        //offing createQueueAsyncTask if activity on paused
        if (shopManager.getCreateQueueAsyncTask() != null) {
            if (shopManager.getCreateQueueAsyncTask().getStatus() == AsyncTask.Status.RUNNING) {
                shopManager.getCreateQueueAsyncTask().cancel(true);
            }
        }

        //offing toServeQueueAsyncTask if activity on paused
        if (shopManager.getToServeQueueAsyncTask() != null) {
            if (shopManager.getToServeQueueAsyncTask().getStatus() == AsyncTask.Status.RUNNING) {
                shopManager.getToServeQueueAsyncTask().cancel(true);
            }
        }
    }

    /**
     * This method create and filling shop.
     */
    public void createAndFillingShop() {
        shop = shopManager.createAndFillingShop();

        shopState.setText(R.string.ma_shop_state_open);

        recyclerViewAdapter = new RecyclerViewAdapter(this, shop.getProducts());
        productList.setLayoutManager(new LinearLayoutManager(this));
        productList.setAdapter(recyclerViewAdapter);
    }

    @OnClick(R.id.ma_open)
    public void onClickOpenShop() {
        shop.setOpen(true);
        shopState.setText(R.string.ma_shop_state_open);

        if (shopManager.isEmptyProductQueue()) {
            makeToast(getString(R.string.toast_shop_is_open));
        } else {
            makeToast(getString(R.string.toast_to_serve_product_queue));
            toServeQueue();
        }
    }

    @OnClick(R.id.ma_close)
    public void onClickCloseShop() {
        shop.setOpen(false);
        shopState.setText(R.string.ma_shop_state_close);

        makeToast(getString(R.string.toast_shop_is_close));

        if (!shop.isOpen()) {
            createQueue();
        }
    }

    @OnClick(R.id.ma_begin_sale)
    public void onclickBeginSale() {
        if (shop.isOpen()) {
            beginSale();
        }
    }

    /**
     * This method served queue which met the during of close.
     */
    private void toServeQueue() {
        shopManager.setToServeQueueAsyncTask(new AsyncTask<Void, Void, Void>() {
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
        });

        if (!shopManager.getProductQueue().isEmpty()) {
            shopManager.getToServeQueueAsyncTask().execute();
        }
    }

    /**
     * This method start sale in shop.
     */
    private void beginSale() {
        shopManager.setSaleAsyncTask(new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                while (true) {
                    if (isCancelled()) {
                        break;
                    }

                    if (shop.isOpen() && !isEmptyProducts()) {
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

                recyclerViewAdapter.notifyDataSetChanged();
            }
        });

        //if saleAsyncTask is running, cancel
        if (shopManager.getSaleAsyncTask().getStatus() == AsyncTask.Status.RUNNING) {
            shopManager.getSaleAsyncTask().cancel(true);
        }

        if (shopManager.isEmptyProductQueue()) {
            makeToast("Begin sale!");
            shopManager.getSaleAsyncTask().execute();
        }
    }

    /**
     * This method create queueLabel with products, which will be serviced.
     */
    private void createQueue() {
        if (shopManager.getProductQueue() == null) {
            shopManager.setProductQueue(new PriorityQueue<Product>());
        }

        shopManager.setCreateQueueAsyncTask(new AsyncTask<Void, Product, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                while (true) {
                    if (isCancelled()) {
                        break;
                    }

                    if (!isEmptyProducts() && !shop.isOpen()) {
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

                String textQueue = queueLabel.getText().toString();

                if (products[0] != null) {
                    queueLabel.setText(textQueue + " \"" + products[0].getName()
                            + "" + products[0].getCount() + "\"");
                }
            }
        });

        makeToast("Create queue!");
        shopManager.getCreateQueueAsyncTask().execute();
    }

    /**
     * This method checked whether there is a products in shop.
     *
     * @return - isThere
     */
    private boolean isEmptyProducts() {
        boolean result = false;
        int count = 0;

        for (int i = 0; i < shop.getProducts().size(); i++) {
            count += shop.getProducts().get(i).getCount();
        }

        if (count == 0) {
            result = true;

            shop.setOpen(false);
        }

        return result;
    }

    /**
     * This method update queue label.
     */
    private void updateQueueLabel() {
        queueLabel.setText(shopManager.deleteTextFromQueueLabel(queueLabel.getText().toString()));
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
