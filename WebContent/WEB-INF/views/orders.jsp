<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html lang="en">
<head>
<spring:url value="/resources/assets/css/bootstrap.min.css"
	var="mainCss" />
<link href="${mainCss}" rel="stylesheet">

<spring:url value="/resources/assets/css/style.css" var="styleCss" />
<link href="${styleCss}" rel="stylesheet">
<spring:url value="/resources/assets/css/style2.css" var="styleCss2" />
<link href="${styleCss2}" rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js" > </script>
<spring:url value="/resources/assets/js/bootstrap.min.js" var="mainJs" />
<script src="${mainJs}"></script>
  <title>Orders Details</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</head>
<body>
<%
		String user = null;
		if (session.getAttribute("user") != null && session.getAttribute("role").equals('A')) {
			user = (String) session.getAttribute("user");
		} else {
			response.sendRedirect("/cmpe275/user/login");
		}
%>
<div class="container">
		<ul class="nav nav-tabs">
			<li><a href="/cmpe275/user/login"><%=user%></a></li>
			<li class="dropdown">
		        <a class="dropdown-toggle" data-toggle="dropdown" href="#">Menu
		        <span class="caret"></span></a>
		        <ul class="dropdown-menu">
		        	<li><a href="/cmpe275/items/viewall">All Menu Items</a></li>
		          <li><a href="/cmpe275/items/viewdisabled">Enable Menu Items</a></li>
		        </ul>
     		 </li>
			<li><a href="/cmpe275/">Add Menu Item</a></li>
			<li class="active"><a href="/cmpe275/items/showOrders">Order Status</a></li>
			<li><a href="/cmpe275/user/logout">Logout</a></li>
		</ul>
</div>
<div class="container" style="color: white;">
  <h2>Orders</h2>
  <table class="table">
    <thead>
      <tr>
        <th>Order Id</th>
        <th>Chef Id</th>
        <th>Preparation Date</th>
		<th>Start Time</th>
		<th>End Time</th>
		<th>Status</th>
      </tr>
    </thead>
      <c:forEach items="${ordersList}" var="orders">
    <tbody>
      <tr>
        <td>${orders.orderId}</td>
        <td>${orders.chefId}</td>
        <td>${orders.prepDate}</td>
        <td>${orders.startTime}</td>
        <td>${orders.endTime}</td>
        <td>${orders.status}</td>
      </tr>
    </tbody>
        </c:forEach>    
  </table>
</div>

</body>
</html>
