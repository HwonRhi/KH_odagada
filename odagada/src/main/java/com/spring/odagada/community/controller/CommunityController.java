package com.spring.odagada.community.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.odagada.community.model.service.CommunityService;
import com.spring.odagada.community.model.vo.ChatRoomVo;
import com.spring.odagada.community.model.vo.MessageVo;
import com.spring.odagada.member.model.vo.Member;

@Controller
public class CommunityController {
	@Autowired
	CommunityService service;
	
	private Logger logger = LoggerFactory.getLogger(CommunityController.class);
	
	@RequestMapping("/community/moveChatting.do")
	public String test() {
		return "community/chatView";
	}
	
	//채팅방이 아닌곳에서 안읽은 메시지 확인용
	@RequestMapping("/community/checkMsg.do")
	public ModelAndView jsutCheckMsg(ModelAndView mv, String myId) 
	{
		int unIsReadMsg = service.jsutCheckMsg(myId);
		mv.addObject("unIsReadMsg", unIsReadMsg);
		mv.setViewName("jsonView");
		
		return mv;
	}

	@RequestMapping("/community/createRoomClick.do")
	public ModelAndView createRoomClick(ModelAndView mv, String chatUser, HttpServletRequest request)
	{
		Member m =(Member)request.getSession().getAttribute("logined");
		Map<String,String> roomIdData = new HashMap<String, String>();
		roomIdData.put("myId", m.getMemberId());
		roomIdData.put("chatUser", chatUser);
		logger.debug(chatUser+"와 채팅방을 검사합니다");
				
		String roomIdCheck = service.roomIdCheck(roomIdData);
		if(roomIdCheck!=null)
		{
			logger.debug(chatUser+"와 채팅방이 이미 존재한다. 채팅방 이름 : "+roomIdCheck);
			List<ChatRoomVo> chatRooms = service.bringChatRooms(m.getMemberId());
			for(ChatRoomVo i:chatRooms)
			{
				logger.debug("채팅방 정보들"+i);
				i.setcDate(i.getcDate().substring(0, 14));
			}
			
			mv.addObject("chatRooms", chatRooms);
			
			//상대방 정보와 채팅 내역 가져옴
			List<Map<String,String>> chatContent = service.bringMsg(roomIdCheck);

			if(!(chatContent.isEmpty())) 
			{
				logger.debug("방도 있고 메시지도 있다"+chatContent);
				mv.addObject("chatContent", chatContent);
				for(int i=0;i<chatContent.size();i++) {
					logger.debug("MAP데이터22"+chatContent.get(i));
				}
			}
			else 
			{
				logger.debug("방은 있지만 메시지가 없다. 상대 정보만 가져옴");
				List<Map<String,String>> member = service.bringUserInfo(roomIdData);
				logger.debug(member+"");
				mv.addObject("chatMember", member);
			}
		}else {
			logger.info("채팅방 존재하지 않음");
			//roomId = 내아이디,상대아이디 로 구성
			roomIdData.put("roomId", String.join(",",m.getMemberId(), chatUser));
			int insertRoomId = service.insertRoomId(roomIdData);
			
			List<ChatRoomVo> chatRooms = service.bringChatRooms(m.getMemberId());
			for(ChatRoomVo i:chatRooms)
			{
				logger.debug("채팅방 정보들"+i);
				i.setcDate(i.getcDate().substring(0, 14));
			}
			mv.addObject("chatRooms", chatRooms);
			List<Map<String,String>> member = service.bringUserInfo(roomIdData);
			mv.addObject("chatMember", member);
		}
		mv.setViewName("community/chatting");
		return mv;
	}

	//채팅방 입장
	@RequestMapping("/community/chatting.do")
	public ModelAndView chatting(HttpServletRequest request, ModelAndView mv) 
	{
		Member m = (Member)request.getSession().getAttribute("logined");
		
		List<ChatRoomVo> chatRooms = service.bringChatRooms(m.getMemberId());
		
		for(ChatRoomVo i:chatRooms) 
		{
			logger.debug("채팅방 정보들"+i);
			i.setcDate(i.getcDate().substring(0, 14));
		}
		
		mv.addObject("chatRooms", chatRooms);
		mv.setViewName("community/chatting");
		
		return mv;
	}
	
	//메시지 왔을 때 채팅방 최신화, (원본 세이브)!	
	@RequestMapping("/community/updateRoom.do")
	public ModelAndView updateRoom(HttpServletRequest request, ModelAndView mv, @RequestParam(value="roomId",required=false, defaultValue="null") String roomId)
	{
		Member m = (Member)request.getSession().getAttribute("logined");		
		logger.debug("updateRoom에 들어온 roomId : "+roomId);
		
		if(!roomId.equals("null")) 
		{
			Map<String,Object> isreadData = new HashMap();
			isreadData.put("loginId", m.getMemberId());
			isreadData.put("roomId", roomId);
			int rs = service.checkedMessage(isreadData);
		}
		List<ChatRoomVo> chatRooms = service.bringChatRooms(m.getMemberId());
		
		for(ChatRoomVo i:chatRooms) 
		{
			logger.debug("채팅방 정보들"+i);
		}
		
		mv.addObject("chatRooms", chatRooms);
		mv.setViewName("jsonView");
		
		return mv;
	}
	
	//채팅방 클릭 했을 때
	@RequestMapping("/community/bringMessage.do")
	public ModelAndView bringMsg(String roomId, ModelAndView mv, HttpServletRequest request) throws UnsupportedEncodingException
	{
		
		Member m =(Member) request.getSession().getAttribute("logined");
		Map<String,Object> isreadData = new HashMap();
		
		isreadData.put("loginId", m.getMemberId());
		isreadData.put("roomId", roomId);
		
		List<Map<String,String>> chatContent = service.bringMsg(roomId);
		int rs = service.checkedMessage(isreadData);
		
		for(Map<String,String> i:chatContent) 
		{
			logger.debug("MAP데이터 : "+i);
		}
		
		
		mv.addObject("chatList",chatContent);
		mv.setViewName("jsonView");
		
		return mv;
	}

	@RequestMapping("/community/notifyForm.do")
	public ModelAndView notifyForm(HttpServletRequest request, HttpSession session)
	{
		ModelAndView mv=new ModelAndView();
		Member m = (Member)session.getAttribute("logined");
		
		if(m!=null)
		{
			mv.setViewName("community/notifyForm");
			return mv;
		}
		else
		{
			mv.setViewName("/common/msg");
			mv.addObject("msg","로그인 후 이용해주세요.");
			mv.addObject("loc","/member/loginForm.do");
			
			return mv;
		}
	}
	
	@Transactional
	@RequestMapping("/community/notifyFormEnd.do")
	public String notifyFormEnd(HttpServletRequest request, String nContent, Model model)
	{
		int memberNum=Integer.parseInt(request.getParameter("memberNum"));
		
		Map<String,Object> notify=new HashMap();
		notify.put("nContent",nContent);
		notify.put("memberNum", memberNum);
		
		int result=service.insertNotify(notify);
		String msg="";
		String loc="/";
		if(result>0)
		{
			msg="신고완료";
		}
		else
		{
			msg="신고실패";
		}
		model.addAttribute("msg",msg);
		model.addAttribute("loc",loc);
		return "common/msg";
	}
	
	@RequestMapping("/community/reviewForm.do")
	public ModelAndView reviewForm(HttpServletRequest request, HttpSession session)
	{
		ModelAndView mv=new ModelAndView();
		Member m = (Member)session.getAttribute("logined");
		
		if(m!=null)
		{
			mv.setViewName("community/reviewForm");
			return mv;
		}
		else
		{
			mv.setViewName("/common/msg");
			mv.addObject("msg","로그인 후 이용해주세요.");
			mv.addObject("loc","/member/loginForm.do");
			
			return mv;
		}
		
	}
	
	@Transactional
	@RequestMapping("/community/reviewFormEnd.do")
	public String reviewFormEnd(HttpServletRequest request, String rContent, int rGrade, Model model)
	{
		int memberNum = Integer.parseInt(request.getParameter("memberNum"));
		
		Map<String, Object> review=new HashMap();
		/*map.put("driverNum", driverNum);*/
		review.put("rContent", rContent);
		review.put("rGrade", rGrade);
		review.put("memberNum", memberNum);
		
		int result=service.insertReview(review);
		
		String msg="";
		String loc="/";
		if(result>0)
		{
			msg="등록성공";
		}
		else
		{
			msg="등록실패";
		}
		model.addAttribute("msg",msg);
		model.addAttribute("loc",loc);
		return "common/msg";
	}
	
	@RequestMapping("community/reviewView.do")
	public ModelAndView reviewView(HttpServletRequest request, HttpSession sessiong,int memberNum)
	{
		ModelAndView mv=new ModelAndView();
		List<Map<String, Object>> map = service.selectReviewList(memberNum);
		logger.debug("내게 달린 리뷰 보기"+map);
		mv.addObject("list",map);
		mv.setViewName("community/reviewView");
		return mv;
	}
	
	@RequestMapping("community/myReviewView.do")
	public ModelAndView myReviewView(HttpServletRequest request, HttpSession session,int memberNum)
	{
		ModelAndView mv=new ModelAndView();
		List<Map<String, Object>> map = service.selectMyReviewList(memberNum);
		logger.debug("내가 남긴 리뷰 보기"+memberNum);
		mv.addObject("list",map);
		mv.setViewName("community/myReviewView");
		return mv;
	}
	
	@RequestMapping("/community/reviewModify.do")
	public ModelAndView reviewModify(HttpServletRequest request, HttpSession session,int carpoolNum) 
	{
		ModelAndView mv=new ModelAndView();
		Map<String,Object> map=service.selectReview(carpoolNum);
		mv.addObject("review",map);
		mv.setViewName("community/reviewModify");
		
		return mv;
	}
	
	@Transactional
	@RequestMapping("/community/reviewModifyEnd.do")
	public String reviewModifyEnd(HttpServletRequest request, String rContent, int rGrade, int carpoolNum, Model model)
	{
		Map<String,Object> map=service.selectReview(carpoolNum);
		
		Map<String, Object> review=new HashMap();
		review.put("rContent", rContent);
		review.put("rGrade", rGrade);
		review.put("carpoolNum", carpoolNum);
		
		int result=service.updateReview(review);
		
		String msg="";
		String loc="/";
		if(result>0)
		{
			msg="수정성공";
		}
		else
		{
			msg="수정실패";
		}
		model.addAttribute("msg",msg);
		model.addAttribute("loc",loc);
		return "common/msg";
	}
	
	@Transactional
	@RequestMapping("/community/reviewDelete.do")
	public String reviewDelete(int carpoolnum,Model model)
	{
		int result=service.deleteReview(carpoolnum);
		String msg="";
		String loc="/";
		if(result>0)
		{
			msg="삭제성공";
		}
		else
		{
			msg="삭제실패";
		}
		model.addAttribute("msg",msg);
		model.addAttribute("loc",loc);
		
		return "common/msg";
	}
}
