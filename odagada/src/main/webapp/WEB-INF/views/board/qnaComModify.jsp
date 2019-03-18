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
	h1{
		text-align: center;
		font-weight: bold;
	}
	#comsubmit{
		float: right;
	}
/* 	li{
		list-style: none;
		padding-left: 0px;
	}
	 */
	 #commentWindow{
	 	border-bottom:1px solid darkgray; 
	 	margin-bottom: 15px;
	 	font-size: 15px;
	 }
	 #commentContent{
	 	margin-top: 10px;
	 }
</style>
<div class="container col-md-6">
	<h1>Q&A</h1>
    	<div class="card">
	        <div class="card-body">
	            <h4 class="card-title mb-3">${qna.QTITLE }</h4>
	            <h6 class="card-subtitle text-muted mb-4">
	                <i class="far fa-user"></i> <c:out value="${qna.QWRITER}"/>
	                <i class="far fa-clock"></i> <fmt:formatDate value="${qna.QDATE}" pattern="yyyy-MM-dd"/>
	            </h6>
	            <p class="card-text">${qna.QCONTENT}</p>
	        </div>
	        <div class="card-body">
	        <c:if test="${qna.QWRITER == member.memberName}">
	            <button class="btn btn-outline-secondary btn-sm" onclick="javascript:location.href='${path}/board/qnaModify?qnaNum=${qna.QNANUM}';">수정</button>
	        </c:if>
	        <c:choose>
	        	<c:when test="${qna.QWRITER == member.memberName}">
	        		<button class="btn btn-outline-secondary btn-sm " onclick="javascript:location.href='${path}/board/qnaDelete?qnaNum=${qna.QNANUM}';" >삭제</button>
	        	</c:when>
    	    	<c:when test="${member.isAdmin eq '1'}">
	        		<button class="btn btn-outline-secondary btn-sm " onclick="javascript:location.href='${path}/board/qnaDelete?qnaNum=${qna.QNANUM}';">삭제</button>
	        	</c:when>
	        </c:choose>
	        </div>
	        <div class="card-body">
	            <a href='<c:url value='/board/qnaList'/>' style="margin-left: 45%;" class="btn btn-success" role="button">목록으로</a>
	        </div>
	        <br/>
	        <c:if test="${member.isAdmin eq '1'}">
			<div class="container">
			<label for="content">comment</label>
				<form name="commentInsertForm" action="${path}/board/qnaComEnroll" method="post" onsubmit="return validate();">
					<div class="input-group">
						<input type="hidden" name="qnaNum" value="${qna.QNANUM}"/>
						<input type="text" class="form-control" id="comContent" name="comContent" placeholder="내용을 입력하세요.">	
						<span class="input-group-btn">
							<input type="submit" class="btn btn-success" type="button" name="commentInsertBtn" value="등록">
						</span>
					</div>
			    </form>
		  	</div>
		  	</c:if>

			  <c:forEach var="c" items="${comment}">
			  <div class="card-body text-muted">
			  	<div class="container" style="font-size: 15px;">
			  		<div class="commentList">
			  			<div class="commentArea" id="commentWindow">
			  				<div class="commentInfo far fa-user">관리자 &nbsp;&nbsp;
			  					<c:if test="${member.isAdmin eq '1'}">
			  					<input type="hidden" name="commentNum" value="${c.COMMENTNUM}"/>
              						<a onclick="javascript:location.href='${path}/board/qnaComModifyEnd?qnaNum=${qna.QNANUM}&commentNum=${c.COMMENTNUM}';"> 수정 </a>
                					<a onclick="javascript:location.href='${path}/board/qnaComDelete?qnaNum=${qna.QNANUM}&commentNum=${c.COMMENTNUM}';"> 삭제 </a>
                				</c:if>
               				</div>
               				<ㅊ:ㅑ
              				<div class="commentContent"> 
              					<p id="commentContent"style="margin-top: 10px;"><c:out value="${c.COMCONTENT}"/></p>
              				</div>
				   		</div>
					</div>
				</div>
			</div>
        	 </c:forEach>
    </div>
    </div>

​
<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>​​