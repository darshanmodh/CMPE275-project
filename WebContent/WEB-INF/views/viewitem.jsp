<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
	<nav class="navbar navbar-default">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="#">OrderNow</a>
			</div>
			<%
				try {
					if (session.getAttribute("role").equals('U')) {
			%>
			<button type="button" class="btn btn-info btn-lg pull-right">
				<span class="glyphicon glyphicon-shopping-cart"></span> Shopping
				Cart
			</button>
			<%
				} else if (session.getAttribute("role").equals('A')) {
			%>
			<button type="button" class="btn btn-danger btn-lg pull-right">
				<span class="glyphicon glyphicon-repeat"></span> Reset
			</button>
			<%
				}
				} catch (NullPointerException e) {
					response.setStatus(response.SC_MOVED_TEMPORARILY);
					response.setHeader("Location", "/cmpe275/user/login");
					response.sendRedirect("/cmpe275/user/login");
				}
			%>

		</div>
	</nav>
	<div class="row">
		<c:forEach items="${list}" var="menu">
			<div class="col-lg-4">
				<div class="thumbnail">
					<a href="/cmpe275/items/${menu.menuId}/picture" title="${menu.name}"
						data-toggle="popover" data-trigger="hover" data-placement="bottom"
						data-html="true"
						data-content="Category = ${menu.category} <br />
						Name = ${menu.name} <br />
						Price = $${menu.unitPrice} <br />
						Calories = ${menu.calories}">
						<img class="img-rounded"
						src="/cmpe275/items/${menu.menuId}/picture"></img>
					</a>

					<div class="caption">
						<h3>${menu.name}</h3>
						<h4>${menu.category}</h4>
					</div>
					<%
						try {
								if (session.getAttribute("role").equals('U')) {
					%>
					<form method="post" action="/cmpe275/items/delete/${menu.menuId}">
						<button class="btn btn-info" role="button">
							<span class="glyphicon glyphicon-plus"></span> Add to Cart
						</button>
					</form>
					<%
						} else if (session.getAttribute("role").equals('A')) {
					%>
					<button class="btn btn-warning" role="button" data-toggle="modal"
						data-target="#myModalHorizontal"
						onclick="doAjaxPost(${menu.menuId});">
						<span class="glyphicon glyphicon-pencil"></span> Update
					</button>
					<input type="hidden" class="btn">


					<form class="btn-group" method="post"
						action="/cmpe275/items/delete/${menu.menuId}">
						<input type="hidden" class="btn">
						<button class="btn btn-danger" role="button">
							<span class="glyphicon glyphicon-trash"></span> Remove
						</button>
					</form>
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

<nav class="navbar navbar-default">
  <div class="container-fluid">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">OrderNow</a>
    </div>
<div class="container">
  
  <!-- Trigger the modal with a button -->
  
  <form method="get" action="/cmpe275/items/getCartdetails">
     
 
  <button type="button" class="btn btn-info btn-lg pull-right" data-toggle="modal" data-target="#ShoppingCartModel">
  
  Shopping Cart</button>
  
  
 
          
  <!-- Modal -->
  <div class="modal fade" id="ShoppingCartModel" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Shopping Cart </h4>
        </div>
        <div class="modal-body">
        
         
        
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
   </form>
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