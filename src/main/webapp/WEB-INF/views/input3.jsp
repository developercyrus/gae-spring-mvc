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
	<span>Create a student name, and assign the created phone
		number(s) to that student. This relationship will be saved in
		database.</span>
	<form:form action="output3.do" modelAttribute="studentInputForm">
		<table>
			<tr>
				<td>student name</td>
				<td><form:input path="studentName" /></td>
			</tr>
			<tr>
				<td>phone number</td>
				<td><form:select path="phoneId">
						<form:options items="${phones}" itemValue="id" itemLabel="phoneNo" />
					</form:select></td>
			</tr>
			<tr>
				<td>phone numbers</td>
				<td><form:select path="phoneIds">
						<form:options items="${phones}" itemValue="id" itemLabel="phoneNo" />
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
