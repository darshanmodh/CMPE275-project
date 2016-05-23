<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE HTML>
<html>
<title>Order Management System</title>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<%
try{
	if(session.getAttribute("role").equals('U')) {
		response.sendRedirect("/cmpe275/items/viewall");
	}
	String user = null;
	if (session.getAttribute("user") != null) {
		user = (String) session.getAttribute("user");
	}
%>

<spring:url value="/resources/assets/css/bootstrap.min.css"
	var="mainCss" />
<link href="${mainCss}" rel="stylesheet">

<spring:url value="/resources/assets/css/style2.css" var="styleCss2" />
<link href="${styleCss2}" rel="stylesheet">

<spring:url value="/resources/assets/css/style.css" var="styleCss" />
<link href="${styleCss}" rel="stylesheet">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<spring:url value="/resources/assets/js/bootstrap.min.js" var="mainJs" />
<script src="${mainJs}"></script>

<script type="text/javascript">
$(document).ready(function(){
          console.log("start");
		document.getElementById("picture").style.visibility="hidden";

});
function readURL(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    $('#picture')
                        .attr('src', e.target.result)
                        .width(200)
                        .height(200);
                        document.getElementById("picture").style.visibility="visible";
                        document.getElementById("divId").value = e.target.result.split(",")[1];
                };
                reader.readAsDataURL(input.files[0]);
            }
            else
            {
   	        $('#picture')
                        .width(0)
                        .height(0);
   	         document.getElementById("picture").style.visibility="hidden";
			}
        }
        
</script>

<!--[if lte IE 9]><link href="${ie9}" rel="stylesheet" /><![endif]-->
<!--[if lte IE 8]><link href="${ie8}" rel="stylesheet" /><![endif]-->
</head>
<body id="bod">
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
			<li class="active"><a href="/cmpe275/">Add Menu Item</a></li>
			<li><a href="/cmpe275/items/showOrders">Order Status</a></li>
			<li><a href="/cmpe275/orders/popular">Popular</a></li>
			<li><a href="/cmpe275/user/logout">Logout</a></li>
		</ul>
	</div>
	<div class="container">
		<form class="form-signin" method="post" action="/cmpe275/items/add">
			<input id="name" class="form-control" type="text"
				placeholder="Item Name" name="name" required autofocus> <label
				class="form-label">Picture</label> <input id="inputFileToLoad"  type="file"
				accept="image/*" class="buttonSpace" onchange="readURL(this);"
				style="margin-bottom: 5px" required="true" /> <img id="picture" 
				src="#" alt="your image" /> 
				<select name="category" required class="form-control">
				
				<option value="" selected="selected" disabled="disabled">Select
					a category <span class="arrow">&#9661;</span></option>
				<option value="appetizer">Appetizer</option>
				<option value="mainCourse">Main Course</option>
				<option value="desert">Desert</option>
				<option value="drink">Drink</option>
			</select> 
			<input id="price" class="form-control" type="text"
				pattern="[0-9]*.[0-9]|[0-9]*" placeholder="Price(eg $5.5)" name="unitPrice" required>

			<input id="calories" name="calories" class="form-control" type="number"
				placeholder="Calories" min="0" required> <input
				id="prepTime" name="prepTime" class="form-control" type="number"
				style="margin-bottom: 50px" placeholder="Preparation Time(minutes)"
				min="1" max="10" required>

			<button type="submit" class="btn btn-lg btn-primary btn-block">Add
				Item</button>
				<input type="hidden" id="divId" name="picture">
		<c:set var="success" value="${success}" />
		<c:if test="${success ne true}">
		<p class="form-label">Please enter a different Name.</p>
		</c:if>
		</form>
	</div>

</body>
</html>

<%
} catch(NullPointerException e) {
	response.sendRedirect("/cmpe275/user/login");
}
%>