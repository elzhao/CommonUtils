package com.elzhao.library.core;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDataSource implements BaseDataSourceInterface {
    private Context context;

    private List<ControllerObserver> observerList = new ArrayList<ControllerObserver>();

    private SparseArray<List<BaseAsyncTask<Object>>> taskMap = new SparseArray<List<BaseAsyncTask<Object>>>();

    public BaseDataSource(Context context) {
        this.context = context;
    }

    @Override
    public final void addControllerObserver(ControllerObserver observer) {
        if (!observerList.contains(observer)) {
            observerList.add(observer);
        }
    }

    @Override
    public final void removeControllerObserver(ControllerObserver observer) {
        if (observerList.contains(observer)) {
            observerList.remove(observer);
        }
    }

    private synchronized void cancelTask(int what) {
        List<BaseAsyncTask<Object>> tasks = taskMap.get(what);
        if (tasks != null) {
            for (BaseAsyncTask<Object> task : tasks) {
                if (task != null && task.isCanCancel == true) {
                    Log.i("BaseDataSource", "cancel last task :" + what);
                    task.cancel(true);
                }
            }
        }
    }

    private synchronized void addTask(int what, BaseAsyncTask<Object> task) {
        List<BaseAsyncTask<Object>> tasks = taskMap.get(what);
        if (tasks == null) {
            tasks = new ArrayList<BaseAsyncTask<Object>>();
            tasks.add(task);
            taskMap.put(what, tasks);
        } else {
            tasks.add(task);
        }
    }

    private synchronized void removeTask(int what, BaseAsyncTask<Object> task) {
        List<BaseAsyncTask<Object>> tasks = taskMap.get(what);
        if (tasks != null) {
            tasks.remove(task);
            if (tasks.size() == 0) {
                taskMap.remove(what);
            }
        }
    }

    @Override
    public final void handleMessage(final BaseMessage msg,
                                    final boolean isCanCancel) {
        // if still has the same task ,cancel it
        final int what = msg.getMessageWhat();
        final int id = msg.getMessageId();
        cancelTask(what);
        BaseAsyncTask<Object> task = new BaseAsyncTask<Object>(context) {
            @Override
            public Object call() throws InterruptedException {
                if (isCanceled()) {
                    throw new InterruptedException("task id " + id + " ,what "
                            + what + " is canceled!");
                }
                Object obj = fetchData(msg);
                if (isCanceled()) {
                    throw new InterruptedException("task id " + id + " ,what "
                            + what + " is canceled!");
                }
                return obj;
            }

            @Override
            protected void onSuccess(Object object) {
                if (object != null) {
                    BaseMessage msgResult = new BaseMessage(msg.getMessageId(),
                            msg.getMessageWhat(), object);
                    receiveMessage(msgResult);
                }
            }

            @Override
            protected void onInterrupted(Exception e) {
                Log.i("BaseDataSource", "onInterrupted task id:" + id);
                super.onInterrupted(e);
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                Log.i("BaseDataSource", "onException task id:" + id);
                super.onException(e);
            }

            @Override
            protected void onFinally() throws RuntimeException {
                // Log.i("BaseDataSource", "onFinally task id:" + id);
                super.onFinally();
                removeTask(what, this);
            }

        };
        task.isCanCancel = isCanCancel;
        task.execute();
        addTask(what, task);
    }

    protected final void receiveMessage(BaseMessage msgResult) {
        for (ControllerObserver observer : observerList) {
            observer.onDataChanged(msgResult);
        }
    }

    public abstract Object fetchData(BaseMessage msg);

}
