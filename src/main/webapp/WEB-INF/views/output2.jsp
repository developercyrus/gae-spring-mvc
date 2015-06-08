<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Phone Output</title>
</head>
<body>
	<table>
		<tr>
			<td>Phone ID:</td>
			<td>${phone.id}</td>
		</tr>
		<tr>
			<td>Phone No:</td>
			<td>${phone.phoneNo}</td>
		</tr>
	</table>
	<a href="input3.do">next</a>
</body>
</html>