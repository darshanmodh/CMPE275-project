<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html>
<html>
<head>
<title>Order Management System</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<spring:url value="/resources/assets/css/bootstrap.min.css"
	var="mainCss" />
<link href="${mainCss}" rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<spring:url value="/resources/assets/js/bootstrap.min.js" var="mainJs" />
<script src="${mainJs}"></script>

<spring:url value="/resources/assets/css/style.css" var="styleCss" />
<link href="${styleCss}" rel="stylesheet">

<script>
$(document).ready(function() {
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1; //January is 0!
	var yyyy = today.getFullYear();

	if(dd<10) {
	    dd='0'+dd
	} 

	if(mm<10) {
	    mm='0'+mm
	} 

	today = mm+'-'+dd+'-'+yyyy;
    $("#date").attr("min",today);
$('#checkbox').change(function() {
	   if($(this).is(":checked")) {
		   $("#time").removeAttr("disabled");
		     $("#date").removeAttr("disabled");
	   }
	   else {
		   $("#time").attr("disabled","disabled");
		     $("#date").attr("disabled","disabled");
	   }
	});
	
	
	
	$("#order").click(function(){
		
		if($('#checkbox').is(":checked"))
			{
			
			console.log($('#date').val());
			console.log($('#time').val());
			$.ajax({
				type:"post",
				url:"/cmpe275/items/orderNow?dop="+$('#date').val()+"&pickupTime="+$('#time').val()+":00",
				success:function(response) {
					if (response.success == 'auto order placed' || response.success == 'manual order placed') {
						console.log(response);
						alert(response.msg);
						window.location.href = '/cmpe275/items/viewall';
					}
					if (response.success == 'cannot place') {
						alert(response.msg);
					}
				}
			});
			}
		else
		{
		$.ajax({
			type:"post",
			url:"/cmpe275/items/orderNow",
			success:function(response) {
				if (response.success == 'auto order placed' || response.success == 'manual order placed') {
					console.log(response);
					alert(response.msg);
					window.location.href = '/cmpe275/items/viewall';
				}
				if (response.success == 'cannot place') {
					alert(response.msg);
				}
			}
		});
		}
		
	});
});
</script>
</head>
<body>
<%
		String user = null;
		if (session.getAttribute("user") != null && session.getAttribute("role").equals('U')) {
			user = (String) session.getAttribute("user");
		} else {
			response.sendRedirect("/cmpe275/user/login");
		}
%>
<div class="container">
		<ul class="nav nav-tabs">
			<li class="active"><a href="/cmpe275/user/login"><%=user%></a></li>
			<li><a href="/cmpe275/items/viewall">Menu</a></li>
			<li><a href="/cmpe275/orders/<%session.getAttribute("user");%>">Orders</a></li>
			<li><a href="/cmpe275/user/logout">Logout</a></li>
		</ul>
</div>

<div class="container container-table">
<c:if test="${not empty cart}">
<table class="table table-inverse shoppingtable">
  <thead>
    <tr>
      <th style="color:white">Item</th>
      <th style="color:white">Quantity</th>
      <th style="color:white">Edit</th>
      <th style="color:white">Delete</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach items="${cart}" var="menu">
    <tr >
      <td style="color:white">${menu.menuName}</td>
      <td style="color:white">${menu.quantity}</td>
      <td><p data-placement="top" data-toggle="tooltip" title="Edit">
      <button class="btn btn-primary btn-xs" data-title="Edit" data-toggle="modal" data-target="#edit" data-menuname="${menu.menuName}" data-quantity="${menu.quantity }" data-href="/cmpe275/items/cart"><span class="glyphicon glyphicon-pencil"></span></button></p></td>
    <td><p data-placement="top" data-toggle="tooltip" title="Delete"><button class="btn btn-danger btn-xs" data-href="/cmpe275/items/cart/${menu.menuName }" data-toggle="modal" data-target="#delete"  ><span class="glyphicon glyphicon-trash"></span></button></p></td>
    
    </tr>
    </c:forEach>
  </tbody>
</table>



  <form class="form-signin">
   <div class="checkbox" style="color:white">
    <label>
      <input id="checkbox" type="checkbox" > Pick Manual Time
    </label>
  </div>
  <input class="form-control" id="time" type="time" name="usr_time" disabled  max="21:00" required><br>
  <input class="form-control" id="date" type="date" name="bday" disabled required><br>
       <button id="order" class="btn btn-lg btn-primary btn-block" type="button"> 
        Order 
       </button>
       </form>
       <form class="form-signin" action="/cmpe275/items/cart/cancel" method="POST">
       <button id="cancel" class="btn btn-lg btn-primary btn-block" type="submit"> 
        Cancel 
       </button>
       </form>
       
       </c:if>
       <c:if test="${empty cart}">
       <form class="form-signin">
       <p class="paragraph" style="color:white">Please place some orders</p>
       </form>
       </c:if>
       </div>
           <div class="modal fade" id="delete" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
            
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">Confirm Delete</h4>
                </div>
            
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
                    <a id="del" class="btn btn-danger btn-ok">Delete</a>
                </div>
            </div>
        </div>
    </div>
    
    <div class="modal fade" id="edit" tabindex="-1" role="dialog" aria-labelledby="edit" aria-hidden="true">
      <div class="modal-dialog">
    <div class="modal-content">
          <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button>
        <h4 class="modal-title custom_align" id="Heading">Edit Your Detail</h4>
      </div>
      <form method="post" id="formupdate">
          <div class="modal-body">
          <div class="form-group">
        <input class="form-control " name="menuName" id="mName" type="text" readonly>
        </div>
        <div class="form-group">
        <input id="quantity" name="quantity" class="form-control" type="number"
							placeholder="Quantity" min="1" max="100"  required> 
        </div>
        </div>
          <div class="modal-footer ">
        <button type="submit" class="btn btn-warning btn-lg" style="width: 100%;"><span class="glyphicon glyphicon-ok-sign"></span> Update</button>
      </div>
      </form>
        </div>
    <!-- /.modal-content --> 
  </div>
      <!-- /.modal-dialog --> 
    </div>
    
    
       
</body>
<script>
        $('#delete').on('show.bs.modal', function(e) {
            $(this).find('#del').attr('href', encodeURI($(e.relatedTarget).data('href')));
            
             });
        
        $('#edit').on('show.bs.modal', function(e) {
            $(this).find('#mName').attr('value', $(e.relatedTarget).data('menuname'));
            $(this).find('#quantity').attr('value', $(e.relatedTarget).data('quantity'));
            $(this).find('#formupdate').attr('action', $(e.relatedTarget).data('href'));
            
             });
    </script>
</html>