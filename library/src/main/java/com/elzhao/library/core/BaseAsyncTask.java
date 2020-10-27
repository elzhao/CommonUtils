package com.elzhao.library.core;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import roboguice.util.RoboAsyncTask;
import roboguice.util.SafeAsyncTask;

public abstract class BaseAsyncTask<T> extends RoboAsyncTask<T> {
    private static final String TAG = "BaseAsyncTask";

    private static int threadNum = 8;

    public boolean isCanCancel = true;

    private boolean isCanceled = false;

    private static ExecutorService executorService = Executors
            .newFixedThreadPool(threadNum);

    protected BaseAsyncTask(Context context) {
        super(context);
    }

    @Override
    public final void execute() {
        executor(executorService);
        super.execute();
    }

    @Override
    public final SafeAsyncTask<T> executor(Executor executor) {
        return super.executor(executorService);
    }

    @Override
    public final Executor executor() {
        return executorService;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    @Override
    public final boolean cancel(boolean mayInterruptIfRunning) {
        if (future == null) {
            return false;
        }

        isCanceled = true;
        return super.cancel(mayInterruptIfRunning);
    }

}
