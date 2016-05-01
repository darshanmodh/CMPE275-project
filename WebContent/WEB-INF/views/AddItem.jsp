<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>

<!DOCTYPE HTML>
<html>
	<head>
		<title>Takeout order system</title>
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
    $("#update").click(function(){
    	var query ="firstname="+encodeURIComponent($("#firstname").val())+
    	"&lastname="+encodeURIComponent($("#lastname").val())+"&email=" + encodeURIComponent($("#email").val())+
    			"&address="+encodeURIComponent($("#address").val())+
    			"&organization="+encodeURIComponent($("#organization").val())+"&aboutMyself="
    			+encodeURIComponent($("#aboutMyself").val());
        $.ajax({
        	url:$("#id").val()+"?"+query,
			method:"post",
			success: function(data) {
				var temp = $("<div/>");
				temp.append(data);
				var myvalue = temp.find("input[id='id']").val();
				window.location.href = myvalue;
			}
			
        });
    });
});
</script>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<!--[if lte IE 8]><script src="${shiv.js}"></script><![endif]-->
		
		<spring:url value="/resources/assets/css/main.css" var="mainCss" />
		<spring:url value="/resources/assets/css/ie9.cs" var="ie9" />
		<spring:url value="/resources/assets/css/ie8.css" var="ie8" />
		<spring:url value= "assets/js/ie/html5shiv.js" var="shiv.js"/>
			
			
		<link href="${mainCss}" rel="stylesheet" />
		
		<!--[if lte IE 9]><link href="${ie9}" rel="stylesheet" /><![endif]-->
		<!--[if lte IE 8]><link href="${ie8}" rel="stylesheet" /><![endif]-->
	</head>
	<body>

		<!-- Sidebar -->
			<section id="sidebar">
				<div class="inner">
					<nav>
						<ul>
								<li><a href="">Profile Manager</a></li>
						</ul>
					</nav>
				</div>
			</section>
 
		<!-- Wrapper -->
			<div id="wrapper">

				


				<!-- Three -->
					<section id="three" class="wrapper style1 fade-up">
						<div class="inner">
							<h2>Profile Details</h2>
							<div >
								<section>
									<form method="post" action="#">
									<div class="field half first">
											<label for="name">ID</label>
											<input type="text"id="id" />
										</div>
										<div class="field half">
											<label for="name">Name</label>
											<input type="text"  id="firstname" />
										</div>
										<div class="field half first">
											<label for="email">Picture</label>
											<input type="file" id="picture" />
										</div>
										<div class="field half">
											<label for="name">Price</label>
											<input type="text"  id="price" />
										</div>
										<div class="field half first">
											<label for="email">Calories</label>
											<input type="text" id="calories" />
										</div>
										<div class="field half">
											<label for="name">Preparation time</label>
											<input type="text"  id="prepTime" />
										</div>
										
										<ul class="actions">
											<!--<li><a href="" class="button submit">Create</a></li>-->
											<form method="post" action="${id}">
											<input type="hidden" name="_method" value="DELETE">
											<input type="button" class="button submit" value="Add Item" id="update"/>
											</form>
										</ul>
									</form>
								</section>
								
							</div>
						</div>
					</section>

			</div>

		<!-- Footer -->
			<footer id="footer" class="wrapper style1-alt">
				<div class="inner">
					<ul class="menu">
						<li></li>
					</ul>
				</div>
			</footer>

		<!-- Scripts -->
			<spring:url value= "assets/js/jquery.min.js" var="min.js"/>
		    <spring:url value= "assets/js/jquery.scrollex.min.js" var="scrollex.min.js"/>
		    <spring:url value= "assets/js/jquery.scrolly.min.js" var="scrolly.min.js"/>
		    <spring:url value= "assets/js/skel.min.js" var="skel.min.js"/>
		    <spring:url value= "assets/js/util.js" var="util.js"/>
		    <spring:url value= "assets/js/main.js" var="main.js"/>
		    <spring:url value= "assets/js/ie/respond.min.js" var="respond.min.js"/>
			<script src="${min.js}"></script>
			<script src="${scrollex.min.js}"></script>
			<script src="${scrolly.min.js}"></script>
			<script src="${skel.min.js}"></script>
			<script src="${util.js}"></script>
			<!--[if lte IE 8]><script src="${respond.min.js}"></script><![endif]-->
			<script src="${main.js}"></script>

	</body>
</html>

