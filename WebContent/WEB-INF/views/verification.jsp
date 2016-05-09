<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!doctype html>
<html>
<head>
<title>Order Management System</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<spring:url value="/resources/assets/css/bootstrap.min.css"
	var="mainCss" />
<link href="${mainCss}" rel="stylesheet">

<spring:url value="/resources/assets/css/style.css" var="styleCss" />
<link href="${styleCss}" rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.2/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
</head>
<body>
	<h3 class="whiteTextOverride">Verification Page</h3>
	<div class="container">
		<form class="form-signin" method="post"
			action="/cmpe275/user/verification">
			<input id="inputEmail" name="inputEmail" class="form-control"
				type="email" placeholder="Email address" maxlength="50" required
				autofocus /> <input id="inputVerificationCode"
				name="inputVerificationCode" class="form-control" type="text"
				placeholder="Code" maxlength="10" minlength="4" required>
			<button type="submit" class="btn btn-lg btn-primary btn-block">Login</button>
		</form>
		<div id="message" class="alert alert-info fade in">
			<a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
			<p>Please input verification code sent to your email address.</p>
			<p>${message}</p>
		</div>
	</div>
</body>
</html>