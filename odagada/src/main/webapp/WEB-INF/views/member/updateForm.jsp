<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*,com.spring.odagada.member.model.vo.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
	Member m = (Member) request.getAttribute("logined");
    String eDate []=m.getEnrollDate().split(" ");
%>

<c:set var="path" value="${pageContext.request.contextPath}"/>
<jsp:include page="/WEB-INF/views/common/header.jsp">
   <jsp:param value="오다가다 타는 카풀" name="pageTitle"/>
</jsp:include>
<script src="https://code.jquery.com/jquery-3.3.1.min.js" integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8=" crossorigin="anonymous"></script>



  <style>
     div#enroll-container{width:850px; margin:0 auto; text-align:center;}
     div#enroll-container input, div#enroll-container select {margin-bottom:10px;}
  
    /*비밀번호 체크 */
    div#enroll-container span.ck {display:none;font-size: 12px;position:absolute; top:58px; right:10px;}
    div#enroll-container span.ckOk{color:green;}
    div#enroll-container span.ckNo{color:red;}
    
    .ckOk, .ckNo{padding-right:13px; }
    .phone{margin-left:0; margin-right:0;}
    #pro_img{width: 400px; height: auto;}
    .profileDiv{padding-right:0; width:100px;}
    .pPassword{font-size:13px; margin-top:1px;}
  	.passwordInfo{height:40px; align:left;}
  	.oriEmail{font-size:20px; text-align:left;}
  	.oriPhone{font-size:20px; text-align:left;}
  	.alert{padding:0; }
  	.cg {font-size:13px;}
  	.btns{padding-top:3px; padding-right:0; padding-left:0;}
  	#chgMailInfoP{margin-top:10px; margin-bottom:15px;}
    .chgBtnDiv{padding-right:15px;}
    #chgEmailBtn{width:117px;}
    #sentEmailDiv, #sentTxtDiv, #sentPhoneDiv{margin-bottom:20px;}  
    ul{list-style:none;} 
    </style>
    
     
<script>

 $(function(){
	 //파일 업로드 함수
   $('[name=upFile]').on('change',function(){
        var filename=this.files[0].name;
   		var fileReader = new FileReader();
 	
 		fileReader.readAsDataURL(this.files[0]);
 		fileReader.onload = function(e){
	 		var result = e.target.result;	
	 		$('#pro_img').attr('src',result); 		
 		};

      $(this).next('.custom-file-label').html(filename);
      console.log(filename);
   });  
  
   //프로필 사진 클릭시 파일 업로드 가능하게 하는 이벤트.
	$('#pro_img').on('click', function() {
			$('#upFile').trigger('click');			
		})	 
});

 //비밀번호 유효성 검사
 function passwordCheck(password){
    var pw=$(password).val();
    var ckPw =/^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{6,15}$/;
      if(!ckPw.test(pw)){        
         alert('숫자,영문자,특수문자 조합으로 6~15자');
         $('#password1').val('').focus();
         return false;
     }                  
     return true;
 }

	$(function() {	
		//이메일(아이디부분) 알파벳만 입력 받게 하기    
		$("#email1").keyup(function(event) {
			if (!(event.keyCode >= 37 && event.keyCode <= 40)) {
				var inputVal = $(this).val();
				$(this).val(inputVal.replace(/[^a-z0-9]/gi, ''));
			}
		});
		
		$(document).ready(function(){
			$("#chgEmailInfo").hide();
			$("#chgMailInfoP").hide();
			$("#sentEmailDiv").hide();
			$("#alertEmail").hide();
			$("#inputPhone").hide();
			$("#sentPhoneDiv").hide();
			$("#alertPhone").hide();
			$("#pass-div").hide();//패스워드 변경 입력창
		})
		
		//패스워드 변경 버튼
		$("#chgPass-btn").on("click", function(){
			$("#pass-div").show();//패스워드 변경 입력창
		})
		
		//이메일 변경 버튼 
		$("#chgEmailBtn").on("click",function(){
			$("#oriEmailDiv").hide();
			$("#chgEmailInfo").show();
			$("#chgMailInfoP").show();
			
		});
		
		//이메일 변경 취소 버튼 
		$("#backEmail").on("click", function(){
			$("#oriEmailDiv").show();
			$("#chgEmailInfo").hide();
			$("#chgMailInfoP").hide();
		});
		
		//인증 메일 발송 취소 버튼
		$("#backEmail").on("click", function(){
			$("#alertEmail").hide();
    		$("#sentEmailDiv").hide();
		})
		
		//핸드폰 번호 변경 버튼
		$("#chgPhoneBtn").on("click", function(){
			$("#inputPhone").show();
		    $("#oriPhoneDiv").hide(); 
		});
		
		//핸드폰 번호 변경 취소 버튼
		$("#backPhone").on("click", function(){
			$("#inputPhone").hide();
     	    $("#oriPhoneDiv").show();
		})
		
		//인증 번호 발송 버튼
		$("#sentTxtBtn").on("click", function(){
			$("#sentPhoneDiv").show();
			$("#sentTxtDiv").hide();
		})
		
		//인증 번호 취소 버튼
		$("#backOriPhone").on("click", function(){
			$("#sentPhoneDiv").hidFe();
			$("#sentTxtDiv").show();
		})

	});

	function validate() {
		//핸드폰 유효성 검사 
		var regExp = /([0-9]{7,8})$/;
		var ck = /^\d{3}-\d{3,4}-\d{4}$/;

		if (!regExp.test($("input[name=phone2]").val())) {
			alert("정확한 휴대폰 번호를 입력해주세요.");
			return false;
		}
		
		//이메일 유효성 검사
		var ckMail=/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
		if(!ckMail.test($("input[name=email]").val())){
			alert("정확한 이메일 형식을 입력해주세요.");	
			return false;
		}		
		//비밀번호 체크 
		var pw1 = $("#password1").val().trim();
		var pw2 = $("#password2").val().trim();
		if (pw1 != pw2) {
			alert("정확한 비밀번호 입력하세요.");
			return false;
		}
		return true;
		
	}

	//프로필 사진을 이미지 타입 파일로만 받기
	function fileCheck(obj) {
		var fileKind = obj.value.lastIndexOf('.');
		var fileName = obj.value.substring(fileKind + 1, obj.length);
		var fileType = fileName.toLowerCase();

		var ckFileType = new Array();
		ckFileType = [ 'jpg', 'gif', 'png', 'jpeg', 'jpeg', 'bmp' ];

		if (ckFileType.indexOf(fileType) == -1) {
			alert("이미지 파일만 선택할 수 있습니다.");
			var parentObj = obj.parentNode;
			var node = parentObj.replaceChild(obj.cloneNode(true), obj);
			return false;
		}
	}
	
 	function sendEmail(){		    
	    //이전 이메일과 중복여부 확인
	    	var reMail=$('#reMail').val().trim();//변경할 이메일
	   	    var oriMail=$('.oriEmail').text();//기존 이메일
	   	    
	   		//이메일 유효성 검사
			var ckMail=/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
			
			if(!ckMail.test($("input[name=email]").val())){
				alert("정확한 이메일 형식을 입력해주세요.");	
				return false;
			}
	   	    
	     	if(reMail===oriMail){
	    		$("#alertEmail").show();
	    		$("#sentEmailDiv").hide();	    		
	    		 return false;	    		
	    	}else{
	    		$("#alertEmail").hide();
	    		$("#sentEmailDiv").show();
	    		
	    		$.ajax({
	    		url:"${path}/member/"	
	    		})
	    			    		
	    	} 
	    
	} 
 	
 	

	
</script>
      <div id="enroll-container" class="container">
         <form name="memberEnrollFrm" id="memberEnrollFrm" action="${path }/member/updateInfoEnd.do" method="post" onsubmit="return validate();" enctype="multipart/form-data">         
       		<%-- <div class="row">
       			<div class="profileDiv col-6">
		           <div class="custom-file" >
		           		<img class="card-img-top" id="pro_img" src="${path }/resources/upload/profile/${logined.profileImageRe}" alt="Card image cap">
	   		    	    <input type="file" class="custom-file-input" accept="image/*" id="upFile" name="upFile" value="${logined.profileImageRe }" onchange="fileCheck(this)" required>
		           </div>
		         </div> 
	         	  <div class="col-6"> 	         	  	           	
	           		 <!--기존 Email 정보  -->
           		     <div class="row passwordInfo" id="oriEmailDiv">    	
		           		<div class="ptext col-6">
		           			<p class="oriEmail font-weight-bold"><%=m.getEmail() %></p>
		       			</div>  
		       			<div class="col-6 chgBtnDiv">
		       				<button type="button" id="chgEmailBtn" class="cg btn btn-secondary btn-sm">이메일변경</button>      	
		          		</div> 		
	           		 </div>
	           		 <!--Email 변경 버튼 클릭시  -->
	           		 <div class="row passwordInfo" id="chgEmailInfo">    	
		           		<div class="ptext col-7">
		       			  <input type="email" class="form-control" value="${logined.email }" id="reMail" name="email" maxlength="30" required>
		       			</div>  
		       			<div class="btns col-5">
		       			  <button type="button" class="cg btn btn-secondary btn-sm" id="sendMail-btn" onclick="sendEmail();">인증 메일 발송</button>
		       			  <button type="button" id="backEmail" class="cg btn btn-secondary btn-sm">취소</button>
		          		</div> 		
	           		 </div>
       			 	 <div class="alert alert-danger text-danger" id="alertEmail" role="alert">이전과 동일한 메일주소 입니다.</div>          		 
     			     <div><p class="pPassword" id="chgMailInfoP">이메일 주소를 인증하시면 변경이 완료됩니다.</p></div>
	           		  <!--Email 변경 인증 메일 전송 후  -->      			  
	       			  <div id="sentEmailDiv">
		    			  <div class="alert alert-success text-success" role="alert">인증메일이 발송되었습니다.</div>
		    			  <p class="pPassword">유효시간: 이메일 발송 후 3분 이내.</p>
		    			    <button type="button" class="cg btn btn-secondary btn-sm">인증 메일 재발송</button>
		       			  <button type="button" class="cg btn btn-secondary btn-sm">이메일 확인하러 가기</button>
	       			  </div>		
	       			   <!--기존 Phone 정보  -->
	           		  <div class="row passwordInfo" id="oriPhoneDiv">    	
		           		<div class="ptext col-6">
		           			<p class="oriPhone font-weight-bold"><%=m.getPhone() %></p>
		       			</div>  
		       			<div class="col-6">
		       				<button type="button" id="chgPhoneBtn" class="cg btn btn-secondary btn-sm">핸드폰 번호 변경</button>      	
		          		</div> 		
	           		 </div>
	           		 <!--핸드폰번호 변경 버튼 클릭시  -->
	           		 <div id="inputPhone">
		       		      <div class="row phone">
			               	  <c:set var="phone" value="${logined.phone}"/>
			                     <select class="tel form-control col-sm-5" name="phone1" id="selectPhone" required>                                                                                        
			                        <option  value="010" <c:if test="${fn:contains(fn:substring(phone,0,3),'010')}">selected</c:if>>010</option>
			                        <option  value="011" <c:if test="${fn:contains(fn:substring(phone,0,3),'011')}">selected</c:if>>011</option>
			                        <option  value="016" <c:if test="${fn:contains(fn:substring(phone,0,3),'016')}">selected</c:if>>016</option>
			                        <option  value="017" <c:if test="${fn:contains(fn:substring(phone,0,3),'017')}">selected</c:if>>017</option>
			                        <option  value="018" <c:if test="${fn:contains(fn:substring(phone,0,3),'018')}">selected</c:if>>018</option>
			                        <option  value="019" <c:if test="${fn:contains(fn:substring(phone,0,3),'019')}">selected</c:if>>019</option>         
			                        <option  value="070" <c:if test="${fn:contains(fn:substring(phone,0,3),'070')}">selected</c:if>>070</option> 
			                     </select>
			                   
			                     <c:if test="${fn:length(phone) eq 11}">
			                		<input type="text" class="tel form-control col-sm-7" name="phone2" id="phone2" value="${fn:substring(phone, 3,12)}"required>
			           			 </c:if>
			           			 <c:if test="${fn:length(phone) eq 10}">
			                		<input type="text" class="tel form-control col-sm-7" name="phone2" id="phone2" value="${fn:substring(phone, 3,11)}"required>
			           			</c:if>
		         		 </div>	       			
	       			 	 <div class="alert alert-danger text-danger" id="alertPhone" role="alert">이전과 동일한 번호 입니다.</div>          		 		       			
		   			     <div><p class="pPassword" id="chgPhoneInfoP">핸드폰 번호를 인증하시면 변경이 완료됩니다.</p></div>
		   			    <div id="sentTxtDiv">
		       			  <button type="button" class="cg btn btn-secondary btn-sm" id="sentTxtBtn">인증 번호 발송</button>
		       			  <button type="button" id="backPhone" class="cg btn btn-secondary btn-sm">취소</button>
		       			</div>   
	       			    <!--핸드폰 변경 문자 인증 전송 후  -->      			  
		       			  <div id="sentPhoneDiv">
			    			  <div class="alert alert-success text-success" role="alert">인증번호가 발송되었습니다.</div>
				           	  <input type="number" class="form-control" placeholder="핸드폰으로 전송된 인증번호 3분이내 입력하세요." name="phoneCk" required>
		    			      <button type="button" class="cg btn btn-secondary btn-sm">인증 문자 재발송</button>
		    			      <button type="button" id="backOriPhone" class="cg btn btn-secondary btn-sm">취소</button>
		       			  </div>	          				
		       	   	</div>	
	                  <input type="submit" class="btn btn-outline-success" id="enrollBtn" value="정보변경" >&nbsp;
	                  <input type="reset" class="btn btn-outline-success" value="취소">
           	 	</div>       			   			
              </div> --%>
              
              
              <%-- 
		           <div class="custom-file" >
		           		<img class="card-img-top" id="pro_img" src="${path }/resources/upload/profile/${logined.profileImageRe}" alt="Card image cap">
	   		    	    <input type="file" class="custom-file-input" accept="image/*" id="upFile" name="upFile" value="${logined.profileImageRe }" onchange="fileCheck(this)" required>
		           </div> --%>
              
              
             
              
              
				<div class="card" >
				  <ul class="list-group list-group-flush">
					 <li>
						 <div class="custom-file" > 
							 <img class="img-thumbnail mx-auto d-block" id="pro_img" src="${path }/resources/upload/profile/${logined.profileImageRe}" alt="Card image cap">
							 <input type="file" class="custom-file-input" accept="image/*" id="upFile" name="upFile" value="" onchange="fileCheck(this)" required>
					  	 </div> 
				  	 </li>
				    <li class="list-group-item">
				    	<button type="button" class="btn btn-secondary btn-sm btn-block" data-toggle="modal" data-target="#chgPass">비밀번호 변경하기</button>   
				    </li>
				    <li class="list-group-item">
						<!--기존 Email 정보  -->
					     <div class="row passwordInfo" id="oriEmailDiv">    	
				    		<div class="ptext col-6">
				    			<p class="oriEmail font-weight-bold"><%=m.getEmail() %></p>
							</div>  
							<div class="col-6 chgBtnDiv">
								<button type="button" id="chgEmailBtn" class="cg btn btn-secondary btn-sm">이메일변경</button>      	
				   			</div> 		
				   		 </div>
				   		 <!--Email 변경 버튼 클릭시  -->
				   		 <div class="row passwordInfo" id="chgEmailInfo">    	
				    		<div class="ptext col-7">
							  <input type="email" class="form-control" value="${logined.email }" id="reMail" name="email" maxlength="30" required>
							</div>  
							<div class="btns col-5">
							  <button type="button" class="cg btn btn-secondary btn-sm" id="sendMail-btn" onclick="sendEmail();">인증 메일 발송</button>
							  <button type="button" id="backEmail" class="cg btn btn-secondary btn-sm">취소</button>
				   			</div> 		
				   		 </div>
					 	 <div class="alert alert-danger text-danger" id="alertEmail" role="alert">이전과 동일한 메일주소 입니다.</div>          		 
						 <div><p class="pPassword" id="chgMailInfoP">이메일 주소를 인증하시면 변경이 완료됩니다.</p></div>
				   		  <!--Email 변경 인증 메일 전송 후  -->      			  
						  <div id="sentEmailDiv">
				 			 <div class="alert alert-success text-success" role="alert">인증메일이 발송되었습니다.</div>
				  				<p class="pPassword">유효시간: 이메일 발송 후 3분 이내.</p>
				   			    <button type="button" class="cg btn btn-secondary btn-sm">인증 메일 재발송</button>
							    <button type="button" class="cg btn btn-secondary btn-sm">이메일 확인하러 가기</button>
						  </div> 
				    </li>
				    <li class="list-group-item">
				    	   <!--기존 Phone 정보  -->
	           		  <div class="row passwordInfo" id="oriPhoneDiv">    	
		           		<div class="ptext col-6">
		           			<p class="oriPhone font-weight-bold"><%=m.getPhone() %></p>
		       			</div>  
		       			<div class="col-6">
		       				<button type="button" id="chgPhoneBtn" class="cg btn btn-secondary btn-sm">핸드폰 번호 변경</button>      	
		          		</div> 		
	           		 </div>
	           		 <!--핸드폰번호 변경 버튼 클릭시  -->
	           		 <div id="inputPhone">
		       		      <div class="row phone">
			               	  <c:set var="phone" value="${logined.phone}"/>
			                     <select class="tel form-control col-sm-5" name="phone1" id="selectPhone" required>                                                                                        
			                        <option  value="010" <c:if test="${fn:contains(fn:substring(phone,0,3),'010')}">selected</c:if>>010</option>
			                        <option  value="011" <c:if test="${fn:contains(fn:substring(phone,0,3),'011')}">selected</c:if>>011</option>
			                        <option  value="016" <c:if test="${fn:contains(fn:substring(phone,0,3),'016')}">selected</c:if>>016</option>
			                        <option  value="017" <c:if test="${fn:contains(fn:substring(phone,0,3),'017')}">selected</c:if>>017</option>
			                        <option  value="018" <c:if test="${fn:contains(fn:substring(phone,0,3),'018')}">selected</c:if>>018</option>
			                        <option  value="019" <c:if test="${fn:contains(fn:substring(phone,0,3),'019')}">selected</c:if>>019</option>         
			                        <option  value="070" <c:if test="${fn:contains(fn:substring(phone,0,3),'070')}">selected</c:if>>070</option> 
			                     </select>
			                   
			                     <c:if test="${fn:length(phone) eq 11}">
			                		<input type="text" class="tel form-control col-sm-7" name="phone2" id="phone2" value="${fn:substring(phone, 3,12)}"required>
			           			 </c:if>
			           			 <c:if test="${fn:length(phone) eq 10}">
			                		<input type="text" class="tel form-control col-sm-7" name="phone2" id="phone2" value="${fn:substring(phone, 3,11)}"required>
			           			</c:if>
		         		 </div>	       			
	       			 	 <div class="alert alert-danger text-danger" id="alertPhone" role="alert">이전과 동일한 번호 입니다.</div>          		 		       			
		   			     <div><p class="pPassword" id="chgPhoneInfoP">핸드폰 번호를 인증하시면 변경이 완료됩니다.</p></div>
		   			    <div id="sentTxtDiv">
		       			  <button type="button" class="cg btn btn-secondary btn-sm" id="sentTxtBtn">인증 번호 발송</button>
		       			  <button type="button" id="backPhone" class="cg btn btn-secondary btn-sm">취소</button>
		       			</div>   
	       			    <!--핸드폰 변경 문자 인증 전송 후  -->      			  
		       			  <div id="sentPhoneDiv">
			    			  <div class="alert alert-success text-success" role="alert">인증번호가 발송되었습니다.</div>
				           	  <input type="number" class="form-control" placeholder="핸드폰으로 전송된 인증번호 3분이내 입력하세요." name="phoneCk" required>
		    			      <button type="button" class="cg btn btn-secondary btn-sm">인증 문자 재발송</button>
		    			      <button type="button" id="backOriPhone" class="cg btn btn-secondary btn-sm">취소</button>
		       			  </div>
       			  		</div>			    
				    </li>
				  </ul>
				  <div class="card-body">
				    <a href="#" class="card-link">Card link</a>
				    <a href="#" class="card-link">Another link</a>
				  </div>
				</div>
              
              
      
          </form>
       </div> 

<!--비밀번호 변경하기 모달창  -->
<div class="modal fade" id="chgPass" tabindex="-1" role="dialog" aria-labelledby="chgPassLabel" aria-hidden="true">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">비밀번호 변경하기</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
          <div class="row form-group">
       		<div class="ptext col-5">
       			<p class="badge badge-secondary">비밀번호 변경 시 유의사항</p>&nbsp&nbsp  
   			</div>  
   			<div class="col-7">      	
   				<p class="pPassword">숫자/영문자/특수문자 조합 6~15자</p>
      		</div> 		             
          </div>
          <input type="password" class="form-control" placeholder="새 비밀번호" name="memberPw" id="password1" onchange="passwordCheck(this)" maxlength="15" required>                         
          <input type="password" class="form-control" placeholder="새 비밀번호확인" id="password2" maxlength="15" required>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
        <a href="#" class="btn btn-primary" data-dismiss="modal" onclick="changePass();">변경하기</a>
         <script>
        	function changePass(){
        		var pass1=$("#password1").val();
        		var pass2=$("#password2").val();
        		if(pass1==pass2){
        			console.log("ajax실행할 차례");
        		 	$.ajax({
        				url:"${path}/member/changePass",
        				data:{"password":pass1},
        				success:function(result){
        					console.log(result);       				
        					if(result=="update"){
        						alert("비밀번호 변경 성공");       					
	        				}else{
	        					alert("비밀번호 변경 실패");
	        				}	
       					}
        			});        			
        		}else{
        			alert("비밀번호가 일치하지 않습니다.");
        		 	return false; 
        		}
        	}
        </script> 
      </div>
    </div>
  </div>
</div>







<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>