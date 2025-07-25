package egovframework.app.base.car.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

/**
 * 연료 비즈니스 구현 클래스
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
@Repository("BaseCarFuelTypeDAO")
public class BaseCarFuelTypeDAO extends EgovAbstractMapper {
	public List<Map<String, Object>> selectFuelTypeGroupList() {
		return selectList("BaseCarFuelTypeMapper.selectFuelTypeGroupList");
	}
	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap) {
		return selectList("BaseCarFuelTypeMapper.selectList", paramMap);
	}
	/**
	 * 조회
	 */
	public Map<String, Object> select(Map<String, Object> paramMap) {
		return selectOne("BaseCarFuelTypeMapper.select", paramMap);
	}
	/**
	 * 추가/수정
	 */
	public void merge(Map<String, Object> paramMap) {
		update("BaseCarFuelTypeMapper.merge", paramMap);
	}
	/**
	 * 삭제
	 */
	public void delete(Map<String, Object> paramMap) {
		delete("BaseCarFuelTypeMapper.delete", paramMap);
	}
}
