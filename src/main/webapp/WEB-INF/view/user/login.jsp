<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
	<head>
		<title>로그인</title>
	</head>
	
	<body>
		
		<form action="${pageContext.request.contextPath}/user/loginProcess" class="well span3" method="POST">
			<c:if test="${not empty param.isError}">
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert">×</button>
				로그인 할 수 없습니다.<br>
				아이디와 비밀번호를 확인해 주세요!
			</div>
			</c:if>
			<label>아이디</label>
			<input type="text" name="j_username" class="span3">
			<label>비밀번호</label>
			<input type="password" name="j_password" class="span3">
			<br>
			<button type="submit" class="btn btn-primary">로그인</button>
			<a class="btn" href="${pageContext.request.contextPath}/user/join">회원가입</a>
		</form>

	</body>
</html>
