package com.mz.json.validator;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class PrintableObject {
    @Override
    public String toString() {
        Map<String, Object> map = new HashMap<>();

        Class<?> currentClass = getClass();
        while ((currentClass != null) && (currentClass != Object.class)) {
            for (Field field : currentClass.getDeclaredFields()) {
                if (Modifier.isTransient(field.getModifiers()) || field.getName().equals("this$0")) {
                    continue;
                }

                Object object = getValue(field);
                String value = (object != null) ? object.toString() : "null";

                SerializedName annotation = field.getAnnotation(SerializedName.class);
                String key = (annotation != null) ? annotation.value() : field.getName();

                map.put(key, value);
            }
            currentClass = currentClass.getSuperclass();
        }
        return map.toString();
    }

    Object getValue(Field field) {
        if (field.isAccessible()) {
            try {
                return field.get(this);
            } catch (IllegalAccessException ignored) {
                return "[!illegal-access-exception!]";
            }
        } else {

            field.setAccessible(true);
            try {
                return field.get(this);
            } catch (IllegalAccessException ignored) {
                return "[!illegal-access-exception!]";
            } finally {
                field.setAccessible(false);
            }
        }
    }
}