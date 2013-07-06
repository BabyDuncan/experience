<%--
  最外层的页面
  User: guohaozhao@yahoo.cn
  Date: 13-3-9
  Time: 下午2:12
 --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>
        partview demo .
    </title>
</head>
<body>
<div>
    sys content . ${syscontent}
</div>
<c:if test="${not empty sys_embed}">
    <jsp:include page="${sys_embed}.jsp"/>
</c:if>
<c:if test="${empty  sys_embed}">
    <jsp:include page="user/_user.jsp"/>
</c:if>
</body>
</html>