package com.mz.json.validator;

import java.util.List;

import static com.mz.json.validator.PropertyPath.getEmptyPropertyPath;

public interface CheckableList<T extends ComplexCheckable> extends List<T>, ComplexCheckable, SimpleCheckable {

    @Override
    default void check() throws CheckException {
        check(this, getEmptyPropertyPath());
    }

    @Override
    default void check(ComplexCheckable complexCheckable, PropertyPath propertyPath) throws CheckException {
        Checker.checkList(this, complexCheckable, propertyPath);
    }
}