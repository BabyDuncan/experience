package com.babyduncan.purpleFrog.partview.internal.tag;


import com.babyduncan.purpleFrog.partview.internal.Constants;
import com.babyduncan.purpleFrog.partview.internal.model.PartViewResponse;

import javax.servlet.jsp.tagext.BodyTagSupport;

public abstract class BaseFragTag extends BodyTagSupport {

    public static final String ATTR_REQ_FORMAT = BaseFragTag.class.getName() + ".Froamt";

    public static final String ATTR_REQ_PARTVIEW_RESPONSE = BaseFragTag.class.getName() + ".JsonEntity";

    private static final String ATTR_REQ_STACK = BaseFragTag.class.getName() + ".stack";

    protected boolean isJsonStyle() {
        return FragType.JSON == this.pageContext.getRequest().getAttribute(ATTR_REQ_FORMAT);
    }

    protected PartViewResponse getPartViewResponse() {
        PartViewResponse response = (PartViewResponse) this.pageContext.getRequest().getAttribute(ATTR_REQ_PARTVIEW_RESPONSE);
        if (response == null) {
            response = new PartViewResponse();
            response.setStatus(Constants.SUCCESS_STATUS);
            this.pageContext.getRequest().setAttribute(ATTR_REQ_PARTVIEW_RESPONSE, response);
        }
        return response;
    }

    protected String getContextName() {
        String name = (String) this.pageContext.getRequest().getAttribute(Constants.ATTR_REQ_PARTVIEW_CONTEXT);
        return (name == null ? "" : name);
    }

    protected String getPreviousContextName() {
        String name = (String) this.pageContext.getRequest().getAttribute(Constants.ATTR_REQ_PARTVIEW_CONTEXT_PREVIOUS);
        return (name == null ? "" : name);
    }


    protected Integer getStackCount() {
        Integer count = (Integer) this.pageContext.getRequest().getAttribute(ATTR_REQ_STACK);
        if (count == null) {
            count = 0;
            this.pageContext.getRequest().setAttribute(ATTR_REQ_STACK, count);
        }
        return count;
    }

    protected void setStackCount(Integer count) {
        this.pageContext.getRequest().setAttribute(ATTR_REQ_STACK, count);
    }
}
