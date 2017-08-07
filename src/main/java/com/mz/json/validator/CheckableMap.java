package com.mz.json.validator;

import java.util.Map;

import static com.mz.json.validator.PropertyPath.getEmptyPropertyPath;

public interface CheckableMap<K,V extends ComplexCheckable> extends Map<K,V>, ComplexCheckable, SimpleCheckable {

    @Override
    default void check() throws CheckException {
        check(this, getEmptyPropertyPath());
    }

    @Override
    default void check(ComplexCheckable complexCheckable, PropertyPath propertyPath) throws CheckException {
        Checker.checkMap(this, complexCheckable, propertyPath);
    }
}