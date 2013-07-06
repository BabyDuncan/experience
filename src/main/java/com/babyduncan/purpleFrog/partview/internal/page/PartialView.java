package com.babyduncan.purpleFrog.partview.internal.page;

import com.babyduncan.purpleFrog.partview.internal.anno.PartialViewParameter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * one partview means one piece of HTML , some partview could construct to a page . like a tree .
 */
public class PartialView {

    //the name of this partview
    private String name;
    //the parent name of this partview
    private String parentName;
    //default child of this partview
    private String defaultChildName;
    //partview handler
    private Method handler;

    private Object handlerRef;
    //partview paramters
    private PartialViewParameter[] parameters;
    //all the paramters
    private final Set<String> parameterNames = new HashSet<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentName() {
        return parentName;
    }

    protected void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getDefaultChildName() {
        return defaultChildName;
    }

    protected void setDefaultChildName(String defaultChildName) {
        this.defaultChildName = defaultChildName;
    }

    public Method getHandler() {
        return handler;
    }

    protected void setHandler(Method handler) {
        this.handler = handler;
    }

    public Object getHandlerRef() {
        return handlerRef;
    }

    protected void setHandlerRef(Object handlerRef) {
        this.handlerRef = handlerRef;
    }

    public PartialViewParameter[] getParameters() {
        return parameters;
    }

    protected void setParameters(PartialViewParameter[] parameters) {
        this.parameters = parameters;
        this.parameterNames.clear();
        for (PartialViewParameter param : this.parameters) {
            if (this.parameterNames.contains(param.name())) {
                throw new RuntimeException("Dup parameter name [" + param.name() + "]");
            }
            this.parameterNames.add(param.name());
        }
    }

    public boolean isParameterExist(String name) {
        return this.parameterNames.contains(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartialView that = (PartialView) o;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PartialView{" +
                "name='" + name + '\'' +
                ", parentName='" + parentName + '\'' +
                ", defaultChildName='" + defaultChildName + '\'' +
                ", handler=" + handler +
                ", handlerRef=" + handlerRef +
                ", parameters=" + (parameters == null ? null : Arrays.asList(parameters)) +
                ", parameterNames=" + parameterNames +
                '}';
    }
}
