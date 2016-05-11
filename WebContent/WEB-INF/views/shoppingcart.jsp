<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<title>Order Management System</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
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
$('#checkbox').change(function() {
	   if($(this).is(":checked")) {
		   $("#time").removeAttr("disabled");
		     $("#date").removeAttr("disabled");
	   }
	   else {
		   $("#time").attr("disabled","disabled");
		     $("#date").attr("disabled","disabled");
	   }
	});
	
	$("#order").click(function(){
		
		if($('#checkbox').is(":checked"))
			{
			
			console.log($('#date').val());
			console.log($('#time').val());
			$.ajax({
				type:"post",
				url:"/cmpe275/items/orderNow?dop="+$('#date').val()+"&pickupTime="+$('#time').val()+":00",
				success:function(response) {
					if (response.success == 'auto order placed' || response.success == 'manual order placed') {
						console.log(response);
						alert(response.msg);
						window.location.href = '/cmpe275/items/viewall';
					}
					if (response.success == 'cannot place') {
						alert(response.msg);
					}
				}
			});
			}
		else
		{
		$.ajax({
			type:"post",
			url:"/cmpe275/items/orderNow",
			success:function(response) {
				if (response.success == 'auto order placed' || response.success == 'manual order placed') {
					console.log(response);
					alert(response.msg);
					window.location.href = '/cmpe275/items/viewall';
				}
				if (response.success == 'cannot place') {
					alert(response.msg);
				}
			}
		});}
		
	});
});
</script>
</head>
<body>
<%
		String user = null;
		if (session.getAttribute("user") != null && session.getAttribute("role").equals('U')) {
			user = (String) session.getAttribute("user");
		} else {
			response.sendRedirect("/cmpe275/user/login");
		}
%>
<div class="container">
		<ul class="nav nav-tabs">
			<li class="active"><a href="/cmpe275/user/login"><%=user%></a></li>
			<li><a href="/cmpe275/items/viewall">Menu</a></li>
			<li><a href="#">Orders</a></li>
			<li><a href="/cmpe275/user/logout">Logout</a></li>
		</ul>
</div>

<div class="container container-table">
<c:if test="${not empty cart}">
<table class="table table-condensed shoppingtable">
  <thead>
    <tr>
      <th style="color:white">Item</th>
      <th style="color:white">Quantity</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach items="${cart}" var="menu">
    <tr>
      <td style="color:white">${menu.menuName}</td>
      <td style="color:white">${menu.quantity}</td>
    </tr>
    </c:forEach>
  </tbody>
</table>



  <form class="form-signin">
   <div class="checkbox" style="color:white">
    <label>
      <input id="checkbox" type="checkbox" > Pick Manual Time
    </label>
  </div>
  <input class="form-control" id="time" type="time" name="usr_time" disabled  max="21:00"><br>
  <input class="form-control" id="date" type="date" name="bday" min="2000-01-02" max="2016-06-11" disabled><br>
       <button id="order" class="btn btn-lg btn-primary btn-block" type="button"> 
        Order 
       </button>
       </form>
       </c:if>
       <c:if test="${empty cart}">
       <form class="form-signin">
       <p class="paragraph" style="color:white">Please place some orders</p>
       </form>
       </c:if>
       </div>
</body>
</html>