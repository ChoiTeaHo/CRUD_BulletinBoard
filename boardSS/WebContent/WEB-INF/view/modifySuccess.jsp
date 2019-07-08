<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>게시글 수정</title>
</head>
<body>

게시글을 수정했습니다.
<br/>

<c:set var="ctxPath" value="${pageContext.request.contextPath}" />
<a href="${ctxPath}/article/list.do">[게시글 목록보기]</a>
<a href="${ctxPath}/article/read.do?no=${modReq.articleNumber}">[게시글 내용보기]</a>

</body>
</html>
