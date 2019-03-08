<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*,com.spring.odagada.member.model.vo.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
	Member m = (Member) request.getAttribute("logined");
	System.out.println(m);
    String eDate []=m.getEnrollDate().split(" ");
%>

<c:set var="path" value="${pageContext.request.contextPath}"/>
<jsp:include page="/WEB-INF/views/common/header.jsp">
   <jsp:param value="오다가다 타는 카풀" name="pageTitle"/>
</jsp:include>

<style>

  div#enroll-container{width:400px; margin:0 auto;}
  div#enroll-container input, div#enroll-container select {margin-bottom:10px;}
  .menu{text-align:center; font-weight:bold;}
  .info{margin-top:50px; margin-bottom: 50px;}
  #answer{height:50%;}
  
</style>

<section class="container">
   <div class="row">

      <div class="col-12 col-md-4">
         <div class="menu_list info list-group">
            <a class="list-group-item list-group-item-action active">회원 정보 관리</a>
            <a href="#" class="list-group-item list-group-item-action">드라이버 정보 관리</a>
            <a href="${path }/member/myCarpool" class="list-group-item list-group-item-action">카풀 내역</a>
         </div>
      </div>
      
      <div class="col-12 col-md-8" id="enroll-container">   
         <div class="info col-12 col-sm-12 col-md-9">
           <img class="img-thumbnail mx-auto d-block" src="${path }/resources/upload/profile/${logined.profileImageRe}" alt="Card image cap">
            <div class="card-group">
               <div class="card">
                    <table class="table table-hover">
                      <tr>
                      <th scope="row">아이디</th>
				      <td colspan="2"><c:out value="${logined.memberId }"></c:out></td>  
				    </tr>
				    <tr>
					   <th scope="row">이름</th>
				       <td colspan="2"><c:out value="${logined.memberName }"></c:out></td>
				    </tr>
				    <tr>
				       <th scope="row">가입날짜</th>
			     	   <td colspan="2"><%=eDate[0] %></td>
				    </tr>
				     <tr>
				       <th scope="row">E-mail</th>
				       <td><c:out value="${logined.email }"></c:out></td>
				      <td>
				      	<c:choose>
				      		<c:when test="${logined.isEmailAuth eq 'N'}">
				      			<a href="#" class="ck badge badge-danger">인증하기</a>
		      					<button type="button" class="ck badge badge-danger" data-toggle="modal" data-target="#sendEmail" data-whatever="@mdo">인증하기</button>
		      				</c:when>
				      		<c:when test="${logined.isEmailAuth eq 'Y'}">
				      			<div class="alertS alert alert-success text-success" role="alert">인증완료</div>
			      			</c:when>
				      	</c:choose> 
				      </td>				       
				    </tr>
				     <tr>				       
				      <th scope="row">Phone</th>
				        <td><c:out value="${logined.phone }"></c:out></td>
				      <td>		
			       	 	<c:choose>
			       	 		<c:when test="${logined.isPhoneAuth eq 'N'}">				      			
				      			<a href="#" class="ck badge badge-danger">인증하기</a>
				      		</c:when>
				      		<c:when test="${logined.isPhoneAuth eq 'Y'}">
				      			<div class="alertS alert alert-success text-success" role="alert">인증완료</div>
				      		</c:when>
			      		</c:choose>   
				      </td>
				    </tr>		 
				</table>
				<a href="${path }/member/updateInfo.do" class="btn btn-primary" >정보변경하자좀</a>		
			  <div class="menu card-body">		 	   
			 	 <button type="button" class="btn btn-outline-success"  data-toggle="modal" data-target="#exampleModal" data-whatever="@mdo">정보 변경</button>
				 <button type="button" class="btn btn-outline-success" onclick="withdrawal();"data-toggle="modal" data-target="#exampleModal" data-whatever="@fat">회원 탈퇴</button>	
					<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
					  <div class="modal-dialog" role="document">
					    <div class="modal-content">
					      <div class="modal-header">
					        <h5 class="modal-title" id="exampleModalLabel">정보변경</h5>				        
					        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
					          <span aria-hidden="true">&times;</span>
					        </button>
					      </div>
					      <div class="modal-body">
					          <div class="form-group">
					            <label for="answer" class="form-control-label">보안을 위해 비밀번호를 한 번 더 입력해주세요.</label>
					            <input type="password" class="form-control" id="answer" name="answer" style="resize: none;">
					          </div>
					      </div>
					      <div class="modal-footer">					      	
					        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
					        <button type="button" class="btn btn-primary" onclick="updateCheck();">정보변경하기</button>					        
					      </div>
					    </div>
					  </div>
					</div>
					
					
					
					
						<div class="modal fade" id="sendEmail" tabindex="-1" role="dialog" aria-labelledby="sendEmailLabel" aria-hidden="true">
						  <div class="modal-dialog" role="document">
						    <div class="modal-content">
						      <div class="modal-header">
						        <h5 class="modal-title" id="sendEmailLabel">E-mail 인증하기</h5>				        
						        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
						          <span aria-hidden="true">&times;</span>
						        </button>
						      </div>
						      <div class="modal-body">
						          <div class="form-group">
						            <label for="answer" class="form-control-label">E-mail로 전송된 인증번호를 입력해주세요.</label>
						            <input type="password" class="form-control" id="answer" name="answer" style="resize: none;">
						          </div>
						      </div>
						      <div class="modal-footer">					      	
						        <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
						        <button type="button" class="btn btn-primary" onclick="emailCheck();">인증하기</button>					        
						      </div>
						    </div>
						  </div>
						</div>
					  </div>
					</div>
				</div>		
			</div>
		</div>
	</div>
</section>

<script>
function updateCheck() {
    var answer = $("#answer").val();
    console.log("anwer : "+answer);
    $.ajax({
    	url:"${path}/member/checkPw.do",
    	data:{"answer":answer},
    	success:function(data){
    		
    		console.log(data);
    		console.log(data+": "+typeof data);
    		
    		if(data =='no'){
				alert("비밀번호가 올바르지 않습니다.");
				return;
    		}else{   			
    			location.href="${path}/member/updateInfo.do";
    		}
    	}
    });  
    
    
   
  
}
function  emailCheck() {  	
}



</script>






<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>