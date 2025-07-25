package egovframework.com.cmm.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.AbstractServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * 코드 비즈니스 구현 클래스
 */
@Slf4j
@Service("CodeService")
public class CodeService extends AbstractServiceImpl {
	/**
	 * 코드 목록 조회
	 */
	public List<Map<String, Object>> selectList(String clsGroupCd) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("CLS_GROUP_CD", clsGroupCd);
		return selectList(paramMap);
	}
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap) {
		return sqlSession.selectList("CodeMapper.selectList", paramMap);
	}
}