package com.mz.json.validator;

import static com.mz.json.validator.PropertyPath.getEmptyPropertyPath;

public interface CheckableObject extends ComplexCheckable, SimpleCheckable {

    @Override
    default void check() throws CheckException {
        check(this, getEmptyPropertyPath());
    }

    @Override
    default void check(ComplexCheckable complexCheckable, PropertyPath propertyPath) throws CheckException {
        Checker.checkObject(this, complexCheckable, propertyPath);
    }
}