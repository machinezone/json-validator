package com.mz.json.examples.model.complex;

import com.mz.json.validator.CheckableKeyValuePair;

import java.util.HashMap;

public final class CustomData extends HashMap<String, Object> implements CheckableKeyValuePair<Object> {

    private CustomData() {
        put("data", null);
    }

    public CustomData(Object value) {
        put("data", value);
    }
}