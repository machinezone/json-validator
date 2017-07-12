package com.mz.json.validator;

import java.util.ArrayList;

import static com.mz.json.validator.CheckType.NOT_NULL;
import static com.mz.json.validator.PropertyPath.getEmptyPropertyPath;

public abstract class AbstractCheckableList<T extends ComplexCheckable> extends ArrayList<T>
        implements ComplexCheckable, SimpleCheckable {

    @Override
    public void check() throws CheckException {
        check(this, getEmptyPropertyPath());
    }

    @Override
    public void check(ComplexCheckable complexCheckable, PropertyPath propertyPath) throws CheckException {
        int index = 0;
        for (T item : this) {
            PropertyPath itemPropertyPath = propertyPath.getAppendedInstance(String.valueOf(index));
            NOT_NULL.check(complexCheckable, itemPropertyPath, item);
            item.check(complexCheckable, itemPropertyPath);
            index ++;
        }
    }
}