<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<jsp:include page="/WEB-INF/views/common/header.jsp">
   <jsp:param value="오다가다 타는 카풀" name="pageTitle"/>
</jsp:include>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.min.js"></script>

   <style>
     div#enroll-container{width:350px; margin:0 auto; text-align:center; display: none;}
     div#enroll-container input, div#enroll-container select {margin-bottom:10px;}
    /*중복아이디체크관련*/
    div#enroll-container{position:relative; padding:0px;}
    div#enroll-container span.guide {display:none;font-size: 12px;position:absolute; top:12px; right:10px;}
    div#enroll-container span.ok{color:green;}
    div#enroll-container span.error{color:red;}
    
    div#board-continer{width:350px;margin:0 auto; text-align:center}
    div#board-continer input{margin-bottom:15px;} 
    
    
    /*비밀번호 체크 */
    div#enroll-container span.ck {display:none;font-size: 12px;position:absolute; top:12px; right:10px;}
    div#enroll-container span.ckOk{color:green;}
    div#enroll-container span.ckNo{color:red;}
    
    .ckOk, .ckNo{
    padding-right:13px;
    }
       
  .emailC{     
    display: block;
    padding: .375rem .75rem;
    font-size: 1rem;
    line-height: 1.5;
    color: #495057;
    background-color: #fff;
    background-clip: padding-box;
    border: 1px solid #ced4da;
    border-radius: .25rem;
    transition: border-color .15s ease-in-out,box-shadow .15s ease-in-out;   
    }
    
    .tel{
    display: block;
    padding: .375rem .75rem;
    font-size: 1rem;
    line-height: 1.5;
    color: #495057;
    background-color: #fff;
    background-clip: padding-box;
    border: 1px solid #ced4da;
    border-radius: .25rem;
    transition: border-color .15s ease-in-out,box-shadow .15s ease-in-out; 
    width: 100%; 
    }
   
   .div-email{
   padding : 0px;
   }
   .row-email{
      padding: 0px;
      margin-left: 0px;
      margin-right: 0px;
   }
    
   .addon-email{
      margin-bottom: 10px;
   }
   
   .profile{
   margin-left:13px;
   margin-right:13px;
   }
   
   .gender{
   padding-left:28%;
   }
   
   .genderRow{
   margin-top: 10px;
   }
   
   .submitB{
   margin-top:5%;
   margin-left:30%;
   margin-bottom: 10%;
   }
   
   #enrollBtn{
   margin-right:15%; 
   }
   
   #memberId_{
   margin-top:20%;
   }
   
   .genderC{
   padding-left:0;
   }
   
   #gender1{
   margin-left:20px;
   } 
   .dR{
   padding-left:1%;
   }
   .dL{
   padding-right:1%;
   }
   .phone2C{
   padding-left:0px;
   }
   
   li { list-style: none }
  
  	li.li_pass{
  		font-size:14px;
  		margin-top:-10px;
  		margin-left:-10px;
  	}
  	p{
  	font-size:12px;
  	}
  	.passwordInfo{
  	height:40px;
  	}
  	.eck{padding-right:0; padding-left:0; font-size:15px;}
  	.upFile-div{margin-bottom:10px;}
  	.select-div, .phone-div, .phone-btn{padding:0;} 
  	.p-div{margin-top:4px;}
  	.name-div{padding-right:3px;}
    </style>
    
     
<script>
 $(function(){
   $('[name=upFile]').on('change',function(){
      //var filename=$(this).val();
      var filename=this.files[0].name;
      //var filename=$(this).prop('files')[0].name;
      $(this).next('.custom-file-label').html(filename);
   });
   

   //이름 한글만 입력받게 하는 함수       
   $("#memberName").keyup(function(event) {
         if (!(event.keyCode >= 37 && event.keyCode <= 40)) {
            var inputVal = $(this).val();
            $(this).val(inputVal.replace(/[^가-힣]/gis, ''));
      }
   });
   
   //생년월일
     $("#birth" ).datepicker({
       changeMonth: true, changeYear: true, dateFormat: "yy-mm-dd", showButtonPanel: true, yearRange: "c-99:c+99", maxDate: "+200d"   
     });
              
   //이메일(아이디부분)    
   $("#email1").keyup(function(event) {
      if (!(event.keyCode >= 37 && event.keyCode <= 40)) {
         var inputVal = $(this).val();
         $(this).val(inputVal.replace(/[^a-z0-9_+.-]/gi, ''));
      }
   });
   
   //핸드폰 숫자만 입력받게 하는 함수    
   $('#phone2').on('keyup', function() {
      if (/\D/.test(this.value)) {
         this.value = this.value.replace(/\D/g, '')        
         alert('숫자만 입력가능합니다.');
      }
   });   
 });
 
 function validate() {
  	      
      //이름 유효성 검사
      //var nameCk=/^[가-힣]{2,6}||[a-zA-Z]{2,10}\s[a-zA-Z]{2,10}$/;
      var name=$('#memberName').val().trim();
       if(name.length>7){
          alert("정확한 이름을 입력해주세요.");
          return false;
    	  }
     	//이메일 도메인 정규식 받기    
        var mC= /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i; 
        if(!mC.test($("input[name=email2]").val())){
          alert("정확한 이메일 형식을 입력해주세요.");
          return false;
       }  
       
        //E-mail 중복확인 체크
        if($('#emailStatus').val()==0){
        alert('e-mail 중복확인 해주세요.')
        return false;
        }
        //핸드폰 중복체크 
        if ($('#phoneStatus').val()==0) {
        	console.log($('#phoneStatus').val());
              alert("핸드폰 중복확인해주세요.");
              return false;
           }
        
        //생년월일 받기
        var birth=$('#birth').val().trim();
        if(birth==''){
        	alert('생일을 입력해주세요.')
        	return false;
        }
        
  
   }
 
   //프로필 사진을 이미지 타입 파일로만 받기
   function fileCheck(obj){
      var fileKind=obj.value.lastIndexOf('.');
      var fileName=obj.value.substring(fileKind+1, obj.length);
      var fileType=fileName.toLowerCase();
      
      var ckFileType=new Array();
      ckFileType=['jpg','gif','png','jpeg','jpeg','bmp'];
      
      if(ckFileType.indexOf(fileType)==-1){
         alert("이미지 파일만 선택할 수 있습니다.");
         var parentObj=obj.parentNode;
         var node=parentObj.replaceChild(obj.cloneNode(true), obj);
         return false;      
         }   
      }
   
   
 //E-mail 중복 확인
   function checkEmail(){ 	
     	//이메일 도메인 정규식 받기    
       var mC= /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i; 
       if(!mC.test($("input[name=email2]").val())){
         alert("정확한 이메일 형식을 입력해주세요.");
         return false;
      }  
   		
       var email1=$("#email1").val();
       var at='@';
       var email2=$("#email2").val();
       var email=email1+at+email2;
       var phoneStatus=$('#phoneStatus').val();
        
       $.ajax({
           url:"${path}/member/checkEmail.do",
           data:{"email":email},
           success:function(data){                 
               if(data == 'no')
               {
              	  alert('이미 사용중인 이메일 입니다. 사용하실 수 없습니다.');          
                  document.getElementById('emailStatus').value='0';
                  return false;
               }else{
               	 document.getElementById('emailStatus').value='1'; 
               	alert('사용하셔도 좋습니다.');
               }
           }
        });  
      };
      
      //핸드폰 중복확인
      function checkPhone(){
    	    var phone1=$('#selectPhone option:selected').val();
    	    var phone2=$('#phone2').val();
    	    var phone=phone1+phone2;
    	    console.log(phone);
    	  //핸드폰 유효성 검사 
          var regExp = /([0-9]{7,8})$/;
          if (!regExp.test($("input[name=phone2]").val())) {
                alert("정확한 휴대폰 번호를 입력해주세요.");
                return false;
             }
   	    
    	    $.ajax({
    	    	url:"${path}/member/phoneCheck.do",
    	    	data:{"phone": phone},
    	    	success:function(data){
    	    		
    	    		if(data == 'Y'){
    	    			alert('사용하셔도 좋습니다.');
    	    			document.getElementById('phoneStatus').value='1';
    	    		}else{
    	    			alert('이미 사용중인 번호입니다. 사용하실 수 없습니다.');
    	    			 document.getElementById('phoneStatus').value='0';
    	    			return false;
    	    		}
    	    	}
    	    }) ;   	  
      };
      
      
</script>
<script type="text/javascript" src="https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.0.js" charset="utf-8"></script>
<script>
	
	$(function(){
		
	var naverLogin = new naver.LoginWithNaverId({
				clientId: "vc3JlDx8OrM42I7Dhas4",
				callbackUrl: "http://localhost:9090/odagada/member/naverSignup",
				
			});

	naverLogin.init();
	
	
	naverLogin.getLoginStatus(function (status) {
		if (status) {
			var loginStatus = naverLogin.loginStatus;
			$("#enroll-container").hide();
			$.ajax({
				url:"${path}/member/checkNaver",
				type:"post",
				data:{
					"id":"naver_"+loginStatus.naverUser.id,
					"pw":loginStatus.accessToken.accessToken,
				},
				success:function(result){
					if(result === 'true'){
						location.href="${path}/";
					}else{
						$("#password_").val(loginStatus.accessToken.accessToken);
						$("#memberId_").val("naver_"+loginStatus.naverUser.id);
						$("#enroll-container").show();
					}
				}
			});
			
			
		} else {
			console.log("callback 처리에 실패하였습니다.");
		}
	});
	});

</script>
      <div id="enroll-container">
         <h3>추가 정보 입력</h3>
         <form name="memberEnrollFrm" action="${path }/member/signUpEnd.do" method="post" onsubmit="return validate();" enctype="multipart/form-data">
            <input type="text" class="form-control" name="memberId" id="memberId_" maxlength="12" required hidden>
            <span class="guide ok">ID 사용 가능 </span>
            <span class="guide error">ID 중복 사용 불가 </span>
            <input type="hidden" name="checkId" value="0"/>
            <input type="hidden" id="checkStatus" value="0"/>
            <div class="row">
               <div class="col-5 dL">
                  <div>
                     <input type="password" class="form-control" name="memberPw" id="password_" onchange="passwordCheck(this)" maxlength="15" required hidden>                
                  </div>                       
               </div>       
            </div>
             <div class="row">                     
                  <div class="upFile-div custom-file col-12">
                      <input type="file" class="custom-file-input" accept="image/*" name="upFile" onchange="fileCheck(this)" required>
                      <label class="custom-file-label profile" for="upFile">프로필 사진 등록</label>
                  </div>                      
             </div>           
            <div class="row">
               <div class="col-6 name-div">
                  <div>
                     <input type="text" class="form-control" placeholder="이름" name="memberName" id="memberName" maxlength="8" required>
                  </div>                       
               </div>
               <div class="col-6 dR">
                  <div>
                     <input type="text" class="form-control" placeholder="생년월일" name="birth" id="birth" readonly required>
                  </div>                       
               </div>           
            </div>    
            <div class="row row-email">
               <div class="col-6 div-email">
                  <div class="input-group div-email">
                     <input type="text" class="emailC form-control" placeholder="이메일" name="email1" id="email1" maxlength="20" required>
                     <span class="input-group-addon addon-email" id="basic-addon1">@</span>
                  </div>
               </div>
               <input type="hidden" id="emailStatus" value="0"/>
               <div class="col-4 div-email">
                  <input type="text" class="emailC form-control" name="email2" id="email2" placeholder="도메인" maxlength="20" required>                                 
               </div>
               <div class="div-email col-2">
               		<input type="button" class="eck btn btn-secondary" onclick="checkEmail();" value="중복확인">
               </div>
          </div>
          <div class="row row-email">
             <div class="col-3 select-div">         
	              <select class="tel" name="phone1" id="selectPhone" required>                                                                                           
	                 <option  value="010" selected>010</option>
	                 <option  value="011">011</option>
	                 <option  value="016">016</option>
	                 <option  value="017">017</option>
	                 <option  value="018">018</option>
	                 <option  value="019">019</option>         
	                 <option  value="070">070</option> 
	              </select>
             </div>          
             <div class="col-7 phone-div">
          		<input type="text" class="tel" name="phone2" id="phone2" maxlength="8" placeholder="' - ' 제외" required>         
             </div>
             <div class="col-2 phone-btn">
             	<input type="button" class="eck btn btn-secondary" onclick="checkPhone();" value="중복확인">	
             </div>
          </div>
           <input type="hidden" id="phoneStatus" value="0"/>              
               <div class="row genderRow">         
             		<div class="gender form-check-inline from-control">성별 : &nbsp; 
	                     <input type="radio" class="form-check-input" name="gender" id="gender0" value="F" checked><label for="gender0" class="form-check-label genderC">여자</label>&nbsp;
	                     <input type="radio" class="form-check-input" name="gender" id="gender1" value="M"><label for="gender1" class="form-check-label genderC">남자</label>&nbsp;
             	   </div>
               </div>                 
                <br/>
                <div class="row submitB">  
                  <input type="submit" class="btn btn-outline-success" id="enrollBtn" value="가입" >&nbsp;
                  <input type="reset" class="btn btn-outline-success" value="취소">
               </div>
            </form>
         </div>
         

<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>