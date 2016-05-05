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
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
	<spring:url value="/resources/assets/js/bootstrap.min.js" var="mainJs"/>
	<script src="${mainJs}"></script>
		
	<spring:url value="/resources/assets/css/style.css" var="styleCss"/>
	<link href="${styleCss}" rel="stylesheet">
</head>
<body>

<div class="row">
  <c:forEach items="${list}" var="menu">
  <div class="col-lg-3 col-md-4 col-xs-6 thumb">
  <div class="thumbnail">
    <a  href="/cmpe275/items/${menu.menuId}">
      <img class="img-rounded" src="/cmpe275/items/${menu.menuId}/picture"></img>
    </a>
       <div class="caption">
    		<h3>${menu.name}</h3>
    		<h4>${menu.category}</h4>
       </div>
       <form method="post" action="/cmpe275/items/delete/${menu.menuId}">
       <button class="btn btn-danger" role="button"> 
       <span class="glyphicon glyphicon-remove"></span> Remove 
       </button>
       </form>
    </div>
  </div>
    </c:forEach>
</div>

</body>
</html>