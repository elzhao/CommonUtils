package com.elzhao.library.core;

public interface BaseDataSourceInterface {
    void addControllerObserver(ControllerObserver observer);

    void removeControllerObserver(ControllerObserver observer);

    void handleMessage(BaseMessage msg, boolean isCanCanceled);

}
