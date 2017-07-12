package com.mz.json.validator;

@SuppressWarnings("WeakerAccess")
public class PropertyCheckException extends CheckException {

    public PropertyCheckException(ComplexCheckable complexCheckable, PropertyPath propertyPath, String message) {
        super(String.format("%s of %s%s %s",
                propertyPath, complexCheckable.getClass().getName(), complexCheckable, message));
    }
}