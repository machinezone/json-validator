package com.mz.json.validator;

import java.util.HashMap;

import static com.mz.json.validator.CheckType.NOT_NULL;
import static com.mz.json.validator.PropertyPath.getEmptyPropertyPath;

public abstract class AbstractCheckableMap<K,V extends ComplexCheckable> extends HashMap<K,V>
        implements ComplexCheckable, SimpleCheckable {

    @Override
    public void check() throws CheckException {
        check(this, getEmptyPropertyPath());
    }

    @Override
    public void check(ComplexCheckable complexCheckable, PropertyPath propertyPath) throws CheckException {
        for (Entry<K,V> entry : entrySet()) {
            PropertyPath entryPropertyPath = propertyPath.getAppendedInstance(entry.getKey().toString());
            NOT_NULL.check(complexCheckable, entryPropertyPath, entry.getValue());
            entry.getValue().check(complexCheckable, entryPropertyPath);
        }
    }
}