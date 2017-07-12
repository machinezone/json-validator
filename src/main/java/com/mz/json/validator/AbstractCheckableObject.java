package com.mz.json.validator;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static com.mz.json.validator.CheckType.ANY;
import static com.mz.json.validator.CheckType.NULL_OR_CHECKABLE;
import static com.mz.json.validator.PropertyPath.getEmptyPropertyPath;

@SuppressWarnings("AbstractClassWithoutAbstractMethods")
public abstract class AbstractCheckableObject extends PrintableObject
        implements ComplexCheckable, SimpleCheckable {

    private static final transient Set<CheckType> COMPATIBLE_WITH_CHECKABLE = new HashSet<>(
            Arrays.asList(ANY, NULL_OR_CHECKABLE));

    @Override
    public void check() throws CheckException {
        check(this, getEmptyPropertyPath());
    }

    @Override
    public void check(ComplexCheckable complexCheckable, PropertyPath propertyPath) throws CheckException {
        int checkedFieldsCount = 0;

        Class<?> currentClass = getClass();
        while ((currentClass != null) && (currentClass != AbstractCheckableObject.class)) {
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

                final Object object = getValue(field);
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
                    if (isComplexCheckable && (!COMPATIBLE_WITH_CHECKABLE.contains(checkType))) {
                        throw new PropertyCheckException(complexCheckable,
                                propertyPath.getAppendedInstance(fieldName),
                                "is inherited from 'ComplexCheckable' interface and also has 'CheckRule' annotation");
                    } else {
                        checkType.check(complexCheckable,
                                propertyPath.getAppendedInstance(fieldName), object);
                    }
                }

                checkedFieldsCount += 1;
            }
            currentClass = currentClass.getSuperclass();
        }
        if (checkedFieldsCount < 1) {
            throw new CheckException(String.format("Class %s%s has no fields to check",
                    getClass().getName(), complexCheckable));
        }
    }
}