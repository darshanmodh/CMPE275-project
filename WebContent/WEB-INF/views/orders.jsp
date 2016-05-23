<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Orders Details</title>
  <meta charset="utf-8">
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
<div class="container" style="color: black;">
  <h2>Orders</h2>
  <div class="dropdown pull-right">
    <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">Sort Results
    <span class="caret"></span></button>
    <ul class="dropdown-menu">
      <li><a href="/cmpe275/items/getCustomerDetails?startDate=${startDate}&endDate=${endDate}&sortBy=orderTime">Order Time</a></li>
      <li><a href="/cmpe275/items/getCustomerDetails?startDate=${startDate}&endDate=${endDate}&sortBy=startTime">Start Time</a></li>
    </ul>
  </div>
  <table class="table">
    <thead>
      <tr>
        <th>Order Id</th>
        <th>Chef Id</th>
        <th>Preparation Date</th>
        <th>Order Time</th>
		<th>Start Time</th>
		<th>Ready Time</th>
		<th>Pick Up Date</th>
		<th>Pick Up Time</th>
		<th>Status</th>
		<th>Customer Email</th>
		<th>Contents</th>
		<th>Total Price</th>
      </tr>
    </thead>
      <c:forEach items="${ordersList}" var="orders">
    <tbody>
      <tr>
        <td>${orders.orderId}</td>
        <td>${orders.chefId}</td>
        <td>${orders.prepDate}</td>
        <td>${orders.orderTime}</td>
        <td>${orders.startTime}</td>
        <td>${orders.endTime}</td>
        <td>${orders.pickUpDate}</td>
        <td>${orders.pickupTime }</td>
        <td>${orders.status}</td>
        <td>${orders.email}</td>
        <td><a href="/cmpe275/orders/${orders.orderId}" target="_blank" class="btn btn-primary btn-sm" role="button">View</a></td>
        <td>${orders.totalPrice }</td>
      </tr>
    </tbody>
        </c:forEach>    
  </table>
</div>

</body>
</html>
