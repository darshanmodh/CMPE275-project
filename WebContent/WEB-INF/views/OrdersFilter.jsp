<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE HTML>
<html>
<title>Order Management System</title>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

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
</head>
<body>
<body id="bod">
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
			<li><a href="/cmpe275/orders/popular">Popular</a></li>
			<li><a href="/cmpe275/user/logout">Logout</a></li>
		</ul>
</div>
	<div class="container">
		<form class="form-signin" method="get"
			action="/cmpe275/items/getCustomerDetails">
			<h2 style="color: black">Pick a date range to view orders</h2>
			<br><label class="form-label">Start Date</label> <input
				class="form-control" id="startDate" name="startDate" type="date" required/>
			<label class="form-label" >End Date</label> <input
				class="form-control" name="endDate" id="endDate" type="date" required/><br>
			<br>
			<input type="hidden" name="sortBy" value="orderTime" />
			<button type="submit" class="btn btn-lg btn-primary btn-block">Show
				Orders</button>
		</form>
	</div>

</body>