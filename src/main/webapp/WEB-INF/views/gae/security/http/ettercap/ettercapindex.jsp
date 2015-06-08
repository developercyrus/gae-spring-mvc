<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>ettercap login</title>
</head>
<body>
	<span>Type user account (test/test)</span>
	<form:form action="ettercaplogin.do" commandName="user">
		<table>
			<tr>
				<td>username</td>
				<td><form:input path="username" /></td>
			</tr>
			<tr>
				<td>password</td>
				<td><form:password path="password" /></td>
			</tr>
			<tr>
				<td></td>
				<td><input type="submit" value="Submit" /></td>
			</tr>
		</table>
	</form:form>
</body>
</html>
