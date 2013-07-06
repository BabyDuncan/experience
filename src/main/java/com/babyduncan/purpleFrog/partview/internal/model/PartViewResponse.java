package com.babyduncan.purpleFrog.partview.internal.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


public class PartViewResponse extends ViewResponse {

    private final static Logger logger = LoggerFactory.getLogger(PartViewResponse.class);

    //resources list size when inited
    private static final int RES_LIST_INIT_SIZE = 3;
    //page title
    private String title;
    //page body
    private String body;

    private String context;

    private String previousContext;

    private String redirect;

    private List<LoadResource> beforeLoad = new ArrayList<LoadResource>(RES_LIST_INIT_SIZE);

    private List<LoadResource> afterLoad = new ArrayList<LoadResource>(RES_LIST_INIT_SIZE);

    private String additionalAttributes;

    public PartViewResponse() {

    }

    public void addResource(final LoadPoint loadPoint, final LoadResourceType resourceType, final String src, final String text) {
        if (loadPoint == null) {
            throw new IllegalArgumentException("The loadPoint must be set.");
        }
        if (resourceType == null) {
            throw new IllegalArgumentException("The resourceType must be set.");
        }
        if (src == null && text == null) {
            throw new IllegalArgumentException("The src or text must be set.");
        }
        LoadResource res = new LoadResource(resourceType, src != null ? src : "", text != null ? text : "");
        if (loadPoint == LoadPoint.BEFORE_LOAD) {
            this.beforeLoad.add(res);
        } else if (loadPoint == LoadPoint.AFTER_LOAD) {
            this.afterLoad.add(res);
        } else {
            throw new RuntimeException("Unknown loadPoint type [" + loadPoint + "]");
        }
    }

    public String toJson() {
        JSONObject json = new JSONObject();
        try {
            // put status
            json.put("status", this.status);
            json.put("statusText", this.statusText);
            JSONObject data = new JSONObject();
            {
                //put base data
                data.put("title", this.title);
                data.put("body", this.body);
                data.put("context", this.context);
                data.put("previousContext", this.previousContext);
                data.put("redirect", this.redirect);

                data.put("beforeload", this.buildLoadResources(this.beforeLoad));
                data.put("onload", this.buildLoadResources(this.afterLoad));
                json.put("data", data);
                if (this.additionalAttributes != null && !"".equals(this.additionalAttributes)) {
                    json.put("addAttr", this.additionalAttributes);
                }
            }
        } catch (JSONException e) {
            if (logger.isErrorEnabled()) {
                logger.error("PartView:JSON", e);
            }
            return "{status:503,statusText:'Service Unavailable'}";
        }
        return json.toString();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getPreviousContext() {
        return previousContext;
    }

    public void setPreviousContext(String previousContext) {
        this.previousContext = previousContext;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public List<LoadResource> getBeforeLoad() {
        return beforeLoad;
    }

    public List<LoadResource> getAfterLoad() {
        return afterLoad;
    }

    public String getAdditionalAttribute() {
        return additionalAttributes;
    }

    public void setAdditionalAttribute(String additionalAttributes) {
        this.additionalAttributes = additionalAttributes;
    }

    private JSONArray buildLoadResources(final List<LoadResource> resources) throws JSONException {
        final JSONArray resArray = new JSONArray();
        for (LoadResource res : resources) {
            JSONObject resJson = new JSONObject();
            resJson.put("type", res.getType().getName());
            resJson.put("src", res.getSrc());
            resJson.put("text", res.getText());
            resArray.put(resJson);
        }
        return resArray;
    }
}
