package com.spring.odagada.carpool.model.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.odagada.carpool.model.vo.Carpool;
import com.spring.odagada.carpool.model.vo.CarOption;


@Repository
public class CarpoolDaoImpl implements CarpoolDao {
	@Autowired
	SqlSessionTemplate session;
	
	@Override
	public List<Map<String,String>> selectCarpoolList(Map<String, String> map,int cPage,int numPerPage) {
		RowBounds rb = new RowBounds(cPage,numPerPage);
		return session.selectList("carpool.selectCarpoolList", map,rb);
	}

	@Override
	public int insertCarpool(Carpool carpool) {
		return session.insert("carpool.insertCarpool", carpool);
	}

	@Override
	public int insertOption(CarOption option) {
		return session.insert("carpool.insertOption", option);
	}

	@Override
	public List<Map<String,String>> selectCarOptionList(Map<String,String> map,int cPage,int numPerPage) {
		RowBounds rb = new RowBounds(cPage,numPerPage);
		return session.selectList("carpool.selectCarOptionList",map,rb);
	}

	@Override
	public List<Map<String, String>> selectCarOneList(int carpoolNum) {
		return session.selectList("carpool.selectCarOneList",carpoolNum);
	}

	@Override
	public List<Map<String, String>> selectPasList(int carpoolNum) {
		return session.selectList("carpool.selectPasList",carpoolNum);
	}

	@Override
	public List<Map<String, String>> selectImageList(int mem) {
		return session.selectList("carpool.selectImageList",mem);
	}

	@Override
	public List<Map<String, String>> selectReList(int dNum) {
		return session.selectList("carpool.selectReList",dNum);
	}

	@Override
	public int insertPassenger(Map<String, Object> pass) {
		return session.insert("carpool.insertPassenger", pass);
	}

	public List<Map<String, String>> selectCarpoolList(int memberNum, int cPage, int numPerPage) {
		RowBounds rb = new RowBounds((cPage-1)*numPerPage, numPerPage);
		return session.selectList("carpool.selectMemberCarpoolList", memberNum, rb);
	}

	@Override
	public int selectCarpoolCount(int memberNum) {
		return session.selectOne("carpool.selectCarpoolCount", memberNum);
  }

	@Override
	public List<Map<String, String>> selectPopList() {
		return session.selectList("carpool.selectPopList");
	}	
	
	@Override
	public void setCarpoolStatus(String nowDate) {
		session.update("carpool.setCarpoolStatus", nowDate);
	}

	@Override
	public String getImpUid(Map<String, Integer> map) {
		return session.selectOne("carpool.selectImpUid", map);
	}

	@Override
	public int updateCPayStatus(Map<String, Integer> map) {
		return session.update("carpool.updateCPayStatus", map);
	}	
}
