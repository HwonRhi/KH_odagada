package com.spring.odagada.member.controller;


import static com.spring.odagada.common.PageFactory.getPageBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;


import com.spring.odagada.carpool.model.service.CarpoolService;
import com.spring.odagada.common.exception.BoardException;
import com.spring.odagada.community.model.service.CommunityService;
import com.spring.odagada.driver.model.service.DriverService;
import com.spring.odagada.driver.model.vo.CarImage;
import com.spring.odagada.member.googleApi.FaceDetectApp;
import com.spring.odagada.member.model.service.MemberService;
import com.spring.odagada.member.model.vo.Member;

import oracle.net.aso.l;


@SessionAttributes(value= {"logined", "driver"})
@Controller
public class MemberController {
	
	private Logger logger=LoggerFactory.getLogger(MemberController.class);
	
	@Autowired
	MemberService service;
	
	@Autowired
	CarpoolService cService;
	
	@Autowired
	DriverService dService;
	
	//???????????? ????????? ??????
	@Autowired
	BCryptPasswordEncoder pwEncoder;
	
	@Autowired
	CommunityService comService;
	
	
	@RequestMapping("/member/kakaoIdCK")
	public ModelAndView kakaoIdCK(String kakaoId, String kakaoName, ModelAndView mv) 
	{
		Member m = new Member();
		
		m.setMemberId("kakao_"+kakaoId);
		logger.debug("????????? ????????? ?????? ?????????"+m);
		m = service.kakaoIdCK(m);
		logger.debug("????????? ????????? ?????? ????????? ???"+m);
		
		if(m==null) 
		{
			mv.addObject("result", "N");
		}else{
			mv.addObject("logined", m);
			mv.addObject("result", "Y");
		}
		mv.setViewName("jsonView");
		return mv;
	}
	
	@RequestMapping("/member/kakaoSignUpEnd.do")
	public String kakaoSignUpEnd(String kakaoId, String kakaoName,Model model, Member m, HttpServletRequest request, MultipartFile upFile) throws Exception 
	{
		System.out.println(kakaoId+" "+kakaoName+" "+m);
		m.setMemberId("kakao_"+kakaoId);
		m.setMemberName(kakaoName);
		m.setMemberPw(" ");
		//????????? ??? ????????????
		String oriPw=m.getMemberPw();
		logger.debug("????????? ???:"+oriPw);
		logger.debug("????????? ???: "+pwEncoder.encode(oriPw));	
		m.setMemberPw(pwEncoder.encode(oriPw));		
		//????????????
		String email1=request.getParameter("email1");
		String email2=request.getParameter("email2");
		String email=email1+"@"+email2;	
		m.setEmail(email);			
		//????????????
		String phone1=request.getParameter("phone1");
		String phone2=request.getParameter("phone2");
		String phone=phone1+phone2;
		m.setPhone(phone);		
		//????????? ?????? ???????????? ??????
		String sd=request.getSession().getServletContext().getRealPath("/resources/upload/profile");

		ModelAndView mv = new ModelAndView();
		
		if(!upFile.isEmpty()) {
			//????????? ??????(ReName)
			String oriFileName=upFile.getOriginalFilename();
			String ext=oriFileName.substring(oriFileName.lastIndexOf("."));
			
			//rename ??????
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
			int rdv=(int)(Math.random()*1000);
			String reName=sdf.format(System.currentTimeMillis())+"_"+rdv+ext;
			
			//profile ?????? ??????
			try {
				upFile.transferTo(new File(sd+"/"+reName));
			}catch(IllegalStateException | IOException e){
				e.printStackTrace();
			}		
			m.setProfileImageOri(oriFileName);
			m.setProfileImageRe(reName);
		}
		
		logger.debug("kakao ???????????? : "+m);
		StringBuffer odagada=request.getRequestURL();
		service.insertMember(m, odagada);
		
		String msg="??????????????? ?????????????????????. ?????????????????? ?????? ????????? ??????????????????.";
		String loc="/";
		model.addAttribute("msg", msg);
		model.addAttribute("loc", loc);
		return "common/msg";	
	}
	
	@RequestMapping("/member/kakaoLogin.do")
	public ModelAndView kakaoLogin(String kakaoId, String kakaoName, ModelAndView mv) 
	{
		System.out.println(kakaoId+" "+kakaoName);
		String[] kakaoName2 = kakaoName.split("\"");
		mv.addObject("kakaoId", kakaoId);
		mv.addObject("kakaoName", kakaoName2[1]);
		mv.setViewName("/member/kakaoSignUpForm");
		return mv;
	}
	
	// email ????????????
	@ResponseBody
	@RequestMapping("/member/checkEmail.do")
	public String checkEmail(String email) {
		logger.debug("???????????? ??????:" + email);
		int emailNum = service.checkEmail(email);
		String result = "";
		if (emailNum == 0) {
			result = "ok";
		} else {
			result = "no";
		}
		return result;
	}
			
	// ????????? ????????????
	@RequestMapping("/member/checkId.do")
	public ModelAndView checkId(String memberId, ModelAndView mv) throws UnsupportedEncodingException {
		Map map = new HashMap();
		boolean isId = service.checkId(memberId) == 0 ? false : true;
		map.put("isId", isId);

		mv.addAllObjects(map);
		mv.addObject("char", URLEncoder.encode("?????????", "UTF-8"));
		mv.addObject("num", 1);
		mv.setViewName("jsonView");
		return mv;
	}

	// ????????????????????? ??????
	@RequestMapping("/member/signUp.do")
	public String signUp() {
		return "member/signUpForm";
	}

	// ?????????????????? ?????????
	@ResponseBody
	@RequestMapping("/member/profileTest.do")
	public String[] prifileTeset(MultipartFile upFile, HttpServletRequest request) throws GeneralSecurityException {
		// ?????? ????????? ?????? ?????????
		String sav = request.getSession().getServletContext().getRealPath("/resources/upload/profile/temp");

		String[] result = new String[2];

		File f = new File(sav);
		// ????????? ????????? ?????????
		if (!f.exists()) {
			f.mkdirs();
		}

		if (!upFile.isEmpty()) {
			// ????????? ??????(ReName)
			String oriFileName = upFile.getOriginalFilename();
			String ext = oriFileName.substring(oriFileName.lastIndexOf("."));
			// rename ??????
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
			int rdv = (int) (Math.random() * 1000);
			String reName = sdf.format(System.currentTimeMillis()) + "_" + rdv + ext;
			// profile ?????? ??????
			try {
				upFile.transferTo(new File(sav + "/" + reName));
				String path = sav + "/" + reName;
				result[0] = path;
				FaceDetectApp.main(result);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				result[1] = "no";
				logger.debug("????????? ????????????.");
			} catch (IndexOutOfBoundsException e) {
				result[1] = "many";
				logger.debug("???????????????.");
			}
			File file = new File(sav + "/" + reName);// ?????? ????????? ????????? ??????
			if (file.exists()) {
				file.delete();
			}
		}
		return result;
	}
  
	// ????????????
	@RequestMapping("/member/signUpEnd.do")
	public String signUpEnd(Model model, Member m, HttpServletRequest request, MultipartFile upFile) throws Exception {
		logger.debug("??????: " + m);
		// ????????? ??? ????????????
		String oriPw = m.getMemberPw();
		logger.debug("????????? ???:" + oriPw);
		logger.debug("????????? ???: " + pwEncoder.encode(oriPw));
		m.setMemberPw(pwEncoder.encode(oriPw));
		// ????????????
		String email1 = request.getParameter("email1");
		String email2 = request.getParameter("email2");
		String email = email1 + "@" + email2;
		m.setEmail(email);
		// ????????????
		String phone1 = request.getParameter("phone1");
		String phone2 = request.getParameter("phone2");
		String phone = phone1 + phone2;
		m.setPhone(phone);
		// ????????? ?????? ?????????
		String sd = request.getSession().getServletContext().getRealPath("/resources/upload/profile");

		ModelAndView mv = new ModelAndView();

		if (!upFile.isEmpty()) {
			// ????????? ??????(ReName)
			String oriFileName = upFile.getOriginalFilename();
			String ext = oriFileName.substring(oriFileName.lastIndexOf("."));

			// rename ??????
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
			int rdv = (int) (Math.random() * 1000);
			String reName = sdf.format(System.currentTimeMillis()) + "_" + rdv + ext;

			// profile ?????? ??????
			try {
				upFile.transferTo(new File(sd + "/" + reName));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			m.setProfileImageOri(oriFileName);
			m.setProfileImageRe(reName);
		}

		StringBuffer odagada = request.getRequestURL();
		service.insertMember(m, odagada);

		String msg = "??????????????? ?????????????????????. ?????????????????? ?????? ????????? ??????????????????.";
		String loc = "/";
		model.addAttribute("msg", msg);
		model.addAttribute("loc", loc);
		return "common/msg";
	}

	// ????????? ?????? ?????? ????????????
	@RequestMapping(value = "/emailConfirm.do", method = RequestMethod.GET)
	public ModelAndView emailConfirm(String email, String memberId) {
		Map<String, String> map = new HashMap();

		// ???????????? ??????????????? Y ??? ????????? ???????????? ??????, ??????????????? Y ???????????? ????????? ??????.
		map.put("isEmailAuth", "Y");
		map.put("email", email);
		map.put("memberId", memberId);

		int result = service.updateStatus(map);
		ModelAndView mv = new ModelAndView();

		String msg = "";
		String loc = "/";

		logger.debug("????????? ?????? ??????????????? ????????" + result);
		if (result > 0) {
			msg = "????????? ????????? ?????????????????????.";
		} else {
			mv.addObject("??????????????? ???????????????.", msg);
			mv.setViewName("redirect:/");
		}
		mv.addObject("msg", msg);
		mv.addObject("loc", loc);
		mv.setViewName("member/successAuth");
		return mv;
	}
    
	// ????????? ?????????
	@RequestMapping("/member/loginForm.do")
	public String loginForm() {
		return "member/loginForm";
	}

	// ????????? ?????????
	@RequestMapping("/member/loginForm2.do")
	public String loginForm2() {
		return "member/loginForm2";
	}

	// ?????????
	@RequestMapping("/member/login.do")
	public ModelAndView login(String memberId, String memberPw, Model model) {
		Map<String, String> login = new HashMap();
		login.put("memberId", memberId);
		login.put("memberPw", memberPw);

		Map<String, String> result = service.login(login);

		ModelAndView mv = new ModelAndView();

		Member m = service.selectMember(memberId);
		logger.debug("??????: " + m);
		if (m == null) {
			mv.addObject("msg", "????????? ????????? ????????????.");
			mv.addObject("loc", "/member/loginForm2.do");
			mv.setViewName("common/msg");
		} else if (m.getMemberStatus().equals("N")) {
			mv.addObject("msg", "????????? ?????? ?????????.");
			mv.addObject("loc", "/");
			mv.setViewName("common/msg");
		} else {
			Map<String, String> driver = dService.selectDriverOne(m.getMemberNum());
			logger.debug("????????? ?????? ??????" + m);
			logger.debug("????????? ?????????" + m.getIsAdmin());
			if (result != null) {
				if (pwEncoder.matches(memberPw, result.get("MEMBERPW"))) {
					Map<String, String> black = service.checkBlack(result.get("MEMBERID"));

					// ????????? ???????????? ???????????? ??????
					if (black != null && black.get("BLACKID").equals(result.get("MEMBERID"))) {
						mv.addObject("msg", "????????? ???????????????. ????????? " + black.get("BLACKPUNISH"));
						mv.addObject("loc", "/member/loginForm2.do");
						mv.setViewName("common/msg");
					} else {
						if (m.getCarMsg() != null) {
							mv.addObject("driver", driver);
							mv.addObject("logined", m);
							mv.addObject("msg", m.getCarMsg());
							mv.addObject("loc", "/");
							mv.setViewName("common/msg");
						} else {
							logger.debug("????????? ????????????" + driver);
							mv.addObject("driver", driver);
							mv.addObject("logined", m);
							mv.setViewName("redirect:/");
						}
					}
				} else {
					mv.addObject("msg", "??????????????? ???????????? ????????????.");
					mv.addObject("loc", "/member/loginForm2.do");
					mv.setViewName("common/msg");
				}
			}
		}
		return mv;
	}

   
	// ????????????(????????????)
	@RequestMapping("/member/logout.do")
	public String logout(SessionStatus status) {
		if (!status.isComplete()) {
			status.setComplete();
		}
		return "redirect:/";
	}
   
	// ???????????????
	@RequestMapping("/member/myInfo.do")
	public ModelAndView myInfo(HttpSession session, ModelAndView mav) {
		mav.setViewName("member/myInfo");

		Member m = (Member) session.getAttribute("logined");
		int memberNum = m.getMemberNum();
		Map<String, String> d1 = dService.selectDriverOne(memberNum);
		/* Driver d = (Driver)session.getAttribute("driver"); */
		m = service.selectMember(m.getMemberId());

		/* logger.debug("?????? ?????????????"+d); */
		logger.debug("?????? ?????????????" + d1);
		mav.addObject("logined", m);
		/* mav.addObject("driver",d); */
		mav.addObject("driver", d1);
		return mav;
	}
   
	// ???????????? ??????(ajax ...), ?????? ??????
	@ResponseBody
	@RequestMapping("/member/checkPw.do")
	public String checkPw(HttpServletResponse response, String password, String answer, HttpSession session,
			SessionStatus status) {
		logger.debug("???????????? pw ???: " + password);
		Member m = (Member) session.getAttribute("logined");
		String result = "";

		if (pwEncoder.matches(password, m.getMemberPw())) {
			if (answer.equals("delete")) {
				// delete ??????(?????????>>??????????????? ?????????)
				int rs = service.deleteMember(m.getMemberNum());
				if (rs != 0) {
					status.setComplete();
					result = "delete";
				} else {
					result = "noDelete";
				}
			} else {
				logger.debug("ok");
				result = "ok";
			}
		} else {
			logger.debug("no");
			result = "no";
		}
		return result;
	}

	// ??? ?????? ???????????????
	@RequestMapping("/member/updateInfo.do")
	public String updateInfo(Model model) {
		return "member/updateForm";
	}

	// ??? ????????????
	@RequestMapping("/member/updateProfile.do")
	public ModelAndView updateInfoEnd(HttpServletRequest request, HttpSession session, MultipartFile upFile) {

		logger.debug("????????????  ???????" + upFile);
		// ????????? ?????? ???????????? ??????
		String sd = request.getSession().getServletContext().getRealPath("/resources/upload/profile");

		ModelAndView mv = new ModelAndView("common/msg");

		Member m = (Member) session.getAttribute("logined");

		// ?????? ?????? ????????? ????????? ??????
		String oldFile = m.getProfileImageRe();

		if (!upFile.isEmpty()) {
			// ????????? ??????(ReName)
			String oriFileName = upFile.getOriginalFilename();
			String ext = oriFileName.substring(oriFileName.lastIndexOf("."));

			// rename ??????
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
			int rdv = (int) (Math.random() * 1000);
			String reName = sdf.format(System.currentTimeMillis()) + "_" + rdv + ext;

			// profile ?????? ??????
			try {
				upFile.transferTo(new File(sd + "/" + reName));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			// ????????? ????????? ????????? ??????(Ori, Re)
			m.setProfileImageOri(oriFileName);
			m.setProfileImageRe(reName);
		}
		int result = service.updateMember(m);

		if (result > 0) {
			// ?????? ?????? ????????? ????????? ??????
			File file = new File(sd + "/" + oldFile);
			if (file.exists()) {
				file.delete();
				Member nM = service.selectMember(m.getMemberId());
				logger.debug("???????????? ??? : " + nM);
				mv.addObject("logined", nM);
			}
			mv.addObject("msg", "??????????????? ?????????????????????.");
			mv.addObject("loc", "/member/updateInfo.do");
		} else {

			mv.addObject("msg", "???????????? ??????!");
			mv.addObject("loc", "/member/updateInfo.do");
		}
		return mv;
	}
   
	// ID ?????? ??????
	@RequestMapping("/member/findId")
	public String findId() {
		return "member/findId";
	}

	// ID ??????
	@RequestMapping("/member/findId.do")
	public ModelAndView findIdEnd(String memberName, String email, Model model) {
		ModelAndView mv = new ModelAndView();

		Map<String, String> findId = new HashMap();
		findId.put("memberName", memberName);
		findId.put("email", email);

		Map<String, String> id = service.findId(findId);

		if (id != null) {
			String resultId = id.get("MEMBERID");
			mv.addObject("memberId", resultId);
			mv.setViewName("member/resultId");
		} else {
			mv.addObject("msg", "?????? ????????? ????????????.");
			mv.addObject("loc", "/member/findId");
			mv.setViewName("common/msg");
		}
		return mv;
	}

	// ???????????? ?????? ???
	@RequestMapping("/member/findPw")
	public String findPassword() {
		return "member/findPw";
	}
	
	// ???????????? ??????
	@RequestMapping("/member/findPw.do")
	public ModelAndView findPassword(HttpServletRequest request, String memberId, String email, String memberName)
			throws Exception {
		Map<String, String> info = new HashMap();
		info.put("memberId", memberId);
		info.put("email", email);
		info.put("memberName", memberName);

		Map<String, String> findPw = service.findPw(info);
		ModelAndView mv = new ModelAndView();

		String msg = "";
		String loc = "/";
		if (findPw != null) {
			int ran = new Random().nextInt(100000) + 1000;
			String sendPw = String.valueOf(ran);
			String dbPw = pwEncoder.encode(sendPw);
			info.put("memberPw", dbPw);
			info.put("sendPw", sendPw);

			StringBuffer odagada = request.getRequestURL();
			service.sendPw(info, odagada);
			msg = "???????????? ??????????????? ?????? ??????????????? ?????????????????????.";
			mv.addObject("msg", msg);
			mv.addObject("loc", loc);
			mv.setViewName("common/msg");
		} else {
			msg = "???????????? ????????? ????????????.";
			mv.addObject("msg", msg);
			mv.addObject("loc", "/member/findPw");
		}
		return mv;
	}
	
	// ?????? ????????????
	@RequestMapping("/member/mailAuth")
	public String mailAuth(HttpSession session, HttpServletRequest request) throws Exception {
		Member m = (Member) session.getAttribute("logined");
		StringBuffer odagada = request.getRequestURL();
		service.mailAuth(m, odagada);
		return "redirect:/";
	}
		
    @ResponseBody
    @RequestMapping("/member/smsCheck")
    public String smsCheck(HttpSession session, String code) {
    	Member m = (Member)session.getAttribute("logined");
    	
    	String saveCode = service.getPhoneCode(m.getMemberNum());
    	
    	if(pwEncoder.matches(code, saveCode)) {
    		int result = service.updateYPhoneStatus(m.getMemberNum());
    		
    		if(result > 0) {
    			return "ok";
    		}else {
    			return "no";
    		}
    	}else {
    		return "no";
    	}
    }
    
	// ????????? ?????? ??????
	@ResponseBody
	@RequestMapping("/member/phoneCheck.do")
	public String phoneCheck(String phone) {
		int result = service.checkPhone(phone);
		String isPhone = "";
		if (result > 0) {
			isPhone = "N";
		} else {
			isPhone = "Y";
		}
		return isPhone;
	}

   @RequestMapping("/member/myCarpool")
   public ModelAndView myCarpool(HttpSession session, @RequestParam(value="cPage", required=false, defaultValue="0") int cPage) {
	   
	   int numPerPage = 5;
	   
	   ModelAndView mav = new ModelAndView("member/myCarpool");
	   
	   Member m = (Member)session.getAttribute("logined");
	   int memberNum = m.getMemberNum();
	   Map<String,String> d = dService.selectDriverOne(memberNum);
	   
	   logger.debug("???????????? ???????????? "+d);
	   
	   List<Map<String, String>> list = cService.selectCarpoolList(m.getMemberNum(), cPage, numPerPage);
	   int totalCount = cService.selectCarpoolCount(m.getMemberNum());
	   
	   mav.addObject("carpoolList", list);
	   mav.addObject("driver",d);
	   mav.addObject("pageBar", getPageBar(totalCount, cPage, numPerPage, "/odagada/member/myCarpool"));	   
	   
	   return mav;
   }
   
    @ResponseBody
	@RequestMapping("/member/sendSms")
	public String sendSms(HttpSession session, String receiver) {
		// ?????? ?????? ??????
		int rand = (int) (Math.random() * 899999) + 100000;

		logger.debug(receiver);
		logger.debug(rand + "");

		String phoneCode = pwEncoder.encode(rand + "");
		Member m = (Member) session.getAttribute("logined");
		m.setPhoneCode(phoneCode);
		int result = service.updatePhoneCode(m);

		if (result > 0) {
			// ?????? ????????? (???????????? api ??????)
			String hostname = "api.bluehouselab.com";
			String url = "https://" + hostname + "/smscenter/v1.0/sendsms";

			CredentialsProvider credsProvider = new BasicCredentialsProvider();
			credsProvider.setCredentials(new AuthScope(hostname, 443, AuthScope.ANY_REALM),
					new UsernamePasswordCredentials("odagadaa", "c16067ba511b11e9b5960cc47a1fcfae"));

			AuthCache authCache = new BasicAuthCache();
			authCache.put(new HttpHost(hostname, 443, "https"), new BasicScheme());

			HttpClientContext context = HttpClientContext.create();
			context.setCredentialsProvider(credsProvider);
			context.setAuthCache(authCache);

			DefaultHttpClient client = new DefaultHttpClient();

			try {
				HttpPost httpPost = new HttpPost(url);
				httpPost.setHeader("Content-type", "application/json; charset=utf-8");
				String json = "{\"sender\":\"01077682019\",\"receivers\":[\"" + receiver
						+ "\"],\"content\":\"???????????? ????????? ?????? ?????? ?????? : " + rand + "\"}";

				logger.info(json);
				
				StringEntity se = new StringEntity(json, "UTF-8");
				httpPost.setEntity(se);

				HttpResponse httpResponse = client.execute(httpPost, context);
				System.out.println(httpResponse.getStatusLine().getStatusCode());

				InputStream inputStream = httpResponse.getEntity().getContent();
				if (inputStream != null) {
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
					String line = "";
					while ((line = bufferedReader.readLine()) != null)
						System.out.println(line);
					inputStream.close();
				}
			} catch (Exception e) {
				System.err.println("Error: " + e.getLocalizedMessage());
			} finally {
				client.getConnectionManager().shutdown();
			}
			return "true";
		} else {
			return "false";
		}
	}
    
    //??? ???????????? ??????
   @RequestMapping("/member/myDriver")
   public ModelAndView myDriver(HttpSession session) {
	   
	   
	   ModelAndView mv = new ModelAndView();
	   
	   Member m = (Member)session.getAttribute("logined");	   
	   int memberNum = m.getMemberNum();	   
	   Map<String, String> driver = dService.selectDriverOne(memberNum);
	   
	   if(driver!=null)
	   {
		   logger.debug("??? ??????"+driver.get("CARNUM"));

		   String carNum = driver.get("CARNUM");
		   List<Map<String,String>> carImg = dService.selectCarImg(carNum);
		   
		   String a = driver.get("LICENSENUM").substring(0,3);
		   String b = "**-******";
		   String c = driver.get("LICENSENUM").substring(13);
		   
		   String licenseNum = a+b+c;
		   
		   logger.debug("?????????"+carNum);
		   logger.debug("??? ?????????"+carImg);
		   logger.debug("????????? ??????"+carNum.length());
		   carImg.get(0).put("active", "active");
		   
		   mv.addObject("carImg",carImg);
		   mv.addObject("licenseNum",licenseNum);
	   }
	     
	   logger.debug("?????????"+driver);
	   mv.addObject("driver", driver);
	   mv.setViewName("member/myDriver");
	   
	   return mv;
   }
   //??? ???????????? ?????? ??????
   @RequestMapping("/member/myDriverModify")
   public ModelAndView myDriverModify(HttpSession session)
   {
	   ModelAndView mv = new ModelAndView();
	   Member m = (Member)session.getAttribute("logined");
	   Map<String, String> driver = dService.selectDriverOne(m.getMemberNum());
	   
	   logger.debug("??? ??????"+driver.get("CARNUM"));

	   String carNum = driver.get("CARNUM");
	   List<Map<String,String>> carImg = dService.selectCarImg(carNum);
	   
	   String a = driver.get("LICENSENUM").substring(0,3);
	   String b = "**-******";
	   String c = driver.get("LICENSENUM").substring(13);
	   String licenseNum = a+b+c;
	   
	   carImg.get(0).put("active", "active");
	   
	   mv.addObject("carImg",carImg);
	   mv.addObject("licenseNum",licenseNum);
	   logger.debug("?????????"+driver);
	   mv.addObject("driver", driver);
	   mv.setViewName("member/myDriverModify");
	   
	   return mv;
	   
   }
   //??? ???????????? ???????????? ??????
   @RequestMapping("/member/myDriverModifyEnd")
   public ModelAndView myDriverModifyEnd(String oldCarNum,String carNum,String licenseNum,String carModel,MultipartFile [] upFile,
		   HttpServletRequest request, HttpServletResponse response,int memberNum) throws BoardException
   {
	  String sd = request.getSession().getServletContext().getRealPath("/resources/upload/car"); 
	  ModelAndView mv = new ModelAndView();
	  Map<String,String> map = dService.selectDriverOne(memberNum);
	  List<String> img = dService.selectImgRe(oldCarNum);
	  
	  for(int i=0;i<img.size();i++) {
		  logger.debug("?????? ????????? ??????"+img.get(i));		  
	  }	  
	  
	  Map<String,Object> driver = new HashMap();	  
	  driver.put("licenseNum", licenseNum);
	  driver.put("carModel", carModel);
	  driver.put("carNum", carNum);
	  driver.put("memberNum", memberNum);
	  
	  int imgOrder = 0;
	  
	  ArrayList<CarImage> files = new ArrayList();
	  String savDir=request.getSession().getServletContext().getRealPath("/resources/upload/car");
	  
	  for(MultipartFile f : upFile) {
		  
		  //????????? ??????(rename)
		  String oriFileName = f.getOriginalFilename();
		  String ext = oriFileName.substring(oriFileName.lastIndexOf("."));
		  //rename ????????????
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
		  int rdv = (int)(Math.random()*1000);
		  String reName = sdf.format(System.currentTimeMillis())+"_"+rdv+ext;
		  //?????? ??????
		  try {
			  f.transferTo(new File(savDir+"/"+reName));
		  }catch(IllegalStateException | IOException e)
		  {
			  e.printStackTrace();
		  }
		  
		  imgOrder = imgOrder+1;
		  CarImage cImg = new CarImage();
		  cImg.setCarImageOri(oriFileName);
		  cImg.setCarImageRe(reName);
		  cImg.setCarNum(carNum);
		  logger.debug("????????? ?????????"+cImg);
		  cImg.setImageOrder(imgOrder);
		  files.add(cImg);	  
	  }

	  
	  int result = dService.updateDriver(driver);
	  int deleteImg = dService.deleteImg(carNum);
	  
	  int insertImg = dService.insertImg(files);
	  if(result>0)
	  {
		  for(String oldImg : img)
		  {
			  File file = new File(sd+"/"+oldImg);
			  if(file.exists())
			  {
				  file.delete();
				  logger.debug("?????? ??? ???????????? ??????"+driver);
				  logger.debug("?????? ??? ?????????"+files);
				  mv.addObject("driver",driver);
				  mv.addObject("files",files);
			  } 
		  }
		  mv.addObject("msg","???????????? ????????? ?????????????????????.");
		  mv.addObject("loc","/member/myDriver");
		  mv.setViewName("common/msg");
		  
	  }
	  else {
		  mv.addObject("msg","?????? ??????~!");
		  mv.addObject("loc","/member/myDriverModify");
		  mv.setViewName("common/msg");
	  }
  
	  return mv;
   }
 
	// ???????????? ??????
	@ResponseBody
	@RequestMapping("/member/changePass")
	public String changePassword(String password, HttpSession session) {
		String pw = pwEncoder.encode(password);
		Member m = (Member) session.getAttribute("logined");
		m.setMemberPw(pw);
		int result = service.updatePassword(m);
		if (result > 0) {
			return "update";
		} else {
			return "fail";
		}
	}
   
	// ????????? ??????
	@ResponseBody
	@RequestMapping("/member/changeEmail")
	public String changeEmail(String email, HttpServletRequest request, HttpSession session) throws Exception {
		Member m = (Member) session.getAttribute("logined");
		m.setEmail(email);
		StringBuffer odagada = request.getRequestURL();
		service.mailUpdate(m, odagada);
		return "sent";
	}

   //????????? ?????? ??????
   @ResponseBody
   @RequestMapping("/member/phoneUpdate")
   public String smsCheck(HttpSession session, String code, String phone) {
   	Member m = (Member)session.getAttribute("logined");  	
   	String saveCode = service.getPhoneCode(m.getMemberNum());
		logger.debug("???????????? ????????? ??????" + phone);
		if (pwEncoder.matches(code, saveCode)) {
			m.setPhone(phone);
			m.setIsPhoneAuth("Y");
			int result = service.updatePhone(m);
			if (result > 0) {
				return "ok";
			} else {
				return "no";
			}
		} else {
			return "no";
		}
	}
   
   //?????? ??????
   @ResponseBody
   @RequestMapping("/member/changeName")
   public String changeName(HttpSession session, String memberName) {
		logger.debug("???????????? ??????????" + memberName);
		Member m = (Member) session.getAttribute("logined");
		m.setMemberName(memberName);
		int result = service.updateName(m);
		if (result > 0) {
			return "ok";
		} else {
			return "fail";
		}
	}

  
   @RequestMapping("/member/naverSignup")
   public ModelAndView naverLogin(ModelAndView mav) {
	   mav.setViewName("member/naverSignup");
	   return mav;
   }
   
   @ResponseBody
   @RequestMapping("/member/checkNaver")
   public String checkNaver(HttpSession session, String id, String pw) {
	   
	   Member m=service.selectMember(id);   
	         
		if (m == null) {
			return "false";
		} else {
			session.setAttribute("logined", m);
			return "true";
		}
   }
   
   
}