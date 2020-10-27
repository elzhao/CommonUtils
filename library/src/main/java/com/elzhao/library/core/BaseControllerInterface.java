package com.elzhao.library.core;

public interface BaseControllerInterface<T> {
    void addObserver(T observer);

    void removeObserver(T observer);

}
