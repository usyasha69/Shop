package com.example.pk.shop;

import android.os.AsyncTask;

public class AsyncTaskManager {
    /**
     * Time of delay beetween purchases.
     */
    public static final int TIME_DELAY_BEETWEEN_PURCHASES = 80;

    /**
     * Async task for working shop in background.
     */
    private AsyncTask<Void, Void, Void> saleAsyncTask;
    private AsyncTask<Void, Product, Void> createQueueAsyncTask;
    private AsyncTask<Void, Void, Void> toServeQueueAsyncTask;

    public AsyncTask<Void, Void, Void> getSaleAsyncTask() {
        return saleAsyncTask;
    }

    public void setSaleAsyncTask(AsyncTask<Void, Void, Void> saleAsyncTask) {
        this.saleAsyncTask = saleAsyncTask;
    }

    public AsyncTask<Void, Product, Void> getCreateQueueAsyncTask() {
        return createQueueAsyncTask;
    }

    public void setCreateQueueAsyncTask(AsyncTask<Void, Product, Void> createQueueAsyncTask) {
        this.createQueueAsyncTask = createQueueAsyncTask;
    }

    public AsyncTask<Void, Void, Void> getToServeQueueAsyncTask() {
        return toServeQueueAsyncTask;
    }

    public void setToServeQueueAsyncTask(AsyncTask<Void, Void, Void> toServeQueueAsyncTask) {
        this.toServeQueueAsyncTask = toServeQueueAsyncTask;
    }

    /**
     * This method cancel sale async task if he running.
     */
    public void cancelSaleAsyncTasks() {
        //offing saleAsyncTask
        if (saleAsyncTask != null) {
            if (saleAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                saleAsyncTask.cancel(true);
            }
        }
    }

    /**
     * This method cancel create queue async task if he running.
     */
    public void cancelCreateQueueAsyncTasks() {
        //offing createQueueAsyncTask
        if (createQueueAsyncTask != null) {
            if (createQueueAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                createQueueAsyncTask.cancel(true);
            }
        }
    }

    /**
     * This method cancel to serve queue async task if he running.
     */
    public void cancelToServeQueueAsyncTasks() {
        //offing toServeQueueAsyncTask
        if (toServeQueueAsyncTask != null) {
            if (toServeQueueAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                toServeQueueAsyncTask.cancel(true);
            }
        }
    }
}
