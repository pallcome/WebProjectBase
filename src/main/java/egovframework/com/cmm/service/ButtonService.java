package egovframework.com.cmm.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.AbstractServiceImpl;

@Service("ButtonService")
public class ButtonService extends AbstractServiceImpl {
	/**
	 * 사용자의 메뉴별 버튼 권한 목록 조회.
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> selectAuthBtnList(Map<String, Object> paramMap) {
		return sqlSession.selectList("ButtonMapper.selectAuthBtnList", paramMap);
	}
}
