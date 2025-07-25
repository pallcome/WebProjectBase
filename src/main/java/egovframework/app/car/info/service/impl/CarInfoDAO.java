package egovframework.app.car.info.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

/**
 * 자동차정보 비즈니스 구현 클래스
 * @author 양민우
 * @since 2025.04.02
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.04.02  양민우     최초 생성
 *
 *  </pre>
 */
@Repository("CarInfoDAO")
public class CarInfoDAO extends EgovAbstractMapper {
	/* 제조사 콤보박스 */
	public List<Map<String, Object>> selectCarMakerList() {
		return selectList("CarInfoMapper.selectCarMakerList");
	}
	/* 차종분류1 리스트 */
	public List<Map<String, Object>> selectCarN1KindList() {
		return selectList("CarInfoMapper.selectCarN1KindList");
	}
	/* 차종분류2 리스트 */
	public List<Map<String, Object>> selectCarN2KindList() {
		return selectList("CarInfoMapper.selectCarN2KindList");
	}
	/* 연료 리스트 */
	public List<Map<String, Object>> selectCarFuelList() {
		return selectList("CarInfoMapper.selectCarFuelList");
	}
	/* 변속기 리스트 */
	public List<Map<String, Object>> selectCarTransList() {
		return selectList("CarInfoMapper.selectCarTransList");
	}
	/* 벨리데이션 */
	public String selectChkValid(Map<String, Object> paramMap) {
		return selectOne("CarInfoMapper.selectChkValid", paramMap);
	}
	
	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap) {
		return selectList("CarInfoMapper.selectList", paramMap);
	}
	/**
	 * 조회
	 */
	public Map<String, Object> selectInfo(Map<String, Object> paramMap) {
		return selectOne("CarInfoMapper.selectInfo", paramMap);
	}
	/**
	 * 신규/수정
	 */
	public void merge(Map<String, Object> paramMap) {
		update("CarInfoMapper.merge", paramMap);
	}
	/**
	 * 삭제
	 */
	public void delete(Map<String, Object> paramMap) {
		delete("CarInfoMapper.delete", paramMap);
	}
	
}
