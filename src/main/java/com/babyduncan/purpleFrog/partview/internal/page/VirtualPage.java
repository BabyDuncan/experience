package com.babyduncan.purpleFrog.partview.internal.page;

import com.babyduncan.purpleFrog.partview.internal.Constants;
import com.babyduncan.purpleFrog.partview.internal.anno.PartialViewHandler;
import com.babyduncan.purpleFrog.partview.internal.anno.PartialViewMapping;
import com.babyduncan.purpleFrog.partview.internal.tag.FragTag;
import com.babyduncan.purpleFrog.partview.internal.tag.FragType;
import com.babyduncan.purpleFrog.partview.util.PartViewUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.*;

/**
 * the main code of Partview .
 * one VirtualPage is maked up by many PartView.
 * one PartView must have only one parent,but one parent could have many childen.
 */
public class VirtualPage {

    private static final Logger logger = LoggerFactory.getLogger(VirtualPage.class);

    private static final String PARTVIEW_JSP_KEY_EMBED = "_embed";
    // name -> partview
    private final Map<String, PartialView> partViews = new HashMap<String, PartialView>();
    // name -> partviewpath
    private final Map<String, PartialViewPath> partViewPath = new HashMap<String, PartialViewPath>();
    // the name of this virtual page
    private final String name;
    // <jsp:include -> path
    private final String virtualIncludeRoot;

    public VirtualPage(String name, String virtualIncludeRoot) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("The page name must not be null or empty.");
        }
        if (virtualIncludeRoot == null) {
            throw new IllegalArgumentException("The virtualIncludeRoot must be set.");
        }
        this.name = name;
        this.virtualIncludeRoot = virtualIncludeRoot;
    }

    /**
     * @param partViewConfig it is annotated by PartialViewHandler
     */
    public void addPartVeiw(final Object partViewConfig) {
        if (partViewConfig.getClass().getAnnotation(PartialViewHandler.class) == null) {
            throw new IllegalArgumentException("The class [" + partViewConfig.getClass() + "] is not annotated with [ " + PartialViewHandler.class.getName() + "]");
        }
        final Method[] methods = partViewConfig.getClass().getDeclaredMethods();
        for (Method method : methods) {
            PartialViewMapping mapping = method.getAnnotation(PartialViewMapping.class);
            if (mapping == null) {
                continue;
            }
            method.setAccessible(true);
            String name = mapping.value();
            if (this.partViews.containsKey(name)) {
                throw new RuntimeException("The partial view name [" + name + "] conflict with the the same name part view.");
            }
            PartialView partView = new PartialView();
            partView.setHandlerRef(partViewConfig);
            partView.setName(name);
            partView.setDefaultChildName(mapping.defaultChild());
            partView.setParentName(mapping.parent());
            partView.setHandler(method);
            partView.setParameters(mapping.param());
            this.partViews.put(partView.getName(), partView);
        }
    }

    /**
     * you must first add one partview ,then build partviewpath.
     */
    public void buildPartViewPath() {
        final Set<Map.Entry<String, PartialView>> viewSet = this.partViews.entrySet();
        for (Map.Entry<String, PartialView> entry : viewSet) {
            final String viewName = entry.getKey();
            final PartialView view = entry.getValue();
            final PartialViewPath path = new PartialViewPath(view);
            Set<PartialView> visitor = new HashSet<PartialView>();
            List<PartialView> parents = new ArrayList<PartialView>();
            String parent = null;
            PartialView parentView = view;
            while ((parent = parentView.getParentName()) != null) {
                if (parent.isEmpty()) {
                    break;
                }
                parentView = this.partViews.get(parent);
                if (visitor.contains(parentView)) {
                    throw new RuntimeException("The PartView[" + parentView.getName() + "] has been visited,may be wrong config for partview? ");
                } else {
                    visitor.add(parentView);
                }
                parents.add(parentView);
            }
            for (int i = parents.size() - 1; i >= 0; i--) {
                path.addPartialViewNode(parents.get(i));
            }
            if (visitor.contains(view)) {
                throw new RuntimeException("The PartView[" + view.getName() + "] has been visited,may be wrong config for partview? ");
            } else {
                visitor.add(view);
            }
            path.addPartialViewNode(view);
            String defaultChild = null;
            PartialView childView = view;
            while ((defaultChild = childView.getDefaultChildName()) != null) {
                if (defaultChild.isEmpty()) {
                    break;
                }
                childView = this.partViews.get(defaultChild);
                if (childView == null) {
                    throw new RuntimeException("Can't find the child node [" + defaultChild + "]");
                }
                if (visitor.contains(childView)) {
                    throw new RuntimeException("The PartView[" + childView.getName() + "] has been visited,may be wrong config for partview? ");
                } else {
                    visitor.add(childView);
                }
                path.addPartialViewNode(childView);
            }
            path.buildPath();
            partViewPath.put(viewName, path);
            System.out.println("partviewPath is " + partViewPath);
        }
    }

    /**
     * hanlde
     *
     * @param partViewName
     * @param modelAndView
     * @param request
     * @param response
     */
    public void handle(final String partViewName, final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) {
        final PartialView view = this.partViews.get(partViewName);
        try {
            view.getHandler().invoke(view.getHandlerRef(), modelAndView, request, response);
        } catch (Exception e) {
            logger.error("exceptionHandler=" + view.getHandlerRef().getClass().getName() + "." + view.getHandler().getName(), e);
        }
    }

    public String handleWithContext(final String partViewName, final String context, final ModelAndView modelAndView, final HttpServletRequest request, final HttpServletResponse response) {
        final PartialViewPath targetViewPath = this.partViewPath.get(partViewName);
        if (targetViewPath == null) {
            return null;
        }
        final List<PartialView> viewPathPart = targetViewPath.getFullPart();
        if (context != null && !context.isEmpty()) {
            request.setAttribute(FragTag.ATTR_REQ_FORMAT, FragType.JSON);
        }
        //get serverContexts
        final PartViewUtil.PartViewContexts serverContexts = PartViewUtil.buildPartViewContextFromServer(this, partViewName, request);
        //currentContexts is serverContexts
        request.setAttribute(Constants.ATTR_REQ_CURRENT_CONTEXT, serverContexts);
        int diffIndex = -1;
        {
            //PartViewUtil.PartViewContexts clientContexts = PartViewUtil.buildPartViewContextFromClient(this, context);
            //get clientContexts
            PartViewUtil.PartViewContexts clientContexts = PartViewUtil.buildPartViewContextFromClientLine(this, context);
            //get the different index
            diffIndex = PartViewUtil.diff(serverContexts, clientContexts);
        }
        final StringBuilder viewName = new StringBuilder();
        for (int i = 0; i < diffIndex; i++) {
            if (i > 0) {
                viewName.append("/");
            }
            viewName.append(viewPathPart.get(i).getName());
        }

        final StringBuilder embedName = new StringBuilder(viewName);
        for (int i = diffIndex; i < viewPathPart.size(); i++) {
            final String curPartViewName = viewPathPart.get(i).getName();
            this.handle(curPartViewName, modelAndView, request, response);
            if (i < viewPathPart.size() - 1) {
                embedName.append("/");
                embedName.append(curPartViewName);
                String embedKey = curPartViewName + PARTVIEW_JSP_KEY_EMBED;
                final String childPartViewName = viewPathPart.get(i + 1).getName();
                String embedValue = this.virtualIncludeRoot + "/" + embedName.toString() + "/" + childPartViewName + "/_" + childPartViewName;
                modelAndView.addObject(embedKey, embedValue);
            }
        }

        String[] clientContexts = PartViewUtil.toLine(serverContexts, "\n", "\\n");
        request.setAttribute(Constants.ATTR_REQ_PARTVIEW_CONTEXT, clientContexts[0]);
        request.setAttribute(Constants.ATTR_REQ_PARTVIEW_CONTEXT_PREVIOUS, context);
        modelAndView.addObject("frontContext", clientContexts[1]);
        String diffPartViewName = viewPathPart.get(diffIndex).getName();
        String targetViewContext = viewName.toString() + "/" + diffPartViewName;
        String viewPath = targetViewContext + "/_" + diffPartViewName;
        return viewPath;
    }

    public String getCurrentContext(HttpServletRequest request) {
        return (String) request.getAttribute(Constants.ATTR_REQ_PARTVIEW_CONTEXT);
    }

    public String getJSVarCurrentContext(HttpServletRequest request) {
        return (String) request.getAttribute(Constants.ATTR_REQ_PARTVIEW_CONTEXT_JS);
    }

    public String getName() {
        return name;
    }

    public String getVirtualIncludeRoot() {
        return virtualIncludeRoot;
    }

    public Map<String, PartialView> getPartViews() {
        return Collections.unmodifiableMap(partViews);
    }

    public Map<String, PartialViewPath> getPartViewPath() {
        return Collections.unmodifiableMap(partViewPath);
    }

    public PartialViewPath getPartialViewPathByName(String partialViewName) {
        return partViewPath.get(partialViewName);
    }

    @Override
    public String toString() {
        return "VirtualPage{" +
                "virtualIncludeRoot='" + virtualIncludeRoot + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

