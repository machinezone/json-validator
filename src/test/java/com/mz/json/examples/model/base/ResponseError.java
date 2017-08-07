package com.mz.json.examples.model.base;

import com.google.gson.annotations.SerializedName;
import com.mz.json.validator.CheckableObject;
import com.mz.json.validator.CheckRule;
import com.mz.json.validator.StringRepresentation;

import static com.mz.json.validator.CheckType.NON_EMPTY_STRING;
import static com.mz.json.validator.CheckType.POSITIVE_INTEGER;

@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
public class ResponseError implements CheckableObject {
    // Allowed rules: ANY, NULL_OR_CHECKABLE, NOT_NULL, ZERO,
    // NON_NEGATIVE_INTEGER, POSITIVE_INTEGER, NON_EMPTY_STRING, TRUE, FALSE
    @CheckRule(NON_EMPTY_STRING)
    @SerializedName("reason")
    public String reason;

    @CheckRule(POSITIVE_INTEGER)
    @SerializedName("code")
    public Integer code;

    @Override
    public String toString() {
        return StringRepresentation.toString(this);
    }
}