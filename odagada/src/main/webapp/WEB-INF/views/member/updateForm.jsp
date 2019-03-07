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
     div#enroll-container{width:400px; margin:0 auto; text-align:center;}
     div#enroll-container input, div#enroll-container select {margin-bottom:10px;}

    /*중복아이디체크관련*/
    div#enroll-container{position:relative; padding:0px;}
    div#enroll-container span.guide {display:none;font-size: 12px;position:absolute; top:12px; right:10px;}
    div#enroll-container span.ok{color:green;}
    div#enroll-container span.error{color:red;}
    
    div#board-continer{width:400px;margin:0 auto; text-align:center}
    div#board-continer input{margin-bottom:15px;} 
    
    
    /*비밀번호 체크 */
    div#enroll-container span.ck {display:none;font-size: 12px;position:absolute; top:12px; right:10px;}
    div#enroll-container span.ckOk{color:green;}
    div#enroll-container span.ckNo{color:red;}
    
    .ckOk, .ckNo{
    padding-right:13px;
    }
       
    </style>
    
     
<script>

 $(function(){
   $('[name=upFile]').on('change',function(){
      //var filename=$(this).val();
      var filename=this.files[0].name;
      //var filename=$(this).prop('files')[0].name;
      $(this).next('.custom-file-label').html(filename);
   });  
  
	$('#pro_img').on('click', function() {
			$('#upFile').trigger('click');
		})
	});
	$('#pro_img').on('change', function() {

	});

	//비밀번호 유효성 검사
	function passwordCheck(password) {
		var pw = $(password).val();
		var ckPw = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{6,15}$/;
		if (!ckPw.test(pw)) {
			alert('숫자,영문자,특수문자 조합으로 6~15자');
			$('#password_').val('').focus();
			return false;
		}
		return true;
	}

	$(function() {
		//비밀번호 일치 확인
		$('#password2').keyup(function() {
			var password2 = $("#password2").val().trim();
			if (password2.length < 6) {
				$(".ck").hide();
				return;
			}
			var pw1 = $("#password_").val().trim();
			var pw2 = $("#password2").val().trim();
			if (pw1 == pw2) {
				$(".ck.ckOk").show();
				$(".ck.ckNo").hide();
			} else {
				$(".ck.ckOk").hide();
				$(".ck.ckNo").show();
			}
		});

		//이메일(아이디부분) 알파벳만 입력 받게 하기    
		$("#email1").keyup(function(event) {
			if (!(event.keyCode >= 37 && event.keyCode <= 40)) {
				var inputVal = $(this).val();
				$(this).val(inputVal.replace(/[^a-z0-9]/gi, ''));
			}
		});

		/*    //핸드폰 숫자만 입력받게 하는 함수    
		 $('#phone2').on('keyup', function() {
		 if (/\D/.test(this.value)) {
		 this.value = this.value.replace(/\D/g, '')
		 alert('숫자만 입력가능합니다.');
		 }
		 });
		 */
	});

	function validate() {
		//핸드폰 유효성 검사 
		var regExp = /([0-9]{7,8})$/;
		if (!regExp.test($("input[name=phone2]").val())) {
			alert("정확한 휴대폰 번호를 입력해주세요.");
			return false;
		}
		//이메일 도메인 정규식 받기    
		var mC = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i;
		if (!mC.test($("input[name=email2]").val())) {
			alert("정확한 이메일 형식을 입력해주세요.");
			return false;
		}
		//비밀번호 체크 
		var pw1 = $("#password_").val().trim();
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
</script>
      <div id="enroll-container">
         <form name="memberEnrollFrm" action="${path }/member/updateInfo.do" method="post" onsubmit="return validate();" enctype="multipart/form-data">         
       		<div class="row">
       			<div class="col-6">
		           <div class="custom-file" >
		           		<img class="card-img-top" id="pro_img" src="${path }/resources/upload/profile/${logined.profileImageRe}" alt="Card image cap">
		           </div>
		         </div> 
	           <div class="col-6">           			                                
       			 <input type="password" class="form-control" placeholder="새 비밀번호" name="memberPw" id="password_" onchange="passwordCheck(this)" maxlength="15" required>                              
                  <input type="password" class="form-control" placeholder="새 비밀번호확인" id="password2" maxlength="15" required>
                  <span class="ck ckOk">비밀번호 일치</span>
                  <span class="ck ckNo">비밀번호 불일치</span>                      	          			
       			  <input type="email" class="form-control" value="${logined.email }" name="email" maxlength="15" required>
       			  <input type="text" class="form-control" value="${logined.phone }" name="phone" maxlength="15" required>	           				         
   		    	  <input type="file" class="custom-file-input"  accept="image/*" id="upFile" name="upFile" onchange="fileCheck(this)" required>
                  <input type="submit" class="btn btn-outline-success" id="enrollBtn" value="가입" >&nbsp;
                  <input type="reset" class="btn btn-outline-success" value="취소">
               </div>
             </div> 
          </form>
       </div>







<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>