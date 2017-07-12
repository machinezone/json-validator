package com.mz.json.validator;

interface CheckTypeHandler {
    void check(ComplexCheckable complexCheckable, PropertyPath propertyPath, Object value) throws CheckException;
}