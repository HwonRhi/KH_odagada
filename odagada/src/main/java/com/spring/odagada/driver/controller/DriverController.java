package com.spring.odagada.driver.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.search.IntegerComparisonTerm;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import com.spring.odagada.carpool.model.service.CarpoolService;
import com.spring.odagada.common.PageFactory;
import com.spring.odagada.common.exception.BoardException;
import com.spring.odagada.driver.model.service.DriverService;
import com.spring.odagada.driver.model.vo.CarImage;
import com.spring.odagada.driver.model.vo.Driver;
import com.spring.odagada.member.model.service.MemberService;
import com.spring.odagada.member.model.vo.Member;

@SessionAttributes(value= {"logined", "driver"})
@Controller
public class DriverController {
	private Logger logger = LoggerFactory.getLogger(DriverController.class);
	
	@Autowired
	DriverService service;
	
	@Autowired
	MemberService memberService;
	
	@Autowired
	CarpoolService cService;
	

	@RequestMapping("/driver/driverEnroll")
	public ModelAndView driverEnroll(HttpSession session) {
		
		Member m = (Member) session.getAttribute("logined");
		ModelAndView mv = new ModelAndView();		
		int memberNum = m.getMemberNum();
		
		if(m!=null && m.getMemberNum()!=0)
		{
					
			Driver driver = service.selectOne(memberNum);	
			
			if(driver!=null)
			{
				mv.setViewName("/common/msg");
				mv.addObject("msg","???????????? ????????????????????????.");
				mv.addObject("loc","/");
				
				return mv;
			}
			else {
				logger.debug("????????????"+m);
				logger.debug("????????? ?????????"+m.getPhone().substring(0,3));
				int result = memberService.deleteMsg(m);
				if(m.getPhone().length()==11)
				{
					logger.debug("??????????????????"+m.getPhone().substring(3,11));
				}
				else if(m.getPhone().length()==10)
				{
					logger.debug("??????????????????"+m.getPhone().substring(3,10));
				}
				mv.addObject("driver",driver);
				mv.setViewName("driver/driverEnroll");
				
				
				logger.debug("???????????? ?????????"+driver);
				return mv;
			}			
		}
		else {
			mv.setViewName("/common/msg");
			mv.addObject("msg","???????????? ??? ??????????????????.");
			mv.addObject("loc","/member/loginForm.do");
			
			return mv;
		}
	}
	// ????????? ?????? ???????????? ??????? ????????? ???...???...???....???..
	
	@RequestMapping("/driver/driverEnrollEnd")
	public ModelAndView driverEnrollEnd(HttpServletRequest request,MultipartFile[] upFile) throws BoardException
	{
		ModelAndView mv = new ModelAndView();
		int memberNum = Integer.parseInt(request.getParameter("memberNum"));
		String memberId = request.getParameter("memberId");
		String memberName = request.getParameter("memberName");
		String birth = request.getParameter("birth");
		
		String phone1 = request.getParameter("phone1");
		String phone2 = request.getParameter("phone2");
		String phone = phone1+phone2;
		
		String licenseNum = request.getParameter("licenseNum");
		String carModel = request.getParameter("carModel");
		String carNum = request.getParameter("carNum");
		String driver_info = request.getParameter("driver_info");
		
		int imgOrder = 0;
		
		logger.info("????????? ??????"+licenseNum);
		logger.info("????????? "+carNum);
		logger.info("dirver??????"+driver_info);
		
		Map<String,Object> driver = new HashMap();
		driver.put("memberNum", memberNum);
		driver.put("licenseNum", licenseNum);
		driver.put("carModel", carModel);
		driver.put("carNum",carNum);
		driver.put("driver_info", driver_info);
		

		
		ArrayList<CarImage> files = new ArrayList();
		
		String savDir = request.getSession().getServletContext().getRealPath("/resources/upload/car");
		
		for(MultipartFile f : upFile) {
		
			//???????????? ??????(rename)
			String oriFileName = f.getOriginalFilename();
			String ext = oriFileName.substring(oriFileName.lastIndexOf("."));
			//rename ????????????
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmssSSS");
			int rdv = (int)(Math.random()*1000);
			String reName=sdf.format(System.currentTimeMillis())+"_"+rdv+ext;
			//????????? ???????????????
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
			logger.debug("????????? ?????? "+carNum);
			cImg.setCarNum(carNum);
			cImg.setImageOrder(imgOrder);
			files.add(cImg);
		}
		logger.debug("????????? ????????????"+imgOrder);
		
		if(imgOrder>0 && imgOrder<=4)
		{
			int result = service.enrollDriver(driver,files);
			Map<String,String> d = service.selectDriverOne(memberNum);
			mv.addObject("driver",d);
			mv.setViewName("redirect:/");
			return mv;
		}
		else {
			mv.addObject("msg","???????????? 4??? ????????? ???????????????.");
			mv.addObject("loc","/driver/driverEnroll");
			mv.setViewName("common/msg");
			return mv;
		}
	}

	 @RequestMapping("/driver/driverList")
	   public ModelAndView driverList(@RequestParam(value="cPage", required=false, defaultValue="0") int cPage,HttpSession session)
	   {
		   
		  Member m = (Member) session.getAttribute("logined");
		  ModelAndView mv = new ModelAndView();
		  int numPerPage = 5;   
		  int contentCount = service.selectJoinCount();	  
		      
	      List<Map<String,String>> list = service.selectDriverList(cPage,numPerPage);
	      mv.addObject("pageBar",PageFactory.getPageBar(contentCount, cPage, numPerPage,"/odagada/driver/driverList"));
	     
	      mv.addObject("list",list);
	      mv.setViewName("driver/driverJoinList");	      
		      
	      return mv;	      
	   }

	 //????????? ?????? ????????????
	 @ResponseBody
	 @RequestMapping("/driver/checkCarNum")
	 public String checkCarNum(String carNum){
		 logger.debug("carNum???????????????"+carNum);
		 Map map = new HashMap();
		 int result = service.checkCarNum(carNum);
		 String isCarNum = "";
		 if(result>0)
		 {
			 isCarNum = "N";
		 }
		 else {
			 isCarNum = "Y";
		 }
		 
		 return isCarNum;
		 
	 }
	 
	 //????????? ???????????? ????????????
	 @ResponseBody 
	 @RequestMapping("/driver/checkLicense")
	 public String checkLicense(String licenseNum,HttpServletRequest req){
		 logger.debug("???????????? ?????????"+licenseNum);
		 int result = service.checkLicense(licenseNum);
		 String isLicense = "";
		 
		 if(result>0)
		 {
			 isLicense="N";
		 }
		 else {
			 isLicense="Y";
		 }
		 logger.debug("rufrhkrkqt::::::"+isLicense);
		 return isLicense;
	 }

	 @RequestMapping("/driver/driverForm")
	 public ModelAndView driverForm(int memberNum,String carNum,HttpSession session)
	 {
		 Member m = (Member)session.getAttribute("logined");
		 System.out.println("??????????????????????????????"+memberNum+" :"+carNum);
		 ModelAndView mv = new ModelAndView();
		 Map<String,String> map = service.selectDriverOne(memberNum);
		 List<Map<String,String>> carImg = service.selectCarImg(carNum);
		 
		 String a = map.get("LICENSENUM").substring(0, 3);
		 String b = "**-******";
		 String c = map.get("LICENSENUM").substring(13);
		 
		 String licenseNum = a+b+c;
	
		carImg.get(0).put("active", "active");
		 
		 mv.addObject("driver",map);
		 mv.addObject("carImg", carImg);
		 mv.addObject("licenseNum",licenseNum);
		 mv.addObject("member",m);
		 mv.setViewName("driver/driverForm");
		 
		 logger.info("?????? ???"+map);
		 
		 return mv;
	 }
	 

	@RequestMapping("/driver/driverFormEnd")
	public String driverFormEnd(HttpServletRequest request,@RequestParam(value="driverStatus") String driverStatus,@RequestParam(value="memberNum") int memberNum)
	{
		logger.debug("???????????????");
		Driver d = service.selectOne(memberNum);
		String licenseNum = d.getLicenseNum();
		
		logger.debug("??????= "+memberNum);
		logger.debug("???????????????????????? = "+driverStatus);
		logger.debug("?????? ??????"+licenseNum);
		
		Map<String,Object> map = new HashMap<String,Object>();
			map.put("memberNum", memberNum);
			
		//???????????? ?????? ????????? ????????? ?????? ????????? ??????
			
		if(driverStatus.equals("N"))
		{
			driverStatus = "Y";
			map.put("driverStatus", driverStatus);
			int result = service.updateStatus(map);
		}
		return "redirect: driverList";
	}
	
	@RequestMapping("/driver/driverRefuse")
	public String deleteDriver(Model model,HttpSession session,HttpServletRequest request)
	{
		Member m = (Member)session.getAttribute("logined");
		
		String carMsg = request.getParameter("carMsg");
		int memberNum = Integer.parseInt(request.getParameter("memberNum"));
		
		logger.debug("?????? ??????"+carMsg);	
		
		m.setCarMsg(carMsg);
		m.setMemberNum(memberNum);
		
		int result = service.deleteDriver(memberNum);
		int refuse = memberService.updateMsg(m);
				
		return "redirect: driverList";
	}
	
	@RequestMapping("/driver/driverWithdrawal")
	public String driverWithdrawal(int memberNum)
	{
		int result = service.deleteDriver(memberNum);
		
		return "/";
	}
		
    //???????????? ????????? ????????? ?????? ????????? ??????- ??????
    @RequestMapping("/driver/driverCarpool")
    public ModelAndView selectDriverCarpool(HttpSession session, @RequestParam(value="cPage",required=false,defaultValue="0") int cPage,HttpServletRequest request) 
    {
    	int numPerPage=10;
       ModelAndView mav = new ModelAndView();
       Member m = (Member) session.getAttribute("logined");
       int memberNum = m.getMemberNum();
       //total??? ????????????
       int totalCount = service.selectDriverCarCount(memberNum);
       
	   Map<String,String> d = service.selectDriverOne(memberNum);
       
	   
       List<Map<String,String>> dcarList = service.selectDriverCarPool(memberNum,numPerPage,cPage);
       if(dcarList!=null)
		{
    	    mav.addObject("board",dcarList);
    	    mav.addObject("boardCount",dcarList.size());
			mav.addObject("pageBar" ,PageFactory.getPageBar(totalCount, cPage, numPerPage, request.getContextPath()+"/driver/driverCarpool"));
		}
       mav.addObject("driver",d);
       mav.addObject("dcarList",dcarList);
       mav.setViewName("member/driverCarpool");
        return mav;
    }
    
    //??????????????? ???????????? ????????? ??????
    @RequestMapping("/driver/selectDriverPas")
    public ModelAndView selectDriverPas(String driverNum,String carpoolNum,String sta){
       ModelAndView mav = new ModelAndView();
       Map<String,String> m = new HashMap();
       m.put("driverNum", driverNum);
       m.put("carpoolNum", carpoolNum);
       List<Map<String,String>> dList =service.selectDriverPas(m);
       logger.debug(""+dList);
       mav.addObject("dList",dList);
       if(sta.equals("Y")) {
    	   mav.setViewName("member/passengerCk");
       }
       else{
    	   //????????? ?????? ?????? ?????? ??????
    	   mav.setViewName("member/passengerFinish");   
       }
       return mav; 
    }
    //??????????????? ?????? ????????? ??????
    @RequestMapping("/driver/updatePasOk")
    public ModelAndView updatePasOk(int memberNum,String memberName,int carpoolNum,int driverNum){
    	ModelAndView mav = new ModelAndView();
    	Map<String,Integer> map = new HashMap<>();
    	map.put("memberNum", memberNum);
    	map.put("carpoolNum", carpoolNum);
    	String msg="";
    	int rs = service.updatePasOk(map);
    	if(rs>0)
    	{
    		msg=memberName+"?????? ?????????????????????.";
    	}
    	else {
    		msg="?????? ?????????????????????. ?????? ??????????????????.";
    	}
    	mav.addObject("msg",msg);
    	mav.addObject("loc","/driver/selectDriverPas?driverNum="+driverNum+"&carpoolNum="+carpoolNum+"&sta=Y");
    	mav.setViewName("common/msg");
    	return mav;
    }
    
    //??????????????? ?????? ????????? ??????
    @RequestMapping("/driver/updatePasNo")
    public ModelAndView updatePasNo(int memberNum,String memberName,int carpoolNum,int driverNum){
    	ModelAndView mav = new ModelAndView();
    	Map<String,Integer> map = new HashMap<>();
    	map.put("memberNum", memberNum);
    	map.put("carpoolNum", carpoolNum);
    	String msg="";
    	int rs = service.updatePasNo(map);
    	if(rs>0)
    	{
    		msg=memberName+"?????? ?????????????????????.";
    		String impUid = cService.getImpUid(map);
    		
    		if(impUid != null) {
    			final String APIKey = "8371442165887262";
    			final String APISecret = "3BcZ8LhZ715zulG8TuZGMBYDoUyGBWBEyNEHzVGGWMgq8Wz1rzIdaHRyBpfcmQRVEvCerjVo6bPe8ogz";
    			
    			IamportClient client = new IamportClient(APIKey, APISecret);
    			String accessToken = null;
    			try {
    				IamportResponse<AccessToken> authResponse = client.getAuth();
    				
    				accessToken = authResponse.getResponse().getToken();
    			}catch (IamportResponseException e) {
    				e.printStackTrace();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    			
    			CancelData cancelData = new CancelData(impUid, true);
    			
    			try {
    				IamportResponse<Payment> paymentResponse = client.cancelPaymentByImpUid(cancelData);
    			}catch (IamportResponseException e) {
    				e.printStackTrace();
    			}catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	else {
    		msg="?????? ?????????????????????. ?????? ??????????????????.";
    	}
    	mav.addObject("msg",msg);
    	mav.addObject("loc","/driver/selectDriverPas?driverNum="+driverNum+"&carpoolNum="+carpoolNum+"&sta=Y");
    	mav.setViewName("common/msg");
    	return mav;
    }
    //???????????? ajax
    @RequestMapping("/driver/updateDriverCredit")
    public void updateDriverCredit(HttpServletResponse rep, int carpoolNum,int passengerNum, int driverNum, int paycode,int pay) {
    	Map<String,Integer> map = new HashMap<>();
    	map.put("carpoolNum", carpoolNum);
    	map.put("passengerNum", passengerNum);
    	map.put("driverNum", driverNum);
    	map.put("paycode", paycode);
    	map.put("pay", pay);
    	logger.debug("pay: "+pay+" driverNum"+driverNum);
    	//????????? ???????????? ???????????? ????????? ????????? ??????
    	Map<String,String> rs = service.selectCreditCode(map);
		try {
	    	if(rs!=null) {
	    		//?????? ?????? passenger???????????? driver????????? ????????????
	    		int result = service.updateCredit(map);
	    		if(result>0) {
	    			rep.getWriter().print("Y");
	    		}
	    		else {
	    			rep.getWriter().print("N");
	    		}
	    	}
	    	else {
	    	//?????? ?????? N??????
				rep.getWriter().print("N");
	    	}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

}