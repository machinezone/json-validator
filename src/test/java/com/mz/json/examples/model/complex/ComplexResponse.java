package com.mz.json.examples.model.complex;

import com.google.gson.annotations.SerializedName;
import com.mz.json.examples.model.base.Response;

@SuppressWarnings({"InstanceVariableMayNotBeInitialized", "unused"})
public class ComplexResponse extends Response {
    @SerializedName("shards")
    public Shards shards;

    @SerializedName("custom")
    public CustomData customData;
}
