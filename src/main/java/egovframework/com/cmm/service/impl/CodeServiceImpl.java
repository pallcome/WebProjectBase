package egovframework.com.cmm.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.AbstractServiceImpl;
import egovframework.com.cmm.service.CodeService;
import lombok.extern.slf4j.Slf4j;

/**
 * 코드 비즈니스 구현 클래스
 * @author 양민우
 * @since 2025.04.14
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.04.14  양민우     최초 생성
 *
 *  </pre>
 */
@Slf4j
@Service("CodeService")
public class CodeServiceImpl extends AbstractServiceImpl implements CodeService {
	@Resource(name = "CodeDAO")
	private CodeDAO codeDAO;
	/**
	 * 코드 목록 조회
	 */
	@Override
	public List<Map<String, Object>> selectList(String clsGroupCd) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("CLS_GROUP_CD", clsGroupCd);
		return selectList(paramMap);
	}
	@Override
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap) {
		return codeDAO.selectList(paramMap);
	}
}