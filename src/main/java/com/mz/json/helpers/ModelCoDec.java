package com.mz.json.helpers;

import com.google.gson.GsonBuilder;
import com.mz.json.validator.CheckException;
import com.mz.json.validator.SimpleCheckable;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("WeakerAccess")
public final class ModelCoDec {
    private static final AtomicBoolean VALIDATE_MODELS = new AtomicBoolean(true);

    @SuppressWarnings("ConstantNamingConvention")
    private static final ThreadLocalGson GSON = new ThreadLocalGson(new GsonBuilder().disableHtmlEscaping());

    public static <T extends SimpleCheckable> T decodeModel(String content, Type type) throws CheckException {
        T model = GSON.get().fromJson(content, type);
        if (model == null) {
            if (content.isEmpty()) {
                content = "[!empty!]";
            }
            throw new CheckException(String.format("Can't decode %s from JSON: %s", type, content));
        }
        check(model);
        return model;
    }

    public static String encodeModel(SimpleCheckable model) throws CheckException {
        check(model);
        return GSON.get().toJson(model);
    }

    @SuppressWarnings("BooleanParameter")
    public static void setValidateModels(boolean validateModels) {
        VALIDATE_MODELS.set(validateModels);
    }

    private static void check(SimpleCheckable model) throws CheckException {
        if(VALIDATE_MODELS.get()) {
            model.check();
        }
    }
}