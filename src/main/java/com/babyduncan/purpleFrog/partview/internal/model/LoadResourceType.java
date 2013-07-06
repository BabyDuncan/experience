package com.babyduncan.purpleFrog.partview.internal.model;

public enum LoadResourceType {
    JavaScript("javascript"), CSS("csss");

    private final String name;

    LoadResourceType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
