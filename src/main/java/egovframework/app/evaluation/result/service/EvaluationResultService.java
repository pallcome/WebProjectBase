package egovframework.app.evaluation.result.service;

import java.util.List;
import java.util.Map;

/**
 * 시험결과 목록
 * @author 이현우
 * @since 2025.04.17
 * @version 1.0
 * @see
 */
public interface EvaluationResultService {
	/**
	 * 시험년도 목록 조회
	 */
	public List<Map<String, Object>> selectTestYearList();
	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap);
	
	/**
	 * 자동차 정보 조회
	 */
	public Map<String, Object> selectCarInfo(Map<String, Object> paramMap);
	/**
	 * 종합정보 조회
	 */
	public Map<String, Object> selectTotInfo(Map<String, Object> paramMap);
	/**
	 * 정보 조회(간략) 조회
	 */
	public Map<String, Object> selectInfo(Map<String, Object> paramMap);
	/**
	 * 항목별 평가 결과 목록 조회
	 */
	public List<Map<String, Object>> selectResultScoreList(Map<String, Object> paramMap);
	/**
	 * 배점 기준 목록 조회
	 */
	public List<Map<String, Object>> selectTestScoreList(Map<String, Object> paramMap);
	/**
	 * 저장
	 */
	public void save(Map<String, Object> paramMap);
	
	
	
	/**
	 * 자동차 그룹 목록 조회
	 */
	public List<Map<String, Object>> selectCarGroupList();
	/**
	 * 매핑차량 목록 조회
	 */
	public List<Map<String, Object>> selectMappingList(Map<String, Object> paramMap);
}
