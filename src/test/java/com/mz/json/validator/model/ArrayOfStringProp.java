package com.mz.json.validator.model;

import com.mz.json.validator.CheckException;
import com.mz.json.validator.ComplexCheckable;
import com.mz.json.validator.PropertyPath;
import com.mz.json.validator.SimpleCheckable;

import java.util.ArrayList;

import static com.mz.json.validator.PropertyPath.getEmptyPropertyPath;

public class ArrayOfStringProp extends ArrayList<StringProp> implements ComplexCheckable, SimpleCheckable {
    @Override
    public void check() throws CheckException {
        check(this, getEmptyPropertyPath());
    }

    @Override
    public void check(ComplexCheckable complexCheckable, PropertyPath propertyPath) throws CheckException {
        for (StringProp stringProp : this) {
            stringProp.check(complexCheckable, propertyPath);
        }
    }
}