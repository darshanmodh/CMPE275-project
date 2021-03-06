<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!doctype html>

<title>Order Management System</title>
<head>
<%
try {
if (session.getAttribute("user")!=null) {
	response.sendRedirect("/cmpe275/user/login");
}
} catch(NullPointerException e) {
	response.sendRedirect("registration");
}
%>
	<meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
  
    <spring:url value="/resources/assets/css/bootstrap.min.css" var="mainCss" />
	<link href="${mainCss}" rel="stylesheet">
	
	<spring:url value="/resources/assets/css/style.css" var="styleCss"/>
	<link href="${styleCss}" rel="stylesheet">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
	<spring:url value="/resources/assets/js/bootstrap.min.js" var="mainJs"/>
	<script src="${mainJs}"></script>
	<script type="text/javascript">
		window.onload = function () {
			document.getElementById("inputPassword").onchange = validatePassword;
			document.getElementById("confirmPassword").onchange = validatePassword;
		}
		function validatePassword(){
		var confPass = document.getElementById("confirmPassword").value;
		var pass = document.getElementById("inputPassword").value;
		if(confPass!= pass)
			document.getElementById("confirmPassword").setCustomValidity("Passwords Don't Match");
		else
			document.getElementById("confirmPassword").setCustomValidity('');	 
		}

	</script>
</head>
<body>
	<div class="container">
		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" href="#Register">Register</a></li>
			<li><a data-toggle="tab" href="#Login">Login</a></li>
		</ul>
		<div class="tab-content">
			<div id="Register" class="tab-pane fade in active" >
				<form class="form-signin" method="post" action="/cmpe275/user/register">
					<input id="inputEmail" name="email" class="form-control" type="email" placeholder="Email address" maxlength="50" required autofocus>
					<input id ="inputPassword" name="password" class="form-control" type="password" placeholder="Password" maxlength="10" minlength="6" required >
					<input id="confirmPassword" name="cpassword" class="form-control" type="password" placeholder="Confirm Password" required>
					<button type="submit" class="btn btn-lg btn-primary btn-block">Register</button>
				</form>
				
				
				
				<c:if test="${message!=null}">
				<div id="message" class="alert alert-info fade in">
			<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
			
								<p>${message}</p>
						</div>
				</c:if>
				
				
			</div>
			<div id="Login" class="tab-pane fade">
				<form class="form-signin" method="post" action="/cmpe275/user/login">
					<input id="inputEmail" name="inputEmail" class="form-control"
						type="email" placeholder="Email address" maxlength="50" required
						autofocus /> 
						<input id="inputPassword" name="inputPassword"
						class="form-control" type="password" placeholder="Password"
						maxlength="10" minlength="4" required>
					<button type="submit" class="btn btn-lg btn-primary btn-block">Login</button>
				</form>
			</div>
		</div>
	</div>
</body>