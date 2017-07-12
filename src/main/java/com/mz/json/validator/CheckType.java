package com.mz.json.validator;

public enum CheckType implements CheckTypeHandler {

    ANY((complexCheckable, propertyPath, value) -> {
    }),
    NULL_OR_CHECKABLE((complexCheckable, propertyPath, value) -> {
        ComplexCheckable checkableValue = getComplexCheckable(complexCheckable, propertyPath, value);
        if (checkableValue != null) {
            checkableValue.check(complexCheckable, propertyPath);
        }
    }),
    NOT_NULL((complexCheckable, propertyPath, value) -> {
        if (value == null) {
            throw new PropertyCheckException(complexCheckable, propertyPath, "is null");
        }
    }),
    ZERO((complexCheckable, propertyPath, value) -> {
        if (getInteger(complexCheckable, propertyPath, value) != 0) {
            throw new PropertyCheckException(complexCheckable, propertyPath, "is not zero");
        }
    }),
    NON_NEGATIVE_INTEGER((complexCheckable, propertyPath, value) -> {
        if (getInteger(complexCheckable, propertyPath, value) < 0) {
            throw new PropertyCheckException(complexCheckable, propertyPath, "is lower than zero");
        }
    }),
    POSITIVE_INTEGER((complexCheckable, propertyPath, value) -> {
        if (getInteger(complexCheckable, propertyPath, value) < 1) {
            throw new PropertyCheckException(complexCheckable, propertyPath, "is lower than 1");
        }
    }),
    NON_EMPTY_STRING((complexCheckable, propertyPath, value) -> {
        if (getString(complexCheckable, propertyPath, value).isEmpty()) {
            throw new PropertyCheckException(complexCheckable, propertyPath, "is an empty string");
        }
    }),
    TRUE((complexCheckable, propertyPath, value) -> checkBoolean(complexCheckable, propertyPath, value, true)),
    FALSE((complexCheckable, propertyPath, value) -> checkBoolean(complexCheckable, propertyPath, value, false));

    private final CheckTypeHandler handler;

    CheckType(CheckTypeHandler handler) {
        this.handler = handler;
    }

    @Override
    public void check(ComplexCheckable complexCheckable,
                      PropertyPath propertyPath, Object value) throws CheckException {
        handler.check(complexCheckable, propertyPath, value);
    }

    private static int getInteger(ComplexCheckable complexCheckable,
                                  PropertyPath propertyPath, Object value) throws CheckException {
        if (value instanceof Integer) {
            return (int) value;
        }
        throw new PropertyCheckException(complexCheckable, propertyPath,
                "is not an integer");
    }

    @SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
    private static boolean getBoolean(ComplexCheckable complexCheckable,
                                      PropertyPath propertyPath, Object value) throws CheckException {
        if (value instanceof Boolean) {
            return (boolean) value;
        }
        throw new PropertyCheckException(complexCheckable, propertyPath,
                "is not a boolean");
    }

    private static String getString(ComplexCheckable complexCheckable,
                                    PropertyPath propertyPath, Object value) throws CheckException {
        if (value instanceof String) {
            return (String) value;
        }
        throw new PropertyCheckException(complexCheckable, propertyPath,
                "is not a string");
    }

    private static ComplexCheckable getComplexCheckable(ComplexCheckable complexCheckable,
                                                        PropertyPath propertyPath, Object value) throws CheckException {
        if ((value == null) || (value instanceof ComplexCheckable)) {
            return (ComplexCheckable) value;
        }
        throw new PropertyCheckException(complexCheckable, propertyPath,
                "is not null and isn't inherited from 'ComplexCheckable'");
    }

    private static void checkBoolean(ComplexCheckable complexCheckable,
                                     PropertyPath propertyPath, Object value, boolean expected) throws CheckException {
        boolean booleanValue = getBoolean(complexCheckable, propertyPath, value);
        if (booleanValue != expected) {
            throw new PropertyCheckException(complexCheckable, propertyPath,
                    String.format("isn't %s", expected));
        }
    }
}