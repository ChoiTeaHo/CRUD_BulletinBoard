<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>게시글 삭제</title>
</head>
<body>

<form action="modify.do" method="post" >

    <input type="hidden" name="no" value="${modReq.articleNumber}">
    <p>
        삭제기본키:  ${modReq.articleNumber}
    </p>
    
    <p>
        사용자:  ${modReq.userId}
    </p>
    
    <p>
    님께서 삭제를 완료하셨습니다.
    </p>
    
    <input type="submit" value="글 삭제">
</form>

<c:set var="ctxPath" value="${pageContext.request.contextPath}" />
<a href="${ctxPath}/article/list.do">[게시글 목록보기]</a>


</body>
</html>