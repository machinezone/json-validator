package com.mz.json.validator;

import java.util.HashMap;

import static com.mz.json.validator.CheckType.NON_EMPTY_STRING;
import static com.mz.json.validator.CheckType.NOT_NULL;
import static com.mz.json.validator.PropertyPath.getEmptyPropertyPath;

public abstract class AbstractCheckableKeyValuePair<V> extends HashMap<String,V>
        implements ComplexCheckable, SimpleCheckable {

    public AbstractCheckableKeyValuePair(String key, V value) {
        super();
        put(key, value);
    }

    @Override
    public void check() throws CheckException {
        check(this, getEmptyPropertyPath());
    }

    @Override
    public void check(ComplexCheckable complexCheckable, PropertyPath propertyPath) throws CheckException {
        String key = (String)keySet().toArray()[0];
        V value = get(key);

        NON_EMPTY_STRING.check(complexCheckable, propertyPath.getAppendedInstance("$key"), key);
        if (value instanceof ComplexCheckable) {
            ((ComplexCheckable) value).check(complexCheckable, propertyPath.getAppendedInstance(key));
        } else {
            NOT_NULL.check(complexCheckable, propertyPath.getAppendedInstance(key), value);
        }
    }
}