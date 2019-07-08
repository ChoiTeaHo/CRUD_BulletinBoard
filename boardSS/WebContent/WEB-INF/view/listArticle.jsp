<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>게시글 목록</title>
</head>
<body>

<table border="1">
    <tr>
        <td colspan="4"><a href="write.do">[게시글쓰기]</a></td>
    </tr>
    <tr>
        <td>번호</td>
        <td>제목</td>
        <td>작성자</td>
        <td>조회수</td>
    </tr>
    
    
 
    <!-- 게시물없으면 없다고 띄우기 -->
    <c:if test="${articlePage.hasNoArticles()}">	<!-- total값이 0이라면  -->
        <tr>
            <td colspan="4">게시글이 없습니다.</td>
        </tr>
    </c:if>
    
    
    <!-- 게시글을 생성 -->
    <c:forEach var="article" items="${articlePage.content}">	<!--저장된 articlePage<Article> 리스트객체를 변수에 하나씩 저장 -->
        <tr>
        	<!-- 게시글번호(객체의 number) -->
            <td>${article.number}</td>	<!--article<Article> 리스트객체 number를 꺼냄-->
            
            <!-- 게시글제목(객체의 title) -->
            <td>
            	<a href="read.do?no=${article.number}&pageNo=${articlePage.currentPage}">	<!-- a태그시작 링크는 read.do로 가며 객체의num/pageNo은  1값-->
            		<c:out value="${article.title}" /> <!-- 리스트객체 title 꺼냄 -->
            	</a>	
            </td>
            
            <!-- 글쓴이(객체의 writer.name) -->
            <td>${article.writer.name}</td>
            
            <!-- 조회수(객체의 readCount) -->
            <td>${article.readCount}</td>
        </tr>
    </c:forEach>
      
    
    <!-- 하단 링크이동생성 -->
    <c:if test="${articlePage.hasArticles()}">  <!-- total > 0  이조건인데 현재 13>0 이므로 True -->
        <tr>
            <td colspan="4">
            
            	<!-- 이전링크생성 -->
                <c:if test="${articlePage.startPage > 5}">	<!-- 현재 StartPage값 1값   False -->
                    <a href="list.do?pageNo=${articlePage.startPage-5}">[이전]</a>	<!-- 즉, startPage가 6이상이면 -5하는링크생성 -->
                </c:if>
               
               <!-- 하단 게시글목록이동번호생성  -->
               <!-- startPage: 1값  /  endPage: 2값  (즉, 2번반복실행)-->
                <c:forEach var="pNo" 
                	begin="${articlePage.startPage}" 
                	end="${articlePage.endPage}">
                    						
                    <a href="list.do?pageNo=${pNo}"> <!-- list.do?pageNo= --> <!-- 링크생성 --> 
                    	[${pNo}]   
                    </a> 			
                </c:forEach>
                
                <!-- 다음링크생성 -->
                <c:if test="${articlePage.endPage < articlePage.totalPages}"> <!-- 2 < 2 결국조건 false -->
                    <a href="list.do?pageNo=${articlePage.startPage + 5}">  [다음]  </a>
                </c:if>
            </td>
        </tr>
    </c:if>
</table>

</body>
</html>
