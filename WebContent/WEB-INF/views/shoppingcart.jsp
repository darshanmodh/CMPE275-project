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
		});
	});
});
</script>
</head>
<body>
<div class="container container-table">
<c:if test="${not empty cart}">
<table class="table table-condensed shoppingtable">
  <thead>
    <tr>
      <th>Item</th>
      <th>Quantity</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach items="${cart}" var="menu">
    <tr>
      <td>${menu.menuName}</td>
      <td>${menu.quantity}</td>
    </tr>
    </c:forEach>
  </tbody>
</table>



  <form class="form-signin">
   <div class="checkbox">
    <label>
      <input id="checkbox" type="checkbox"> Pick Manual Time
    </label>
  </div>
  <input class="form-control" id="time" type="time" name="usr_time" disabled>
  <input class="form-control" id="date" type="date" name="bday" min="2000-01-02" disabled>
       <button id="order" class="btn btn-lg btn-primary btn-block" type="button"> 
        Order 
       </button>
       </form>
       </c:if>
       <c:if test="${empty cart}">
       <form class="form-signin">
       <p class="paragraph">Please place some orders</p>
       </form>
       </c:if>
       </div>
</body>
</html>