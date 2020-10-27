package com.elzhao.library.core;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseController<T> implements ControllerObserver,
        BaseControllerInterface<T> {
    private BaseDataSourceInterface dataSource;

    public List<T> observerList = new ArrayList<T>();

    private static int gMessageId = 1;

    private synchronized static int generateMessageId() {
        if (gMessageId == Integer.MAX_VALUE - 1) {
            gMessageId = 1;
        } else {
            gMessageId++;
        }
        return gMessageId;
    }

    protected final void attachDataSource(BaseDataSourceInterface dataSource) {
        this.dataSource = dataSource;
        this.dataSource.addControllerObserver(this);
    }

    @Override
    public final void addObserver(T observer) {
        synchronized (this) {
            if (!observerList.contains(observer)) {
                observerList.add(observer);
            }
        }
    }

    @Override
    public final void removeObserver(T observer) {
        synchronized (this) {
            if (observerList.contains(observer)) {
                observerList.remove(observer);
            }
        }
    }

    private void sendMessage(BaseMessage msg, boolean isCanCancel) {
        if (dataSource != null) {
            dataSource.handleMessage(msg, isCanCancel);
        }
    }

    protected final int sendMessage(int what) {
        return sendMessageWithCancel(what, false);
    }

    protected final int sendMessageWithCancel(int what, boolean isCanCancel) {
        int messageId = generateMessageId();
        sendMessage(new BaseMessage(messageId, what), isCanCancel);
        return messageId;
    }

    protected final int sendMessage(int what, Object object) {
        return sendMessageWithCancel(what, object, false);
    }

    protected final int sendMessageWithCancel(int what, Object object,
                                              boolean isCanCancel) {
        int messageId = generateMessageId();
        sendMessage(new BaseMessage(messageId, what, object), isCanCancel);
        return messageId;
    }

}
