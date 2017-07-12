package com.mz.json.examples.model.complex;

import com.mz.json.validator.AbstractCheckableKeyValuePair;

public final class CustomData extends AbstractCheckableKeyValuePair<Object> {

    private CustomData() {
        super("data", null);
    }

    public CustomData(Object value) {
        super("data", value);
    }
}