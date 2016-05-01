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
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
	<script src="assets/js/bootstrap.min.js" type="text/javascript">
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
			<div id="Register" class="tab-pane fade in active">
				<form class="form-signin">
					<input id="inputEmail" class="form-control" type="email" placeholder="Email address" maxlength="50" required autofocus>
					<input id ="inputPassword" class="form-control" type="password" placeholder="Password" maxlength="10" minlength="6" required >
					<input id="confirmPassword" class="form-control" type="password" placeholder="Confirm Password" required>
					<button type="submit" class="btn btn-lg btn-primary btn-block">Register</button>
				</form>
			</div>
			<div id="Login" class="tab-pane fade">
				<form class="form-signin">
					<input id="inputEmail" class="form-control" type="email" placeholder="Email address" maxlength="50" required autofocus>
					<input id ="inputPassword" class="form-control" type="password" placeholder="Password" maxlength="10" minlength="6" required >
					<button type="submit" class="btn btn-lg btn-primary btn-block">Login</button>
				</form>
			</div>
		</div>
	</div>
</body>