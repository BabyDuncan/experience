package com.babyduncan.purpleFrog.partview.internal.model;

/**
 * for resources load
 */
public class LoadResource {

    //resource type
    private final LoadResourceType type;
    //resource distination
    private final String src;
    //resource text
    private final String text;

    public LoadResource(LoadResourceType type, String src, String text) {
        this.type = type;
        this.src = src;
        this.text = text;
    }

    public LoadResourceType getType() {
        return type;
    }

    public String getSrc() {
        return src;
    }

    public String getText() {
        return text;
    }
}
