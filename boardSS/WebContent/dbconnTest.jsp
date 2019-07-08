<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="jdbc.connection.ConnectionProvider"%>
<%@ page import="java.sql.*" %>

<html>
<head><title>연결 테스트</title></head>
<body>
<%
	try(Connection conn = ConnectionProvider.getConnection()){
		out.print("커넥션 연결성공");
	}catch(SQLException ex){
		out.print("커넥션 연결 실패함" + ex.getMessage());
		application.log("커넥션 연결 실패", ex);
	}
%>
</body>
</html>

