  
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<c:set var="path" value="${pageContext.request.contextPath}"/>
<jsp:include page="/WEB-INF/views/common/header.jsp">
   <jsp:param value="오다가다 타는 카풀" name="pageTitle"/>
</jsp:include>

 
<style>
  div#enroll-container{width:400px; margin:0 auto; text-align:center;}
  div#enroll-container input, div#enroll-container select {margin-bottom:10px;}
  #enroll-form{margin-top:20%; }
.submitB{margin:0 auto; margin-bottom:10%; margin-top:8%;}
.find-div{padding-right:0%;}
.cancle-div{padding-left:0%;}
#memberId_{margin-top:5%;} 
p{font-size:13px; margin-bottom:7px;}
.info{margin:1%; margin-top:10%;}
#mail-a{font-size:13px; text-align:left;}
</style>
    
<script>

function validate(){
    //이름 유효성 검사
    var name=$('#memberName').val().trim();
    if(name.length>7){
       alert("정확한 이름을 입력해주세요.");
       return false;
 	 } 
    
/* 	//이메일 유효성 검사
	var ckMail=/^[0-9a-zA-Z_+.-]([-_\.]?[0-9a-zA-Z_+.-])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
	if(!ckMail.test($("#email").val())){
		alert("정확한 이메일 형식을 입력해주세요.");	
		return false;
	}	
     */
}
    
</script>
<section class="container">    
   <div id="enroll-container">
         <form name="memberEnrollFrm" id="enroll-form" action="${path }/member/findId.do" method="post" onsubmit="return validate();" enctype="multipart/form-data">
	      	<h3>아이디 찾기</h3>
            <div class="row info">  
    			<p class="badge badge-secondary">등록된 회원 정보로 찾기</p>
				<p>등록한 이름, 이메일 주소로 아이디를 찾습니다.</p>
            </div>
            <input type="text" class="form-control" placeholder="이름" name="memberName" id="memberName" maxlength="7" required>
            <input type="text" class="form-control email-input" placeholder="등록한 이메일" name="email" id="email" maxlength="30" required>                                    
            <br/>                
           	<input type="submit" class="btn btn-success btn-sm btn-block" value="ID 찾기">
		 	<input type="reset" class="btn btn-outline-success btn-sm btn-block" value="취소">		 	         
         </form>
    </div>
</section>          

      
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>