<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <spring:url value="/resources/assets/css/bootstrap.min.css" var="mainCss" />
	<link href="${mainCss}" rel="stylesheet">
	
	<spring:url value="/resources/assets/css/style.css" var="styleCss"/>
	<link href="${styleCss}" rel="stylesheet">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
	<spring:url value="/resources/assets/js/bootstrap.min.js" var="mainJs"/>
	<script src="${mainJs}"></script>
</head>
<body>

<div class="container">
  <h2>List of MenuItems</h2>
  <div class="list-group">
  <c:forEach items="${list}" var="menu">
    <a  href="/cmpe275/items/${menu.menuId}" class="list-group-item">
      <img class="img-thumbnail icon" src="/cmpe275/items/${menu.menuId}/picture"></img>
      <div class="details">
      <h4 class="list-group-item-heading">${menu.name}</h4>
      <p class="list-group-item-text">${menu.category}</p>
      </div>
    </a>
    </br>
    </c:forEach>
  </div>
</div>

</body>
</html>