package com.spring.odagada.board.model.service;

import java.util.List;
import java.util.Map;

public interface BoardService {
	
	int selectBoardCount();
	List<Map<String,String>> selectBoardList(int cPage,int numPerPage);
	Map<String,String> selectBoard(int boardNo);
	int updateBoardCount(int boardNo);

}
