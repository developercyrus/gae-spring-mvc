<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Phones</title>
</head>
<body>
	<span>Select a created student, and it will show which phone
		number(s) are being assigned.</span>
	<form:form action="output4.do" modelAttribute="studentOutputForm">
		<table>

			<tr>
				<td>student</td>
				<td><form:select path="studentId">
						<form:options items="${students}" itemValue="id" itemLabel="name" />
					</form:select></td>
			</tr>

			<tr>
				<td></td>
				<td><input type="submit" value="Submit" /></td>
			</tr>
		</table>
	</form:form>
</body>
</html>