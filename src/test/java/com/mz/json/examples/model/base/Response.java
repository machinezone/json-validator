package com.mz.json.examples.model.base;

import com.google.gson.annotations.SerializedName;
import com.mz.json.validator.AbstractCheckableObject;
import com.mz.json.validator.CheckRule;

import static com.mz.json.validator.CheckType.NULL_OR_CHECKABLE;

@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
public class Response extends AbstractCheckableObject {
    @CheckRule(NULL_OR_CHECKABLE)
    @SerializedName("errors")
    public ResponseErrors errors;
}