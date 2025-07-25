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
@Repository("CarInfoMappingDAO")
public class CarInfoMappingDAO extends EgovAbstractMapper {
	/*  트리 조회 */
	public List<Map<String, Object>> selectTreeList(Map<String, Object> paramMap) {
		return selectList("CarInfoMappingMapper.selectTreeList", paramMap);
	}
	/*  자동차그룹 info 조회 */
	public Map<String, Object> selectInfo(Map<String, Object> paramMap) {
		return selectOne("CarInfoMappingMapper.selectInfo", paramMap);
	}
	/* 목록 조회 */
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap) {
		return selectList("CarInfoMappingMapper.selectList", paramMap);
	}
	/* GROUP 신규/수정 */
	public void mergeGroup(Map<String, Object> paramMap) {
		update("CarInfoMappingMapper.mergeGroup", paramMap);
	}
	/* MAPPING 신규/수정 */
	public void mergeMpng(Map<String, Object> paramMap) {
		update("CarInfoMappingMapper.mergeMpng", paramMap);
	}
	/* MAPPING 제거 */
	public void deleteMpng(Map<String, Object> paramMap) {
		update("CarInfoMappingMapper.deleteMpng", paramMap);
	}
	
	/* 삭제 */
	public void delete(Map<String, Object> paramMap) {
		delete("CarInfoMappingMapper.delete", paramMap);
	}
	/* 맵핑차량 전체제거 */
	public void deleteMpngAll(Map<String, Object> paramMap) {
		delete("CarInfoMappingMapper.deleteMpngAll", paramMap);
	}
	
	
}
