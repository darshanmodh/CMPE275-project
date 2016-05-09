<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!doctype html>
<head>
<title>Admin Module</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<spring:url value="/resources/assets/css/bootstrap.min.css"
	var="mainCss" />
<link href="${mainCss}" rel="stylesheet">

<spring:url value="/resources/assets/css/style.css" var="styleCss" />
<link href="${styleCss}" rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js" > </script>
<spring:url value="/resources/assets/js/bootstrap.min.js" var="mainJs" />
<script src="${mainJs}"></script>
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
			<li class="active"><a data-toggle="tab" href="#"><%=user%></a></li>
			<li><a data-toggle="tab" href="#">Menu</a></li>
			<li><a data-toggle="tab" href="#">Add Menu Item</a></li>
			<li><a data-toggle="tab" href="#">Delete Menu Item</a></li>
			<li><a href="/cmpe275/user/logout">Logout</a></li>
		</ul>
		<div class="tab-content">
			<div id="Register" class="tab-pane fade in active">
				<div id="Logout" class="tab-pane fade"></div>
			</div>
		</div>
	</div>
</body>