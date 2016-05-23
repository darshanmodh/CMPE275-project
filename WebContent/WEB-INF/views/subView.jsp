<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script type="text/javascript">
function readURL(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    $('#picture')
                        .attr('src', e.target.result)
                        .width(200)
                        .height(200);
                        document.getElementById("divId").value = e.target.result.split(",")[1];
                };
                reader.readAsDataURL(input.files[0]);
            }
            else
            {
   	        $('#picture')
                        .width(0)
                        .height(0);
   	         document.getElementById("picture").style.visibility="hidden";
			}
        }
</script>

<script>
	// function uploadFormData(menuId){
	//   var MyForm = new FormData();
	//   MyForm.append("file", inputFileToLoad.files[0]);
	//   $.ajax({
	//     url: "upload/"+menuId,
	//     data: MyForm,
	//     dataType: 'text',
	//     processData: false,
	//     contentType: false,
	//     type: 'POST',
	//     success: function(data){
	//       console.log("Picture uploaded successfully.");
	//       //window.location.href = "/cmpe275/items/viewall";
	//     }
	//   });
	// }

// 	function testing(menuId) {
// 		var menuitem = {};
// 		menuitem["name"] = $("#name").val();
// 		menuitem["category"] = $("#category").val();
// 		menuitem["unitPrice"] = $("#price").val();
// 		menuitem["calories"] = $("#calories").val();
// 		menuitem["prepTime"] = $("#prepTime").val();
// 		//menuitem["picture"] = $("#inputFileToLoad").val();
// 		//console.log("In console = " + menuitem["picture"]);
// 		$.ajax({
// 			type : "POST",
// 			contentType : "application/json",
// 			url : "update/" + menuId,
// 			data : JSON.stringify(menuitem),
// 			success : function(response) {
// 				console.log("SUCCESS");
// 				window.location.href = "/cmpe275/items/viewall";
// 			},
// 			error : function(e) {
// 				console.log("ERROR : ", e)
// 			}
// 		});
// 	}
</script>

<!-- Modal Body -->
<form id="form2" method="post"
	action="/cmpe275/items/update/${menuItem.menuId}" class="form-horizontal" role="form">
	<div class="modal-body">
		<div class="form-group">
			<label class="col-sm-2 control-label" for="name">Name</label>
			<div class="col-sm-10">
				<input id="name" class="form-control" type="text" name="name"
					required autofocus value="${menuItem.name}">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="category">Category</label>
			<div class="col-sm-10">
				<select id="category" name="category" required class="form-control">
					<c:set var="dbCategory" value="${menuItem.category}" />
					<%
						String resp = (String) pageContext.getAttribute("dbCategory");
					%>
					<option value="" selected="selected" disabled="disabled">Select
						a category <span class="arrow">&#9661;</span></option>
					<option value="appetizer"
						<%if (resp.equals("appetizer"))
				out.println("selected");%>>Appetizer</option>
					<option value="mainCourse"
						<%if (resp.equals("mainCourse"))
				out.println("selected");%>>Main
						Course</option>
					<option value="desert"
						<%if (resp.equals("desert"))
				out.println("selected");%>>Desert</option>
					<option value="drink"
						<%if (resp.equals("drink"))
				out.println("selected");%>>Drink</option>
				</select>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="unitPrice">Unit
				Price</label>
			<div class="col-sm-10">
				<input id="price" class="form-control" type="text"
					pattern="[0-9]*.[0-9]|[0-9]*" name="unitPrice" required
					value="${menuItem.unitPrice}">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="calories">Calories</label>
			<div class="col-sm-10">
				<input id="calories" name="calories" class="form-control"
					type="number" min="0" required value="${menuItem.calories}">
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="calories">Preparation
				Time</label>
			<div class="col-sm-10">
				<input id="prepTime" name="prepTime" class="form-control"
					type="number" style="margin-bottom: 50px"
					value="${menuItem.prepTime}" min="1" max="10" required>
			</div>
		</div>
		<div class="form-group">
			<label class="col-sm-2 control-label" for="calories">Picture</label>
			<div class="col-sm-10">
				<img name="picture" id="picture" height="200"
					width="200" src="/cmpe275/items/${menuItem.menuId}/picture"
					alt="your image" />
			</div>
		</div>
	</div>

	<!-- Modal Footer -->
	<div class="modal-footer">
		<button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
		<input type="submit" name="submit" value="Save Changes"
			class="btn btn-info" />
		<!-- 	<button type="button" class="btn btn-info" -->
		<%-- 		onclick="testing(${menuItem.menuId});">Save Changes</button> --%>
		<%-- <form class="form-signin" method="post" action="/cmpe275/items/update/${menuItem.menuId}"> --%>
		<!-- 	<button type="button" class="btn btn-info">Save Changes</button> -->
		<%-- </form> --%>

		<%-- <form id="form2" method="post" action="/cmpe275/upload" enctype="multipart/form-data"> --%>
		<!--   <input name="file2" id="file2" type="file" /><br/> -->
		<%-- </form> --%>
		<!-- <button value="Submit" onclick="uploadFormData()" >Upload</button> -->

		<input type="hidden" id="divId" name="picture">
	</div>
</form>