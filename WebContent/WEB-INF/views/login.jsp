<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!doctype html>

<title>Order Management System</title>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="assets/css/bootstrap.min.css" rel="stylesheet">

<link href="assets/css/style.css" rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
</head>
<body>
	<div class="container">
		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" href="#Register">Register</a></li>
			<li><a data-toggle="tab" href="#Login">Login</a></li>
		</ul>
		<div class="tab-content">

			<div id="Login" class="tab-pane fade">
				<form class="form-signin" method="post">
					<input id="inputEmail" name="inputEmail" class="form-control"
						type="email" placeholder="Email address" maxlength="50" required
						autofocus /> <input id="inputPassword" name="inputPassword"
						class="form-control" type="password" placeholder="Password"
						maxlength="10" minlength="4" required>
					<button type="submit" class="btn btn-lg btn-primary btn-block">Login</button>
				</form>
			</div>
			<div id="message">
				<h3>${message}</h3>
			</div>
		</div>
	</div>
</body>