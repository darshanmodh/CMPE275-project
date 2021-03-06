<%@page import="cmpe275.order.model.MenuItem"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://myfn" prefix="myfn" %>
<!DOCTYPE html>
<html lang="en">
<head>
<%
	String user = null;
	if (session.getAttribute("user") != null) {
		user = (String) session.getAttribute("user");
	}
%>
<title>Order Management System</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<spring:url value="/resources/assets/css/bootstrap.min.css"
	var="mainCss" />
<link href="${mainCss}" rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<spring:url value="/resources/assets/js/bootstrap.min.js" var="mainJs" />
<script src="${mainJs}"></script>

<spring:url value="/resources/assets/css/style.css" var="styleCss" />
<link href="${styleCss}" rel="stylesheet">
<script type="text/javascript">
	$(document).ready(function() {
		$('[data-toggle="popover"]').popover();
	});
	
	 function doAjaxPost(menuId) {
	        $.ajax({
	            type: "POST",
	            url: menuId,
	            success: function(response) {
	                $("#subViewDiv").html( response );
	            }
	        });
	    } 
</script>
</head>
<body>
	<div class="container">
		<ul class="nav nav-tabs">
			<%
				try {
					if (session.getAttribute("role").equals('U')) {
			%>
			<li><a href="/cmpe275/user/login"><%=user%></a></li>
			<li class="active"><a href="/cmpe275/items/viewall">Menu</a></li>
			<li><a href="/cmpe275/orders">Orders</a></li>
			<li><a href="/cmpe275/user/logout">Logout</a></li>
			<form method="get" action="/cmpe275/items/getCartdetails">
				<button type="submit" class="btn btn-info btn-lg pull-right">
					<span class="glyphicon glyphicon-shopping-cart"></span> Shopping
					Cart
				</button>
			</form>
			<%
				} else if (session.getAttribute("role").equals('A')) {
			%>
			<li><a href="/cmpe275/user/login"><%=user%></a></li>
			<li class="dropdown active"><a class="dropdown-toggle"
				data-toggle="dropdown" href="#">Menu <span class="caret"></span></a>
				<ul class="dropdown-menu">
					<li><a href="/cmpe275/items/viewall">All Menu Items</a></li>
					<li><a href="/cmpe275/items/viewdisabled">Enable Menu
							Items</a></li>
				</ul></li>
			<li><a href="/cmpe275/">Add Menu Item</a></li>
			<li><a href="/cmpe275/items/showOrders">Order Status</a></li>
			<li><a href="/cmpe275/orders/popular">Popular</a></li>
			<li><a href="/cmpe275/user/logout">Logout</a></li>
			<form method="POST" action="/cmpe275/orders/deleteall">
				<input type="hidden" name="_method" value="DELETE">
				<button type="submit" class="btn btn-danger btn-lg pull-right">
					<span class="glyphicon glyphicon-repeat"></span> Reset
				</button>
			</form>
			<%
				}
				} catch (NullPointerException e) {
					response.setStatus(response.SC_MOVED_TEMPORARILY);
					response.setHeader("Location", "/cmpe275/user/login");
					response.sendRedirect("/cmpe275/user/login");
				}
			%>
		</ul>
		<c:set var="isdis" value="${isDisabled}" />
		<c:if test="${isdis==0}">
		<div class="dropdown pull-right">
    <button class="btn btn-default dropdown-toggle" type="button" data-toggle="dropdown">Sort Results
    <span class="caret"></span></button>
    <ul class="dropdown-menu">
      <li><a href="/cmpe275/items/category/appetizer">Appetizer</a></li>
      <li><a href="/cmpe275/items/category/mainCourse">Main Course</a></li>
      <li><a href="/cmpe275/items/category/desert">Desert</a></li>
      <li><a href="/cmpe275/items/category/drink">Drink</a></li>
    </ul>
  </div>
  </c:if>
	</div>
	<br>
	
  
	<c:set var="isDisabled" value="${isDisabled}" />
	<div class="row">
		<c:forEach items="${list}" var="menu">
			<div class="col-lg-5"  style="float:right">
				<div class="thumbnail">
					<a href="/cmpe275/items/${menu.menuId}/picture"
						title="${menu.name}" data-toggle="popover" data-trigger="hover"
						data-placement="bottom" data-html="true"
						data-content="Category = ${menu.category} <br />
						Name = ${menu.name} <br />
						Price = $${menu.unitPrice} <br />
						Calories = ${menu.calories}">
						<img class="img-rounded"
						src="/cmpe275/items/${menu.menuId}/picture"></img>
					</a>

					<div class="caption">
						<c:set var="mId" value="${menu.menuId}"></c:set>
						<c:set var="rating" value="${myfn:getValue(avgRating,mId)}"/>
						<a class="pull-right" href="">Avg: ${rating}</a>
						
						<h3>${menu.name}</h3>
						<c:if test="${myfn:contains(ratingMenuId,mId)}">
						<form method="post" action="/cmpe275/items/ratings/${menu.menuId}">
						<div class="pull-right">
						<label >Rate:</label>
						<input id="rate" name="rate" type="number" min="0" max="5" required>
						<button type="submit"><span class="glyphicon glyphicon-heart"></span></button>
						</div>
						</form>
						</c:if>
						
						
						<h4>${menu.category}</h4>
						
					</div>
					<%
						try {
								if (session.getAttribute("role").equals('U')) {
					%>
					<form method="post" action="/cmpe275/items/shoppingCart">
						<div class="input-group">
							<span class="input-group-btn">
								<button id="addtocart" class="btn btn-danger" role="button">
									<span class="glyphicon glyphicon-plus-sign"> </span> Add to
									Cart
								</button>
							</span> <input type="hidden" name="menuid" value='${menu.menuId}'>
							<input id="quantity" name="quantity" class="form-control"
								type="number" placeholder="Quantity" min="1" max="100" value="1" required>
							<input type="hidden" id="menuName" name="menuName"
								value='${menu.name}'>
								
								 <input type="hidden" name="prepTime" value='${menu.prepTime}'> 
								 <input type="hidden" name="price" value='${menu.unitPrice}'>
								
						</div>
					</form>
					<%
						} else if (session.getAttribute("role").equals('A')) {
					%>


					<c:if test="${isDisabled==1}">
						<form method="post" action="/cmpe275/items/enable/${menu.menuId}">
							<button class="btn btn-success" role="button">
								<span class="glyphicon glyphicon-ok"></span> Enable
							</button>
						</form>
					</c:if>
					<c:if test="${isDisabled==0}">
						<form class="btn-group" method="post"
							action="/cmpe275/items/delete/${menu.menuId}">
							<input type="hidden" class="btn">
							<button class="btn btn-danger" role="button">
								<span class="glyphicon glyphicon-trash"></span> Disable
							</button>
						</form>
						<button class="btn btn-warning" role="button" data-toggle="modal"
							data-target="#myModalHorizontal"
							onclick="doAjaxPost(${menu.menuId});">
							<span class="glyphicon glyphicon-pencil"></span> Update
						</button>
						<input type="hidden" class="btn">
					</c:if>
					<%
						}
							} catch (NullPointerException e) {
								response.setStatus(response.SC_MOVED_TEMPORARILY);
								response.setHeader("Location", "/cmpe275/user/login/");
							}
					%>
				</div>
			</div>

			<div class="modal fade" id="myModalHorizontal" tabindex="-1"
				role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<!-- Modal Header -->
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">
								<span aria-hidden="true">&times;</span> <span class="sr-only">Close</span>
							</button>
							<h4 class="modal-title" id="myModalLabel">Update Menu Item</h4>
						</div>
						<div id="subViewDiv"></div>
					</div>
				</div>
			</div>
		</c:forEach>
	</div>

</body>
</html>