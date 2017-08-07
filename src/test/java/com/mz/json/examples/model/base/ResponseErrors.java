package com.mz.json.examples.model.base;

import com.mz.json.validator.CheckableList;

import java.util.ArrayList;

// Allowed parents: CheckableMap, CheckableList, CheckableKeyValuePair
public class ResponseErrors extends ArrayList<ResponseError> implements CheckableList<ResponseError> {}