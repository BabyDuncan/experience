<%--
  模拟user页面的main模块
  User: guohaozhao@yahoo.cn
  Date: 13-3-9
  Time: 下午2:15
 --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib prefix="t" uri="http://babyduncan.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:frag>
    <div>
        <div>main content ${main}</div>
        <c:if test="${not empty main_embed}">
            <jsp:include page="${main_embed}.jsp"/>
        </c:if>
        <c:if test="${empty  main_embed}">
            <jsp:include page="timeline/_timeline.jsp"/>
        </c:if>
    </div>
</t:frag>