package com.babyduncan.purpleFrog.partview.internal.model;

/**
 * the type of the resources
 */
public enum LoadPoint {
    //the resources should be loaded before the view show
    BEFORE_LOAD("before"),
    //the resources could be loaded after the view show
    AFTER_LOAD("after");

    private final String type;

    LoadPoint(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static LoadPoint getByType(final String type) {
        if (BEFORE_LOAD.getType().equals(type)) {
            return BEFORE_LOAD;
        } else if (AFTER_LOAD.getType().equals(type)) {
            return AFTER_LOAD;
        } else {
            throw new RuntimeException("Unknown load point type [" + type + "]");
        }
    }
}
