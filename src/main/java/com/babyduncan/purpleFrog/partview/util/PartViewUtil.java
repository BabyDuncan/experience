package com.babyduncan.purpleFrog.partview.util;

import com.babyduncan.purpleFrog.partview.internal.anno.PartialViewParameter;
import com.babyduncan.purpleFrog.partview.internal.page.PartialView;
import com.babyduncan.purpleFrog.partview.internal.page.PartialViewPath;
import com.babyduncan.purpleFrog.partview.internal.page.VirtualPage;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class PartViewUtil {

    private static final Logger logger = LoggerFactory.getLogger(PartViewUtil.class);

    public static PartViewContexts buildPartViewContextFromServer(final VirtualPage page, final String partViewName, final HttpServletRequest request) {
        final PartialViewPath viewPath = page.getPartialViewPathByName(partViewName);
        PartViewContexts contexts = new PartViewContexts(viewPath, new IPartViewParameterFetcher() {
            @Override
            public String getValue(String paraname) {
                return request.getParameter(paraname);
            }
        });
        return contexts;
    }

    public static PartViewContexts buildPartViewContextFromClient(final VirtualPage page, final String clientContext) {
        if (clientContext == null || clientContext.isEmpty()) {
            return new PartViewContexts(null, (ContextParameter[]) null);
        }
        try {
            JSONArray array = new JSONArray(clientContext);
            final int length = array.length();
            final JSONArray leafArray = array.getJSONArray(length - 1);
            final String viewName = leafArray.getString(0);
            final PartialViewPath serverPath = page.getPartialViewPathByName(viewName);
            if (serverPath == null || serverPath.getSize() < length) {
                if (logger.isWarnEnabled()) {
                    logger.warn("PART.CHECK.FAIL [" + clientContext + "]");
                }
                return null;
            }
            final PartialViewPath clientPath = new PartialViewPath(null);
            final ContextParameter[] parameters = new ContextParameter[length];
            for (int i = 0; i < length; i++) {
                JSONArray node = array.getJSONArray(i);
                String nodeViewName = node.getString(0);
                PartialView serverView = serverPath.get(i);
                if (!serverView.getName().equals(nodeViewName)) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("PART.CHECK.FAIL [" + clientContext + "]");
                    }
                    return null;
                }
                clientPath.addPartialViewNode(serverView);
                JSONArray nodeParams = node.getJSONArray(1);
                if (nodeParams.length() == 0) {
                    continue;
                }
                parameters[i] = new ContextParameter();
                for (int pi = 0; pi < nodeParams.length(); pi++) {
                    JSONArray pa = nodeParams.getJSONArray(pi);
                    String pname = pa.getString(0);
                    String pvalue = pa.getString(1);
                    if (pname == null || pname.isEmpty()) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("PART.CHECK.FAIL [" + clientContext + "]");
                        }
                        return null;
                    }
                    if (!serverView.isParameterExist(pname)) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("PART.CHECK.FAIL [" + serverView.getName() + "],parameter name [" + pname + "]not exist ");
                        }
                        return null;
                    }
                    parameters[i].params.put(pname, pvalue != null ? pvalue : "");
                }
            }
            return new PartViewContexts(clientPath, parameters);
        } catch (JSONException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Can't  build the context from json[" + clientContext + "]", e);
            }
            return null;
        }
    }

    public static String toJson(PartViewContexts contexts) {
        JSONArray array = new JSONArray();
        final List<PartialView> fullPart = contexts.viewPath.getFullPart();
        final ContextParameter[] parameters = contexts.parameters;
        for (int i = 0; i < fullPart.size(); i++) {
            JSONArray viewJson = new JSONArray();
            array.put(viewJson);
            PartialView view = fullPart.get(i);
            JSONArray param = new JSONArray();
            viewJson.put(view.getName());
            viewJson.put(param);
            ContextParameter parameter = parameters[i];
            if (parameter == null) {
                continue;
            }
            Map<String, String> params = parameter.params;
            if (params.isEmpty()) {
                continue;
            }
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String paramName = entry.getKey();
                String paramValue = entry.getValue();
                JSONArray p = new JSONArray();
                p.put(paramName);
                p.put(paramValue);
                param.put(p);
            }
        }
        return array.toString();
    }

    public static PartViewContexts buildPartViewContextFromClientLine(final VirtualPage page, final String clientContext) {
        List<ClientContextData> clientContextDatas = parseClientContextData(clientContext);
        final int length = clientContextDatas.size();
        if (length == 0) {
            return new PartViewContexts(null, (ContextParameter[]) null);
        }
        final PartialViewPath clientPath = new PartialViewPath(null);
        final ContextParameter[] parameters = new ContextParameter[length];
        for (int i = 0; i < length; i++) {
            ClientContextData node = clientContextDatas.get(i);
            PartialView nodeView = new PartialView();
            nodeView.setName(node.name);
            clientPath.addPartialViewNode(nodeView);
            List<String[]> nodeParams = node.params;
            if (nodeParams.size() == 0) {
                continue;
            }
            parameters[i] = new ContextParameter();
            for (int pi = 0; pi < nodeParams.size(); pi++) {
                String[] pa = nodeParams.get(pi);
                String pname = pa[0];
                String pvalue = pa[1];
                if (pname == null || pname.isEmpty()) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("PART.CHECK.FAIL [" + clientContext + "] pname is null");
                    }
                    return null;
                }
                parameters[i].params.put(pname, pvalue != null ? pvalue : "");
            }
        }
        return new PartViewContexts(clientPath, parameters);
    }

    public static String[] toLine(PartViewContexts contexts, String... lines) {
        final StringBuilder[] sbs = new StringBuilder[lines.length];
        for (int i = 0; i < sbs.length; i++) {
            sbs[i] = new StringBuilder(64);
        }
        final List<PartialView> fullPart = contexts.viewPath.getFullPart();
        final ContextParameter[] parameters = contexts.parameters;
        for (int i = 0; i < fullPart.size(); i++) {
            PartialView view = fullPart.get(i);
            addStr(sbs, view.getName());
            addLine(sbs, lines);
            try {
                ContextParameter parameter = parameters[i];
                if (parameter == null) {
                    continue;
                }
                Map<String, String> params = parameter.params;
                if (params.isEmpty()) {
                    continue;
                }
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String paramName = entry.getKey();
                    String paramValue = entry.getValue();
                    addStr(sbs, paramName);
                    addStr(sbs, "=");
                    addStr(sbs, paramValue);
                    addLine(sbs, lines);
                }
            } finally {
                if (i + 1 < fullPart.size()) {
                    addLine(sbs, lines);
                }
            }
        }
        String[] result = new String[sbs.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = sbs[i].toString();
        }
        return result;
    }

    public static int diff(final PartViewContexts serverContexts, final PartViewContexts clientContexts) {
        if (serverContexts == null) {
            throw new IllegalArgumentException("The serverContexts must not be null");
        }
        if (clientContexts == null || clientContexts.viewPath == null || clientContexts.parameters == null) {
            return 0;
        }
        List<PartialView> serverPart = serverContexts.viewPath.getFullPart();
        List<PartialView> clientPart = clientContexts.viewPath.getFullPart();
        int diffIndex = -1;
        int lastMatch = -1;
        final int clientSize = clientPart.size();
        final int serverSize = serverPart.size();
        for (int i = 0; i < clientSize && i < serverSize; i++) {
            final PartialView clientView = clientPart.get(i);
            final PartialView serverView = serverPart.get(i);
            if (!clientView.getName().equals(serverView.getName())) {
                diffIndex = i;
                break;
            } else {
                ContextParameter serverViewParam = serverContexts.parameters[i];
                ContextParameter clientViewParam = clientContexts.parameters[i];
                if (serverViewParam != null) {
                    if (clientViewParam != null) {
                        if (!(serverViewParam.params.equals(clientViewParam.params))) {
                            diffIndex = i;
                            break;
                        }
                    }
                }
                lastMatch = i;
            }
        }
        if (diffIndex > -1) {
            return diffIndex;
        }
        if (lastMatch > -1) {
            return lastMatch;
        }
        return 0;
    }

    static List<ClientContextData> parseClientContextData(String clientContext) {
        ArrayList<ClientContextData> cdatas = new ArrayList<ClientContextData>();
        if (clientContext == null || clientContext.isEmpty()) {
            return cdatas;
        }
        final StringBuilder curContext = new StringBuilder();
        final StringBuilder curParName = new StringBuilder();
        final StringBuilder curParValue = new StringBuilder();
        final int len = clientContext.length();
        for (int i = 0; i < len; i++) {
            ClientContextData cdata = new ClientContextData();
            String context = null;
            while (i < len) {
                char c = clientContext.charAt(i);
                i++;
                if (c != '\n') {
                    curContext.append(c);
                } else {
                    context = curContext.toString();
                    curContext.setLength(0);
                    break;
                }
            }
            if (context == null || context.isEmpty()) {
                return cdatas;
            }
            cdata.name = context;
            if (i < len) {
                if (clientContext.charAt(i) == '\n') {
                    cdatas.add(cdata);
                    continue;
                }
            }
            cdatas.add(cdata);
            while (i < len) {
                String pName = null;
                String pValue = "";
                while (i < len) {
                    final char c = clientContext.charAt(i);
                    i++;
                    if (c != '=') {
                        curParName.append(c);
                    } else {
                        pName = curParName.toString();
                        break;
                    }
                }
                while (i < len) {
                    final char c = clientContext.charAt(i);
                    i++;
                    if (c != '\n') {
                        curParValue.append(c);
                    } else {
                        pValue = curParValue.toString();
                        break;
                    }
                }
                if (pName == null || pValue == null) {
                    return cdatas;
                }
                String[] params = new String[2];
                params[0] = pName;
                params[1] = pValue;
                curParName.setLength(0);
                curParValue.setLength(0);
                cdata.params.add(params);
                if (i < len) {
                    if (clientContext.charAt(i) == '\n') {
                        break;
                    }
                }
            }
        }
        return cdatas;
    }

    public static interface IPartViewParameterFetcher {
        public String getValue(String paraname);
    }

    public static class ContextParameter {
        private final Map<String, String> params = new LinkedHashMap<String, String>();
    }

    public static class PartViewContexts {
        private final PartialViewPath viewPath;
        private final ContextParameter[] parameters;

        public PartViewContexts(final PartialViewPath viewPath, final IPartViewParameterFetcher fetcher) {
            this.viewPath = viewPath;
            this.parameters = new ContextParameter[this.viewPath.getSize()];
            for (int i = 0; i < this.viewPath.getSize(); i++) {
                final PartialView partialView = this.viewPath.get(i);
                final PartialViewParameter[] configParams = partialView.getParameters();
                if (configParams == null || configParams.length == 0) {
                    continue;
                }
                this.parameters[i] = new ContextParameter();
                fetchParameter(this.parameters[i], fetcher, configParams);
            }
        }

        public PartViewContexts(final PartialViewPath viewPath, final ContextParameter[] parameters) {
            this.viewPath = viewPath;
            this.parameters = parameters;
        }

        public PartialViewPath getViewPath() {
            return viewPath;
        }

        private void fetchParameter(ContextParameter context, IPartViewParameterFetcher fetcher, PartialViewParameter[] parameters) {
            for (PartialViewParameter parm : parameters) {
                String name = parm.name();
                if (name == null || name.isEmpty()) {
                    continue;
                }
                String value = fetcher.getValue(name);
                context.params.put(name, value != null ? value : "");
            }
        }
    }

    private static void addStr(StringBuilder[] sbs, String str) {
        for (int si = 0; si < sbs.length; si++) {
            sbs[si].append(str);
        }
    }

    private static void addStr(StringBuilder[] sbs, char c) {
        for (int si = 0; si < sbs.length; si++) {
            sbs[si].append(c);
        }
    }

    private static void addLine(StringBuilder[] sbs, String[] lines) {
        for (int si = 0; si < sbs.length; si++) {
            sbs[si].append(lines[si]);
        }
    }

    static class ClientContextData {
        public String name;
        public List<String[]> params = new ArrayList<String[]>();
    }
}
