<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>

<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Phone Output</title>
</head>
<body>
	<table>
		<tr>
			<td>Student Name</td>
			<td>${student.name}</td>
		</tr>
		<tr>
			<td>Phone</td>
			<td><c:forEach items="${phones}" var="phone">
					<c:out value="${phone.phoneNo}" />
				</c:forEach></td>
		</tr>
	</table>
</body>
</html>