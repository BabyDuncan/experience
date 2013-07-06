<%--
 模拟用户主页面
 User: guohaozhao@yahoo.cn
 Date: 13-3-9
 Time: 下午2:13
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib prefix="t" uri="http://babyduncan.com" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:frag>
    <div>
        <div> user content . I am ${username}</div>
        <c:if test="${not empty user_embed}">
            <jsp:include page="${user_embed}.jsp"/>
        </c:if>
        <c:if test="${empty user_embed}">
            <jsp:include page="main/_main.jsp"/>
        </c:if>
    </div>
</t:frag>