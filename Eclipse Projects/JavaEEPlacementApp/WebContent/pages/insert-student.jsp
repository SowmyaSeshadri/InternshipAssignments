<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Insert Student Details</title>
<c:set var="req" value="<%=request.getContextPath() %>" />
<base href="${req}/" />
<link rel="stylesheet" type="text/css" href="css/admin-navbar.css">
<link rel="stylesheet" type="text/css" href="css/insert-details.css">
</head>
<body>
	<%
		String responseMessage;
		if(request.getAttribute("responseMessage") == null) {
			responseMessage = "";
		} else {
			responseMessage = (String) request.getAttribute("responseMessage");
		}
	%>
	<div class="navbar">
		<a href="pages/admin.jsp">HOME</a>
		<a href="#">Insert Student</a>
		<a href="ViewStudentsServlet">View Student</a>
		<a href="pages/insert-company.jsp">Insert Company</a>
		<a href="ViewCompanyServlet">View Company</a>
		<a href="index.jsp">Exit</a>
	</div>
	<form action="InsertStudentServlet" method="POST">
	<table>
		<tr>
			<td class="right-align"><label for="companyid">Enter the Register number : </label></td>
			<td><input type="text" name="regno" id="companyid"></td>
		</tr>
		<tr>
			<td class="right-align"><label for="company-name">Enter the student name : </label></td>
			<td><input type="text" name="studentName" id="company-name"></td>
		</tr>
		<tr>
			<td class="right-align"><label for="email">Enter the email : </label></td>
			<td><input type="text" name="email" id="email"></td>
		</tr>
		<tr>
			<td class="right-align"><label for="deptName">Enter the department name : </label></td>
			<td><input type="text" name="deptName" id="deptName"></td>
		</tr>
		<tr>
			<td class="right-align"><label for="arrear-criteria">Enter the number of arrears : </label></td>
			<td><input type="text" name="arrears" id="arrear-criteria"></td>
		</tr>
		<tr>
			<td class="right-align"	><label for="cgpa-criteria">Enter the CGPA : </label></td>
			<td><input type="text" name="cgpa" id="cgpa-criteria"></td>
		</tr>
	</table>
		<p class="error-message"><%=responseMessage %></p>
		<input type="submit" value="Insert">
	</form>
</body>
</html>