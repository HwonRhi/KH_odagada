<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<jsp:include page="/WEB-INF/views/common/header.jsp">
   <jsp:param value="오다가다 타는 카풀" name="pageTitle"/>
</jsp:include>
<style>
	div#review-container {
	width: 500px;
	margin: 0 auto;
	}
	div#review-container ul{
	list-style:none;
	padding-left:0px;
	}
	div#review-container h3{
	text-align:center;
	}
	div#review-context{
	border: 1px solid #DDD;
    border-radius: 16px;
    height: 180px;
    margin-bottom: 10px;
    padding:10px;
    height: auto;
	}
	div#writerNum{
	text-align: left;
	margin-bottom:5px;
	}
	span#review-content{
	text-align: Left;
	}
	#rContent{
	width:460px;
	}
	
.star-input>.input, .star-input>.input>label:hover, .star-input>.input>input:focus+label,
	.star-input>.input>input:checked+label {
	display: inline-block;
	vertical-align: top;
	background:url(${pageContext.request.contextPath}/resources/images/star1.png)no-repeat;
}

.star-input {
	white-space: nowrap;
}

.star-input>.input {
	display: inline-block;
	width: 100px;
	background-size: 100px;
	height: 19px;
	white-space: nowrap;
	overflow: hidden;
	position: relative;
}

.star-input>.input>input {
	position: absolute;
	width: 1px;
	height: 1px;
	opacity: 0;
}

.star-input>.input.focus {
	outline: 1px dotted #ddd;
}

.star-input>.input>label {
	width: 10px;
	height: 0;
	padding: 19px 0 0 0;
	overflow: hidden;
	float: left;
	cursor: pointer;
	position: absolute;
	top: 0;
	left: 0;
}

.star-input>.input>label:hover, .star-input>.input>input:focus+label,
	.star-input>.input>input:checked+label {
	background-size: 100px;
	background-position: 0 bottom;
}

.star-input>.input>label:hover ~label{
	background-image: none;
}

.star-input>.input>label[for="p1"] {
	width: 10px;
	z-index: 10;
}

.star-input>.input>label[for="p2"] {
	width: 20px;
	z-index: 9;
}

.star-input>.input>label[for="p3"] {
	width: 30px;
	z-index: 8;
}

.star-input>.input>label[for="p4"] {
	width: 40px;
	z-index: 7;
}

.star-input>.input>label[for="p5"] {
	width: 50px;
	z-index: 6;
}

.star-input>.input>label[for="p6"] {
	width: 60px;
	z-index: 5;
}

.star-input>.input>label[for="p7"] {
	width: 70px;
	z-index: 4;
}

.star-input>.input>label[for="p8"] {
	width: 80px;
	z-index: 3;
}

.star-input>.input>label[for="p9"] {
	width: 90px;
	z-index: 2;
}

.star-input>.input>label[for="p10"] {
	width: 100px;
	z-index: 1;
}

.star-input>output {
	display: inline-block;
	width: 36px;
	text-align: right;
}

.star-input>output>b {
	font: bold 18px Helvetica, Arial, sans-serif;
	vertical-align: middle;
}
</style>

<div id="review-container">
	<form name="reviewFrm" action="${path}/community/reviewModifyEnd.do" method="post" onsubmit="return validate();"> 
	<h3>내가 작성한 리뷰</h3>
		<div style="overflow:scroll; height:500px;">
			<%-- <c:forEach items="${list}" var="r"> --%>
			<ul id="review-list">
				<div id="review-context">
					<div class="review-content" id="writerNum">아이디</div>
						 <span class="star-input">
						<span class="input">
							<input class="form-control" type="radio" name="rGrade" id="p1" value="0.5"><label for="p1">0.5</label>
							<input class="form-control" type="radio" name="rGrade" id="p2" value="1"><label for="p2">1</label>
							<input class="form-control" type="radio" name="rGrade" id="p3" value="1.5"><label for="p3">1.5</label>
							<input class="form-control" type="radio" name="rGrade" id="p4" value="2"><label for="p4">2</label>
							<input class="form-control" type="radio" name="rGrade" id="p5" value="2.5"><label for="p5">2.5</label>
							<input class="form-control" type="radio" name="rGrade" id="p6" value="3"><label for="p6">3</label>
							<input class="form-control" type="radio" name="rGrade" id="p7" value="3.5"><label for="p7">3.5</label>
							<input class="form-control" type="radio" name="rGrade" id="p8" value="4"><label for="p8">4</label>
							<input class="form-control" type="radio" name="rGrade" id="p9" value="4.5"><label for="p9">4.5</label>
							<input class="form-control" type="radio" name="rGrade" id="p10" value="5"><label for="p10">5</label>
						</span>
						<!-- <output for="star-input"><b>0</b>점</output> -->
						</span>
						<div id="review-content">
							<textarea id="rContent" name="rContent"><c:out value="${review.RCONTENT}"/></textarea>
							<br/>
							<input type="submit" class="btn btn-outline-success" value="수정" style="margin-left:340px;">
							<input type="reset" class="btn btn-outline-success" value="취소" onclick="javascript:location.href='${path}/community/myReviewView';">
						</div>
					</div>
			</ul>
		<%-- </c:forEach> --%>
		</div>
	</form>
</div>
<script>
	/*  별점 */
	var starRating = function(){
	var $star = $(".star-input"),
	    $result = $star.find("output>b");
		
	  	$(document)
		.on("focusin", ".star-input>.input", 
			function(){
	   		 $(this).addClass("focus");
	 	})
			 
	   	.on("focusout", ".star-input>.input", function(){
	    	var $this = $(this);
	    	setTimeout(function(){
	      		if($this.find(":focus").length === 0){
	       			$this.removeClass("focus");
	     	 	}
	   		}, 100);
	 	 })
	  
	    .on("change", ".star-input :radio", function(){
	    	$result.text($(this).next().text());
	  	})
	    .on("mouseover", ".star-input label", function(){
	    	$result.text($(this).text());
	    })
	    .on("mouseleave", ".star-input>.input", function(){
	    	var $checked = $star.find(":checked");
	    		if($checked.length === 0){
	     	 		$result.text("0");
	   		 	} else {
	     	 		$result.text($checked.next().text());
	    		}
	  	});
	};
	starRating();
	
	function validate()
	{
		var content = $("[name=reviewContent]").val();
		if(content.trim().length==0)
		{
			alert("내용을 입력하세요");
			return false;
		}
		return true;
	}
</script>
	
<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>