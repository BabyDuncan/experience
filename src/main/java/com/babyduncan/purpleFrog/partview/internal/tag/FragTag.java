package com.babyduncan.purpleFrog.partview.internal.tag;

import com.babyduncan.purpleFrog.partview.internal.model.PartViewResponse;

import javax.servlet.ServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyContent;
import java.io.IOException;

public class FragTag extends BaseFragTag {

    private static final String PARTVIEW_RESPONSE_TYPE = "application/x-javascript; charset=GBK";

    private static final String DEFAULT_CHARSET = "GBK";

    private String title;

    private String additionalAttribute;

    public FragTag() {
        this.init();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdditionalAttribute() {
        return additionalAttribute;
    }

    public void setAdditionalAttribute(String additionalAttribute) {
        this.additionalAttribute = additionalAttribute;
    }

    @Override
    public int doStartTag() throws JspException {
        if (super.isJsonStyle()) {
            super.getPartViewResponse();
            Integer stack = super.getStackCount();
            stack += 1;
            super.setStackCount(stack);
            return EVAL_BODY_BUFFERED;
        } else {
            return EVAL_BODY_INCLUDE;
        }
    }

    @Override
    public int doEndTag() throws JspException {
        if (!super.isJsonStyle()) {
            return EVAL_PAGE;
        }

        BodyContent body = this.getBodyContent();
        Integer stack = super.getStackCount();
        stack = stack - 1;
        if (stack.intValue() > 0) {
            super.setStackCount(stack);
            try {
                body.getEnclosingWriter().write(body.getString());
                body.clear();
            } catch (IOException e) {
                throw new JspException("Can't wirte the buffered data", e);
            }
        } else {
            final PartViewResponse partViewResponse = super.getPartViewResponse();
            partViewResponse.setTitle(this.title);
            if (body != null) {
                try {
                    partViewResponse.setBody(body.getString());
                    partViewResponse.setContext(super.getContextName());
                    partViewResponse.setPreviousContext(super.getPreviousContextName());
                    partViewResponse.setAdditionalAttribute(additionalAttribute);
                } finally {
                    try {
                        body.clear();
                    } catch (IOException e) {
                        throw new JspException("Can't clear the body", e);
                    }
                }
                try {
                    final ServletResponse response = this.pageContext.getResponse();
                    response.setContentType(PARTVIEW_RESPONSE_TYPE);
                    response.setCharacterEncoding(DEFAULT_CHARSET);
                    body.getEnclosingWriter().write(partViewResponse.toJson());
                } catch (IOException e) {
                    throw new JspException("Can't wirte the buffered data", e);
                }
            }
        }
        return EVAL_PAGE;
    }

    @Override
    public void release() {
        super.release();
        this.init();
    }

    private void init() {
        this.title = "";
    }
}
