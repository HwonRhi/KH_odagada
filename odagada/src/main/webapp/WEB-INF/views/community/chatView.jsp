<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<jsp:include page="/WEB-INF/views/common/header.jsp">
   <jsp:param value="오다가다 타는 카풀" name="pageTitle"/>
</jsp:include>
<script src="https://cdn.jsdelivr.net/sockjs/1/sockjs.min.js"></script>
<script src="https://code.jquery.com/jquery-latest.min.js"></script>

<div>
	<input type="text" id="messageinput">
</div>

<div>
	<button onclick="openSocket();">채팅연결하기</button>
	<button onclick="MessageSend();">전송</button>
	<button onclick="closeSocket();">채팅 끊기</button>
</div>

<div id="message"></div>

<button onclick="moveChatting('driverTest1')">채팅방 생성하기</button>




<script>

	function moveChatting(chatUser)
	{
		location.href="${path}/community/createRoomClick.do?chatUser="+chatUser;	
	}

	var ws;
	var url="http://localhost:9090/odagada/echo";
	
	function openSocket()
	{
		 if(ws!==undefined && ws.readyState!==WeSocket.CLOSED)
		{
			writeResponse("WebSocket is already opend");
			return;
		}; 
		
		ws = new SockJS(url);
		
		ws.onmessage = function(event){
			//writeResponse(event.data);
			jsonTextParse(event.data);
		};
		
		ws.onopen = function(event){
			var myid = {"myId" : "${logined.memberId}"};
			ws.send(JSON.stringify(myid));
			
		};
		
		//소켓이 닫히고 실행되는 함수, 즉 이 메소드가 실행될 땐 이미 소켓이 닫힌 상태
		ws.onclose = function(){
			writeResponse("Connection Closed");
		};
		
	}
	function jsonTextParse(jsonText){
    	var json = JSON.parse(jsonText);
    	writeResponse(json.sender+" : "+json.text);
    	
    	console.log("파이널에 합치는중 : "+json.text);
    }
	
	function MessageSend()
	{
		var jsonData ={};
		jsonData.text = $('#messageinput').val();
		jsonData.sender ="${logined.memberId}";
		jsonData.reciver = "test2";
		jsonData.roomId = "test1,test2";
		ws.send(JSON.stringify(jsonData));
		//ws.send(jsonData);
		console.log(typeof JSON.stringify(jsonData));
		$('#messageinput').val("");
	}
	
	
	function closeSocket()
	{
		//세션 닫기전에 삭제할 id를 보내어 userList지우고 세션 닫기
		//로그아웃전에 삭제할 id를 보내어 userList지우고 세션 닫기로 바꿀 예정
		var deleteId = {"deleteId" : "${logined.memberId}"};
		ws.send(JSON.stringify(deleteId));
		ws.close();
	}
	
	function writeResponse(text){
		message.innerHTML +="<br/>"+text;
	}
	
</script>


<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>