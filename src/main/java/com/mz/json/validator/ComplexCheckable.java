package com.mz.json.validator;

public interface ComplexCheckable {
    void check(ComplexCheckable complexCheckable, PropertyPath propertyPath) throws CheckException;
}