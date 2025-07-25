package egovframework.app.base.car.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

/**
 * 차종 비즈니스 구현 클래스
 * @author 양민우
 * @since 2025.04.17
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.04.17  양민우     최초 생성
 *
 *  </pre>
 */
@Repository("BaseCarCarTypeDAO")
public class BaseCarCarTypeDAO extends EgovAbstractMapper {
	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap) {
		return selectList("BaseCarCarTypeMapper.selectList", paramMap);
	}
	/**
	 * 조회
	 */
	public Map<String, Object> select(Map<String, Object> paramMap) {
		return selectOne("BaseCarCarTypeMapper.select", paramMap);
	}
	/**
	 * 추가/수정
	 */
	public void merge(Map<String, Object> paramMap) {
		update("BaseCarCarTypeMapper.merge", paramMap);
	}
	/**
	 * 삭제
	 */
	public void delete(Map<String, Object> paramMap) {
		delete("BaseCarCarTypeMapper.delete", paramMap);
	}
}
