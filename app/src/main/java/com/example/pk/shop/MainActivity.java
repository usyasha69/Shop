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
    TextView queueReport;

    /**
     * Objects for right shop working.
     */
    private Shop shop;
    private ShopManager shopManager;
    private AsyncTaskManager asyncTaskManager;

    /**
     * Adapter for product list in a shop.
     */
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        shopManager = new ShopManager();
        asyncTaskManager = new AsyncTaskManager();
        openShop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //cancel all async tasks if activity on pause
        asyncTaskManager.cancelSaleAsyncTasks();
        asyncTaskManager.cancelCreateQueueAsyncTasks();
        asyncTaskManager.cancelToServeQueueAsyncTasks();
    }

    /**
     * This method create and filling shop.
     */
    public void openShop() {
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
     * This method served the queue if shop is close and queue is exists.
     */
    private void toServeQueue() {
        asyncTaskManager.setToServeQueueAsyncTask(new ToServeQueueAsyncTask(shop, shopManager, this
                , queueReport, recyclerViewAdapter));

        if (!shopManager.getProductQueue().isEmpty()) {
            asyncTaskManager.getToServeQueueAsyncTask().execute();
        }
    }

    /**
     * This method start sale in a shop.
     */
    private void beginSale() {
        asyncTaskManager.setSaleAsyncTask(new SaleAsyncTask(shop, shopManager, this
                , shopState, recyclerViewAdapter));

        //if saleAsyncTask is running, cancel
        asyncTaskManager.cancelSaleAsyncTasks();

        if (shopManager.isEmptyProductQueue()) {
            makeToast(getString(R.string.toast_begin_sale));
            asyncTaskManager.getSaleAsyncTask().execute();
        }
    }

    /**
     * This method create queue with sales.
     */
    private void createQueue() {
        if (shopManager.getProductQueue() == null) {
            shopManager.setProductQueue(new PriorityQueue<Product>());
        }

        //cancel async task which the serve queue
        asyncTaskManager.cancelToServeQueueAsyncTasks();

        asyncTaskManager.setCreateQueueAsyncTask(new CreateQueueAsyncTask(shop, shopManager
                , this, queueReport));

        makeToast(getString(R.string.toast_create_queue));
        asyncTaskManager.getCreateQueueAsyncTask().execute();
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
