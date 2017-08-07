package com.mz.json.validator;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public final class StringRepresentation {

    public static String toString(Object obj) {
        Map<String, Object> map = new HashMap<>();

        Class<?> currentClass = obj.getClass();
        while ((currentClass != null) && (currentClass != Object.class)) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (Modifier.isTransient(field.getModifiers())
                        || Modifier.isStatic(field.getModifiers())
                        || field.getName().equals("this$0")) {
                    continue;
                }

                Object object = getValue(obj, field);
                String value = (object != null) ? object.toString() : "null";

                SerializedName annotation = field.getAnnotation(SerializedName.class);
                String key = (annotation != null) ? annotation.value() : field.getName();

                map.put(key, value);
            }
            currentClass = currentClass.getSuperclass();
        }
        return map.toString();
    }

    static Object getValue(Object obj, Field field) {
        if (field.isAccessible()) {
            try {
                return field.get(obj);
            } catch (IllegalAccessException ignored) {
                return "[!illegal-access-exception!]";
            }
        } else {

            field.setAccessible(true);
            try {
                return field.get(obj);
            } catch (IllegalAccessException ignored) {
                return "[!illegal-access-exception!]";
            } finally {
                field.setAccessible(false);
            }
        }
    }
}
