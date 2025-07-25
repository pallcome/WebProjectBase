package egovframework.com.cmm.service.impl;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import egovframework.com.cmm.SessionVO;

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
@Repository("UserDAO")
public class UserDAO extends EgovAbstractMapper {
	/**
	 * 코드 목록 조회
	 */
	public SessionVO selectUserInfo(String ip) {
		return selectOne("UserMapper.selectUserInfo", ip);
	}
}
