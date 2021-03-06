<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Popular Orders</title>
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
<%
		String user = null;
		if (session.getAttribute("user") != null) {
			user = (String) session.getAttribute("user");
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
			<li><a href="/cmpe275/items/showOrders">Order Status</a></li>
			<li  class="active"><a href="/cmpe275/orders/popular">Popular</a></li>
			<li><a href="/cmpe275/user/logout">Logout</a></li>
		</ul>
	</div>
	<div class="container container-table">
<table class="table table-inverse shoppingtable">
  <thead>
    <tr>
      <th style="color:black">Id</th>
      <th style="color:black">Name</th>
       <th style="color:black">Category</th>
      <th style="color:black">Count</th>
    </tr>
  </thead>
  <tbody>
  <c:forEach items="${popular}" var="menu">
   <tr >
      <td style="color:black">${menu.menuId}</td>
      <td style="color:black">${menu.name}</td>
      <td style="color:black">${menu.category}</td>
      <td style="color:black">${menu.count}</td>
   </tr>
   </c:forEach>
</tbody>
</table>
</div>
	
</body>
</html>