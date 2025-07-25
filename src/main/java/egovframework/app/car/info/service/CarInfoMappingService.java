package egovframework.app.car.info.service;

import java.util.List;
import java.util.Map;

/**
 * 자동차정보 맵핑 서비스
 * @author 이현우
 * @since 2025.04.29
 * @version 1.0
 * @see
 */
public interface CarInfoMappingService {
	
	/* 트리 조회 */
	public List<Map<String, Object>> selectTreeList(Map<String, Object> paramMap);
	/* 모델그룹 info 조회 */
	public Map<String, Object> selectInfo(Map<String, Object> paramMap);
	/* 목록 조회 */
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap);
	/* 신규/수정 */
	public void save(Map<String, Object> paramMap);
	/* 삭제 */
	public void delete(Map<String, Object> paramMap);
	
	
	
}
