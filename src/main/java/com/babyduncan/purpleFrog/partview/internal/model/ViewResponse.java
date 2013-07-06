package com.babyduncan.purpleFrog.partview.internal.model;


public abstract class ViewResponse {

    protected int status;

    protected String statusText;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public abstract String toJson();
}
