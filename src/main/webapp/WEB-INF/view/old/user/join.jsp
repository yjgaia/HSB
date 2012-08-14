<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
	<head>
		<title>회원 가입</title>
	</head>
	
	<body>
		
		<form:form commandName="userInfo" cssClass="form-horizontal">
			<fieldset>
				<div class="control-group <spring:bind path="username"><c:if test="${not empty status.errorMessage}">error</c:if></spring:bind>">
					<label class="control-label">아이디</label>
					<div class="controls">
						<form:input path="username" />
						<form:errors path="username" cssClass="help-inline" />
					</div>
				</div>
				<div class="control-group <spring:bind path="password"><c:if test="${not empty status.errorMessage}">error</c:if></spring:bind>">
					<label class="control-label">비밀번호</label>
					<div class="controls">
						<form:password path="password" />
						<form:errors path="password" cssClass="help-inline" />
					</div>
				</div>
				<div class="control-group <spring:bind path="passwordConfirm"><c:if test="${not empty status.errorMessage}">error</c:if></spring:bind>">
					<label class="control-label">비밀번호 확인</label>
					<div class="controls">
						<form:password path="passwordConfirm" />
						<form:errors path="passwordConfirm" cssClass="help-inline" />
					</div>
				</div>
				<div class="control-group <spring:bind path="nickname"><c:if test="${not empty status.errorMessage}">error</c:if></spring:bind>">
					<label class="control-label">닉네임</label>
					<div class="controls">
						<form:input path="nickname" />
						<form:errors path="nickname" cssClass="help-inline" />
					</div>
				</div>
				<div class="control-group <spring:bind path="email"><c:if test="${not empty status.errorMessage}">error</c:if></spring:bind>">
					<label class="control-label">이메일</label>
					<div class="controls">
						<form:input path="email" />
						<form:errors path="email" cssClass="help-inline" />
					</div>
				</div>
				<div class="form-actions">
					<button type="submit" class="btn btn-success">
						<i class="icon-ok icon-white"></i>
						회원가입
					</button>
					<a class="btn" href="javascript:history.back();">취소</a>
				</div>
			</fieldset>
		</form:form>

	</body>
</html>
