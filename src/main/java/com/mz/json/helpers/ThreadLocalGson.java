package com.mz.json.helpers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("WeakerAccess")
public class ThreadLocalGson extends ThreadLocal<Gson> {

    private final GsonBuilder builder;

    public ThreadLocalGson(GsonBuilder builder) {
        this.builder = builder;
    }

    @Override
    protected Gson initialValue() {
        return builder.create();
    }
}