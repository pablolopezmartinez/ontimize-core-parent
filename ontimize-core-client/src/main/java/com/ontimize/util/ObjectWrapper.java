package com.ontimize.util;

public class ObjectWrapper<T> {

    private T wrapped;

    public ObjectWrapper() {
        super();
    }

    public ObjectWrapper(T value) {
        super();
        this.setValue(value);
    }

    public void setValue(T wrapped) {
        this.wrapped = wrapped;
    }

    public T getValue() {
        return this.wrapped;
    }

}
