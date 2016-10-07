package com.example.pk.shop;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.ma_product_list)
    ListView productList;
    @BindView(R.id.ma_shop_state)
    TextView shopState;
    @BindView(R.id.ma_queue)
    TextView queue;

    private Shop shop;

    private Handler mainHandler;
    private Handler createQueueHandler;
    private Handler toServeQueueHandler;

    private ArrayList<Product> queueList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        createAndFillingShop();
    }

    /**
     * This method create and filling shop.
     */
    private void createAndFillingShop() {
        shop = new Shop();

        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("Chicken", 20));
        products.add(new Product("Meat", 24));
        products.add(new Product("Potatoes", 21));
        products.add(new Product("Apple", 27));
        products.add(new Product("Banana", 23));

        shop.setProducts(products);
        shop.setOpen(true);

        shopState.setText(R.string.ma_shop_state_open);
        productList.setAdapter(new ProductAdapter(this, products));
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
        }

        if (toServeQueueHandler != null) {
            toServeQueueHandler.removeCallbacksAndMessages(null);
        }

        if (createQueueHandler != null) {
            createQueueHandler.removeCallbacksAndMessages(null);
        }
    }

    @OnClick(R.id.ma_open)
    public void onClickOpenShop() {
        shop.setOpen(true);
        shopState.setText(R.string.ma_shop_state_open);

        if (queueListIsEmpty()) {
            makeToast("Shop is open! Welcome!");
        } else {
            makeToast("To serve queue!");
            toServeQueue();
        }
    }

    @OnClick(R.id.ma_close)
    public void onClickCloseShop() {
        shop.setOpen(false);
        shopState.setText(R.string.ma_shop_state_close);

        makeToast("Shop is close!");

        if (!shop.isOpen() && mainHandler != null) {
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
        toServeQueueHandler = new Handler();

        if (!queueList.isEmpty()) {
            for (int i = queueList.size() - 1; i >= 0; i--) {
                final int finalI = i;

                toServeQueueHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        String name = queueList.get(finalI).getName();
                        int count = queueList.get(finalI).getCount();

                        for (int j = 0; j < shop.getProducts().size(); j++) {
                            if (shop.getProducts().get(j).getName().equals(name)) {

                                shop.getProducts().get(j)
                                        .setCount(shop.getProducts().get(j).getCount() - count);
                                break;
                            }
                        }

                        deleteTextFromQueue();

                        productList.setAdapter(new ProductAdapter(
                                MainActivity.this, shop.getProducts()));

                        if (finalI == 0) {
                            queueList.clear();
                        }
                    }
                }, (80 * (queueList.size() - finalI)));
            }
        }
    }

    /**
     * This method start sale in shop.
     */
    private void beginSale() {
        //if handler != null remove callbacks
        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
        }

        mainHandler = new Handler();

        if (queueListIsEmpty()) {
            makeToast("Begin sale!");
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (shop.isOpen() && !productsIsEmpty()) {
                        int randomProduct = (int) (Math.random() * shop.getProducts().size());

                        if (shop.getProducts().get(randomProduct).getCount() > 0) {
                            shop.getProducts().get(randomProduct).setCount(
                                    shop.getProducts().get(randomProduct).getCount() - 1);
                        }

                        productList.setAdapter(new ProductAdapter(
                                MainActivity.this, shop.getProducts()));
                        mainHandler.postDelayed(this, 80);
                    }
                }
            });
        }
    }

    /**
     * This method create queue with products, which will be serviced.
     */
    private void createQueue() {
        createQueueHandler = new Handler();

        if (queueList == null) {
            queueList = new ArrayList<>();
        }

        if (queueList.size() == 0) {
            makeToast("Create queue!");
            createQueueHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!productsIsEmpty() && !shop.isOpen()) {
                        int randomProduct = (int) (Math.random() * shop.getProducts().size());
                        Product product = null;

                        if (checkProductNumber(shop.getProducts().get(randomProduct).getName())
                                < shop.getProducts().get(randomProduct).getCount()) {
                            product = new Product();
                            product.setName(shop.getProducts().get(randomProduct).getName());
                            product.setCount(1);
                            queueList.add(product);
                        }

                        String textQueue = queue.getText().toString();

                        if (product != null) {
                            queue.setText(textQueue + " \"" + product.getName() + "" + product.getCount() + "\"");
                        }

                        mainHandler.postDelayed(this, 80);
                    }
                }
            });
        }
    }

    /**
     * This method checked whether there is a products im shop.
     *
     * @return - isThere
     */
    private boolean productsIsEmpty() {
        boolean result = false;
        int count = 0;

        for (int i = 0; i < shop.getProducts().size(); i++) {
            count += shop.getProducts().get(i).getCount();
        }

        if (count == 0) {
            result = true;

            makeToast("Product ending!");

            shop.setOpen(false);

            shopState.setText(R.string.ma_shop_state_close);
        }

        return result;
    }

    /**
     * This method delete last product in queue.
     */
    private void deleteTextFromQueue() {
        String[] queueToArray = queue.getText().toString().split(" ");

        String result = "";

        for (int i = 0; i < queueToArray.length - 1; i++) {
            if (i == queueToArray.length - 1) {
                result += queueToArray[i];
            } else {
                result += queueToArray[i] + " ";
            }
        }

        queue.setText(result);
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * This method checked queue list is empty.
     *
     * @return - is empty
     */
    private boolean queueListIsEmpty() {
        boolean result = true;

        if (queueList != null) {
            if (queueList.size() != 0) {
                result = false;
            }
        }

        return result;
    }

    /**
     * This method calculate number of specified product in queue list.
     *
     * @param name - name of product
     * @return - number of products
     */
    private int checkProductNumber(String name) {
        int count = 0;

        for (int i = 0; i < queueList.size(); i++) {
            if (queueList.get(i).getName().equals(name)) {
                count++;
            }
        }

        return count;
    }
}
