package egovframework.com.cmm.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

/**
 * 버튼 비즈니스 구현 클래스
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
@Repository("ButtonDAO")
public class ButtonDAO extends EgovAbstractMapper {
	/**
	 * 버튼 목록 조회
	 */
	public List<Map<String, Object>> selectAuthBtnList(Map<String, Object> paramMap) {
		return selectList("ButtonMapper.selectAuthBtnList", paramMap);
	}
}
