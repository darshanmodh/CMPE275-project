<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>My Orders</title>
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
	<%
		String user = null;
		if (session.getAttribute("user") != null) {
			user = (String) session.getAttribute("user");
		}
	%>
	<div class="container" style="color: white;">
		<ul class="nav nav-tabs">
			<li><a href="/cmpe275/user/login"><%=user%></a></li>
			<li><a href="/cmpe275/items/viewall">Menu</a></li>
			<li class="active"><a href="/cmpe275/orders">Orders</a></li>
			<li><a href="/cmpe275/user/logout">Logout</a></li>
		</ul>
		<h2>Orders</h2>
		<table class="table">
			<thead>
				<tr>
					<th>Order Id</th>
					<th>Preparation Date</th>
					<th>Order Time</th>
					<th>Pick Up Date</th>
					<th>Pick Up Time</th>
					<th>Status</th>
					<th>Contents</th>
					<th>Total Price</th>
					<th>Cancel</th>
				</tr>
			</thead>
			<c:forEach items="${myOrders}" var="orders">
				<tbody>
					<tr>
						<td>${orders.orderId}</td>
						<td>${orders.prepDate}</td>
						<td>${orders.orderTime}</td>
						<td>${orders.pickUpDate}</td>
						<td>${orders.pickupTime}</td>
						<td>${orders.status}</td>
						<td><a href="/cmpe275/orders/${orders.orderId}"
							target="_blank" class="btn btn-primary btn-sm" role="button">View</a></td>
						<td>${orders.totalPrice }</td>
						<c:if test="${orders.status=='Not yet started'}">
							<td>
									<p data-placement="top" data-toggle="tooltip" title="Delete">
										<button class="btn btn-danger btn-xs" data-title="Delete"
											data-toggle="modal" data-target="#delete" data-href="/cmpe275/orders/${orders.orderId}">
											<span class="glyphicon glyphicon-trash"></span>
										</button>
									</p>
							</td>
						</c:if>
					</tr>

				</tbody>
			</c:forEach>
		</table>
	</div>
	<div class="modal fade" id="delete" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">

				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">Confirm Delete</h4>
				</div>

				<div class="modal-footer">
					<form method="POST" id="del">
					<input type="hidden" name="_method" value="DELETE">
					<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
											
					<button type="submit" class="btn btn-danger btn-ok pull-right">Delete</button>
					</form>
				</div>
			</div>
		</div>
	</div>


</body>
<script>
	$('#delete').on(
			'show.bs.modal',
			function(e) {
				$(this).find('#del').attr('action',
						encodeURI($(e.relatedTarget).data('href')));

			});
</script>
</html>