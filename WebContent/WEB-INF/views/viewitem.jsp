<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Bootstrap Example</title>
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
<script>
	$(document).ready(function() {
		$('[data-toggle="popover"]').popover();
	});
</script>
</head>
<body>
	<nav class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">OrderNow</a>
			</div>
			<%
			if (session.getAttribute("role").equals('U')) {
				// user
			%>
				<button type="button" class="btn btn-info btn-lg pull-right">
					<span class="glyphicon glyphicon-shopping-cart"></span> Shopping Cart
				</button>
			<%
			} else if (session.getAttribute("role").equals('A')) {
			%>
			<button type="button" class="btn btn-danger btn-lg pull-right">
					<span class="glyphicon glyphicon-shopping-cart"></span> Reset
			</button>
			<% } %>
			
		</div>
	</nav>
	<div class="row">
		<c:forEach items="${list}" var="menu">
			<div class="col-lg-4">
				<div class="thumbnail">
					<a href="/cmpe275/items/${menu.menuId}" title="${menu.name}"
						data-toggle="popover" data-trigger="hover" data-placement="bottom" data-html="true"
						data-content="Category = ${menu.category} <br />
						Name = ${menu.name} <br />
						Price = ${menu.unitPrice} <br />
						Calories = ${menu.calories}"> <img
						class="img-rounded" src="/cmpe275/items/${menu.menuId}/picture"></img>
					</a>
					<div class="caption">
						<h3>${menu.name}</h3>
						<h4>${menu.category}</h4>
					</div>
					<form method="post" action="/cmpe275/items/delete/${menu.menuId}">
						<button class="btn btn-danger" role="button">
							<span class="glyphicon glyphicon-remove"></span> Remove
						</button>
						<button class="btn btn-info" role="button">
							<span class="glyphicon glyphicon-plus-sign"></span> Add to Cart
						</button>
					</form>
				</div>
			</div>
		</c:forEach>
	</div>

<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">OrderNow</a>
    </div>
    <button type="button" class="btn btn-info btn-lg pull-right">
          <span class="glyphicon glyphicon-shopping-cart"></span> Shopping Cart
        </button>
  </div>
</nav>
<div class="row">
<c:set var="isDisabled" value="${isDisabled}"/>
  <c:forEach items="${list}" var="menu">
  <div class="col-lg-4">
  <div class="thumbnail">
    <a  href="/cmpe275/items/${menu.menuId}">
      <img class="img-rounded" src="/cmpe275/items/${menu.menuId}/picture"></img>
    </a>
       <div class="caption">
    		<h3>${menu.name}</h3>
    		<h4>${menu.category}</h4>
       </div>
       
       <c:if test="${isDisabled==1}">
	       <form method="post" action="/cmpe275/items/enable/${menu.menuId}">
	       <button class="btn btn-danger" role="button"> 
	       <span class="glyphicon glyphicon-ok"></span> Enable 
       </button>
       </form>
       </c:if>
       <c:if test="${isDisabled== 0}">
	       <form method="post" action="/cmpe275/items/delete/${menu.menuId}">
	       <button class="btn btn-danger" role="button"> 
	       <span class="glyphicon glyphicon-remove"></span> Remove 
	       </button>
	       </form>
	        <button class="btn btn-danger" role="button"> 
	       <span class="glyphicon glyphicon-plus-sign"></span> Add to Cart 
	       </button>
       </c:if>
        <button class="btn btn-danger" role="button"> 
       <span class="glyphicon glyphicon-plus-sign"></span> Add to Cart 
       </button>
    </div>
  </div>
    </c:forEach>
</div>

</body>
</html>