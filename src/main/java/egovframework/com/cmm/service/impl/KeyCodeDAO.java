package egovframework.com.cmm.service.impl;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

/**
 * 키코드 비즈니스 구현 클래스
 * @author 양민우
 * @since 2025.04.16
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.04.16  양민우     최초 생성
 *
 *  </pre>
 */
@Repository("KeyCodeDAO")
public class KeyCodeDAO extends EgovAbstractMapper {
	/**
	 * 키코드 조회
	 */
	public String select() {
		return selectOne("KeyCodeMapper.select");
	}
}
