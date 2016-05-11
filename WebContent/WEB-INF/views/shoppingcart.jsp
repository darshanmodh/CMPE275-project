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

</head>
<body>
<div class="container container-table">
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
  <form method="post" action="/cmpe275/items/orderNow">
  <input type="time" name="usr_time">
  <input type="date" name="bday" min="2000-01-02">
       <button class="btn btn-info btn-lg pull-right" role="button"> 
        Order 
       </button>
       </form>
</div>
</body>
</html>