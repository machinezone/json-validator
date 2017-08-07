package com.mz.json.validator;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

import static com.mz.json.validator.CheckType.*;

final class Checker {

    static <T extends ComplexCheckable> void checkList(
            CheckableList<T> list,
            ComplexCheckable complexCheckable,
            PropertyPath propertyPath)
            throws CheckException {
        int index = 0;
        for (T item : list) {
            PropertyPath itemPropertyPath = propertyPath.getAppendedInstance(String.valueOf(index));
            NOT_NULL.check(complexCheckable, itemPropertyPath, item);
            item.check(complexCheckable, itemPropertyPath);
            index++;
        }
    }

    static <K, V extends ComplexCheckable> void checkMap(
            CheckableMap<K, V> map,
            ComplexCheckable complexCheckable,
            PropertyPath propertyPath)
            throws CheckException {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            PropertyPath entryPropertyPath = propertyPath.getAppendedInstance(entry.getKey().toString());
            NOT_NULL.check(complexCheckable, entryPropertyPath, entry.getValue());
            entry.getValue().check(complexCheckable, entryPropertyPath);
        }
    }

    static <V> void checkKeyValuePair(
            CheckableKeyValuePair<V> kv,
            ComplexCheckable complexCheckable,
            PropertyPath propertyPath)
            throws CheckException {
        String key = (String) kv.keySet().toArray()[0];
        V value = kv.get(key);

        NON_EMPTY_STRING.check(complexCheckable, propertyPath.getAppendedInstance("$key"), key);
        if (value instanceof ComplexCheckable) {
            ((ComplexCheckable) value).check(complexCheckable, propertyPath.getAppendedInstance(key));
        } else {
            NOT_NULL.check(complexCheckable, propertyPath.getAppendedInstance(key), value);
        }

    }

    static void checkObject(
            CheckableObject obj,
            ComplexCheckable complexCheckable,
            PropertyPath propertyPath)
            throws CheckException {
        int checkedFieldsCount = 0;

        Class<?> currentClass = obj.getClass();
        while ((currentClass != null) && (currentClass != CheckableObject.class)) {
            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isTransient(field.getModifiers())
                        || field.getName().equals("this$0")) {
                    continue;
                }

                final SerializedName serializedNameAnn = field.getAnnotation(SerializedName.class);
                if (serializedNameAnn == null) {
                    throw new PropertyCheckException(complexCheckable,
                            propertyPath.getAppendedInstance(field.getName()),
                            "has no explicit serialized name");
                }
                final String fieldName = serializedNameAnn.value();

                final Object object = StringRepresentation.getValue(obj, field);
                boolean isComplexCheckable = object instanceof ComplexCheckable;
                if (isComplexCheckable) {
                    ((ComplexCheckable) object).check(complexCheckable,
                            propertyPath.getAppendedInstance(fieldName));
                }

                final CheckRule checkRuleAnnotation = field.getAnnotation(CheckRule.class);
                if (checkRuleAnnotation == null) {
                    if (!isComplexCheckable) {
                        throw new PropertyCheckException(complexCheckable,
                                propertyPath.getAppendedInstance(fieldName),
                                "isn't inherited from 'ComplexCheckable' interface and has no 'CheckRule' annotation");
                    }
                } else {
                    CheckType checkType = checkRuleAnnotation.value();
                    if (isComplexCheckable && (checkType != ANY) && (checkType != NULL_OR_CHECKABLE)) {
                        throw new PropertyCheckException(complexCheckable,
                                propertyPath.getAppendedInstance(fieldName),
                                "is inherited from 'ComplexCheckable' interface and also has 'CheckRule' annotation");
                    } else {
                        checkType.check(complexCheckable,
                                propertyPath.getAppendedInstance(fieldName), object);
                    }
                }

                checkedFieldsCount++;
            }
            currentClass = currentClass.getSuperclass();
        }
        if (checkedFieldsCount < 1) {
            throw new CheckException(String.format("Class %s%s has no fields to check",
                    obj.getClass().getName(), complexCheckable));
        }

    }
}
