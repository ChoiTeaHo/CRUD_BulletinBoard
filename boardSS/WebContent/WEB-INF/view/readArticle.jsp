<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="u" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>게시글 읽기</title>
</head>
<body>

<table border="1" width="100%">
    <tr>
        <td>번호</td>
        <td>${articleData.article.number}</td>
    </tr>
    <tr>
        <td>작성자</td>
        <td>${articleData.article.writer.name}</td>
    </tr>
    <tr>
        <td>제목</td>
        <td>${articleData.article.title}</td>
    </tr>
    <tr>
        <td>내용</td>
        <td><u:pre value="${articleData.content}"></u:pre></td>
    </tr>
    
    <tr>
        <td colspan="2">
            <c:set var="pageNo" value="${empty param.pageNo?'1':param.pageNo}" />
            	<a href="list.do?pageNo=${pageNo}">[목록]</a>

			<!-- 세션User객체의 id와   && 게시물의 작성자 articleDate의 id를 비교하여 일치하면 출력 -->
            <c:if test="${authUser.id == articleData.article.writer.id}">
                <a href="modify.do?no=${articleData.article.number}">[게시글수정]</a>
                <a href="delete.do?no=${articleData.article.number}">[게시글삭제]</a>
            </c:if>
        </td>
    </tr>
</table>

</body>
</html>
