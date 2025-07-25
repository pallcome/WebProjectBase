package egovframework.com.cmm.service.impl;

import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

/**
 * 로그 비즈니스 구현 클래스
 * @author 양민우
 * @since 2025.04.10
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.04.10  양민우     최초 생성
 *
 *  </pre>
 */
@Repository("LogDAO")
public class LogDAO extends EgovAbstractMapper {
	public void insert(Map<String, Object> paramMap) {
		insert("LogMapper.insert", paramMap);
	}
}
