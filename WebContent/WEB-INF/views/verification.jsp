<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!doctype html>
<html>
<head>
<title>Order Management System</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="assets/css/bootstrap.min.css" rel="stylesheet">

<link href="assets/css/style.css" rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
</head>
<body>
	<h3>Verification Page</h3>
	<div class="container">
		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" href="#Register">Register</a></li>
			<li><a data-toggle="tab" href="#Login">Login</a></li>
		</ul>
		<div class="tab-content">

			<div id="Login" class="tab-pane fade">
			<c:url var="addAction" value="/user/verification" ></c:url>
				<form class="form-signin" method="post" action="${addAction}">
					<input id="inputEmail" name="inputEmail" class="form-control"
						type="email" placeholder="Email address" maxlength="50" value=${email}  required
						autofocus /> <input id="inputVerificationCode" name="inputVerificationCode"
						class="form-control" type="text" placeholder="Verification Code"
						maxlength="10" minlength="6" required>
					<button type="submit" class="btn btn-lg btn-primary btn-block">Verify</button>
				</form>
			</div>
			<div id="message">
				<p>Please input verification code sent to your email address.</p>
			</div>
		</div>
	</div>
</body>
</html>