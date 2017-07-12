package com.mz.json.validator.model;

import com.google.gson.annotations.SerializedName;
import com.mz.json.validator.AbstractCheckableObject;
import com.mz.json.validator.CheckRule;

import static com.mz.json.validator.CheckType.NON_EMPTY_STRING;

public class StringProp extends AbstractCheckableObject {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    @CheckRule(NON_EMPTY_STRING)
    @SerializedName("prop")
    private final String prop;

    public StringProp(String prop) {
        this.prop = prop;
    }
}