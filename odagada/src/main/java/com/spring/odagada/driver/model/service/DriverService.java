package com.spring.odagada.driver.model.service;

import java.util.List;
import java.util.Map;

import com.spring.odagada.common.exception.BoardException;
import com.spring.odagada.driver.model.vo.Driver;
import com.spring.odagada.driver.model.vo.CarImage;

public interface DriverService {

	Driver selectOne(int memberNum);
	int selectJoinCount();
	List<Map<String,String>> selectDriverList(int cPage,int numPerPage);	
	int enrollDriver(Map<String,Object> driver,List<CarImage> files) throws BoardException;
	Map<String,String> selectDriverOne(int memberNum);
	List<Map<String,String>> selectCarImg(String carNum);
	int updateStatus(Map<String,Object> map);
	//드라이버 카풀 등록 리스트
	List<Map<String,String>> selectDriverCarPool(int memberNum,int numPerPage,int cPage);
	List<Map<String,String>> selectDriverPas(Map<String,String> m);
	int deleteDriver(int memberNum);
	int updatePasOk(Map<String,Integer> map);
	int updatePasNo(Map<String,Integer> map);
	Map<String,String> selectCreditCode(Map<String,Integer> map);
	int updateCredit(Map<String,Integer> map);
	List<String> selectImgRe(String oldCarNum);
	int updateDriver(Map<String,Object> driver);
	int deleteImg(String carNum);
	int insertImg(List<CarImage> files) throws BoardException;
	int selectDriverCarCount(int memberNum);
	
	int checkCarNum(String carNum);
	int checkLicense(String licenseNum);
}
