<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<head>
		<title>HSB</title>
	</head>
	
	<body>
	
		<h1>HSB</h1>

		<p>
			<%--<sec:authorize access="isAnonymous()">--%>
			<a class="btn" href="${pageContext.request.contextPath}/user/join">회원가입 페이지</a>
			<a class="btn" href="${pageContext.request.contextPath}/user/login">로그인 페이지</a>
			<%--</sec:authorize>--%>
			<%--<sec:authorize access="isAuthenticated()">--%>
			<a class="btn" href="${pageContext.request.contextPath}/user/logout">로그아웃</a>
			<%--</sec:authorize>--%>
		</p>

	</body>
</html>
