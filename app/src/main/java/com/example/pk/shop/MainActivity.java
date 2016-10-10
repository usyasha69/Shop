package com.example.pk.shop;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pk.shop.async_tasks.CreateQueueAsyncTask;
import com.example.pk.shop.async_tasks.SaleAsyncTask;
import com.example.pk.shop.async_tasks.ToServeQueueAsyncTask;
import com.example.pk.shop.adapters.RecyclerViewAdapter;
import com.example.pk.shop.managers.AsyncTaskManager;
import com.example.pk.shop.managers.ShopManager;
import com.example.pk.shop.models.ProductModel;
import com.example.pk.shop.models.ShopModel;

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
     * Objects for right shopModel working.
     */
    private ShopModel shopModel;
    private ShopManager shopManager;
    private AsyncTaskManager asyncTaskManager;

    /**
     * Adapter for product list in a shopModel.
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
        asyncTaskManager.cancellAllAsyncTasks();
    }

    /**
     * This method create and filling shopModel.
     */
    public void openShop() {
        shopModel = shopManager.createAndFillingShop();

        shopState.setText(R.string.ma_shop_state_open);

        recyclerViewAdapter = new RecyclerViewAdapter(this, shopModel.getProductModels());
        productList.setLayoutManager(new LinearLayoutManager(this));
        productList.setAdapter(recyclerViewAdapter);
    }

    @OnClick(R.id.ma_open)
    public void onClickOpenShop() {
        shopModel.setOpen(true);
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
        shopModel.setOpen(false);
        shopState.setText(R.string.ma_shop_state_close);

        makeToast(getString(R.string.toast_shop_is_close));

        if (!shopModel.isOpen()) {
            createQueue();
        }
    }

    @OnClick(R.id.ma_begin_sale)
    public void onclickBeginSale() {
        if (shopModel.isOpen()) {
            beginSale();
        }
    }

    /**
     * This method update queue report.
     */
    public void updateQueueReport() {
        queueReport.setText(shopManager.deleteTextFromQueueLabel(queueReport.getText().toString()));

    }

    public void updateAdapter() {
        recyclerViewAdapter.notifyDataSetChanged();
    }

    /**
     * This method add text to report queue.
     *
     * @param productModels - product model
     */
    public void addTextToQueueReport(ProductModel... productModels) {
        String textQueue = queueReport.getText().toString();
        if (productModels[0] != null) {
            queueReport.setText(textQueue + " \"" + productModels[0].getName()
                    + "" + productModels[0].getCount() + "\"");
        }
    }

    public void setShopStateText() {
        shopState.setText(R.string.ma_shop_state_close);
    }

    private void makeToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * This method start sale in a shopModel.
     */
    private void beginSale() {
        asyncTaskManager.setSaleAsyncTask(new SaleAsyncTask(shopModel, shopManager, this));

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
        if (shopManager.getProductModelQueue() == null) {
            shopManager.setProductModelQueue(new PriorityQueue<ProductModel>());
        }

        //cancel async task which the serve queue
        asyncTaskManager.cancelToServeQueueAsyncTasks();

        asyncTaskManager.setCreateQueueAsyncTask(new CreateQueueAsyncTask(shopModel, shopManager, this));

        makeToast(getString(R.string.toast_create_queue));
        asyncTaskManager.getCreateQueueAsyncTask().execute();
    }

    /**
     * This method served the queue if shopModel is close and queue is exists.
     */
    private void toServeQueue() {
        asyncTaskManager.setToServeQueueAsyncTask(new ToServeQueueAsyncTask(shopModel, shopManager, this));

        if (!shopManager.getProductModelQueue().isEmpty()) {
            asyncTaskManager.getToServeQueueAsyncTask().execute();
        }
    }
}
