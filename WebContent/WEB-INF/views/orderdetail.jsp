<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Order Management System</title>
<spring:url value="/resources/assets/css/bootstrap.min.css"
	var="mainCss" />
<link href="${mainCss}" rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<spring:url value="/resources/assets/js/bootstrap.min.js" var="mainJs" />
<script src="${mainJs}"></script>

<spring:url value="/resources/assets/css/style.css" var="styleCss" />
<link href="${styleCss}" rel="stylesheet">

</head>
<body>
<div class="container container-table">
<table class="table table-inverse shoppingtable">
  <thead>
    <tr>
      <th style="color:black">Item</th>
      <th style="color:black">Quantity</th>
    </tr>
  </thead>
  <tbody>
  <c:forEach items="${orderDetail}" var="menu">
   <tr >
      <td style="color:black">${menu.menuName}</td>
      <td style="color:black">${menu.quantity}</td>
   </tr>
   </c:forEach>
</tbody>
</table>
</div>
</body>
</html>