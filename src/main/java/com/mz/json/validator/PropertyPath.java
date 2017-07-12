package com.mz.json.validator;

import java.util.ArrayList;
import java.util.List;

public final class PropertyPath {
    private static final PropertyPath EMPTY_PROPERTY_PATH = new PropertyPath();

    private final List<String> pathItems;

    private PropertyPath() {
        pathItems = new ArrayList<>();
    }

    private PropertyPath(PropertyPath path, String pathItem) {
        //noinspection AccessingNonPublicFieldOfAnotherObject
        pathItems = new ArrayList<>(path.pathItems);
        pathItems.add(pathItem);
    }

    @SuppressWarnings("WeakerAccess")
    public PropertyPath getAppendedInstance(String pathItem) {
        return new PropertyPath(this, pathItem);
    }

    /*
        We could get rid of this method in favor of simple usage of EMPTY_PROPERTY_PATH.
        But it may be much more effective to return a new copy of EMPTY_PROPERTY_PATH
        each time on this call if we work with long paths.
        We save this method for the future compatibility with such optimization.
     */
    public static PropertyPath getEmptyPropertyPath() {
        return EMPTY_PROPERTY_PATH;
    }

    @Override
    public String toString() {
        return String.format("Property '%s'", pathItems.isEmpty()
                ? "[!empty!]"
                :String.join(".", pathItems));
    }
}