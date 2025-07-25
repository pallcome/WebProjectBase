package egovframework.app.evaluation.result.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

/**
 * 평가결과 등록
 * @author 이현우
 * @since 2025.04.17
 * @version 1.0
 * @see
 */
@Repository("EvaluationResultDAO")
public class EvaluationResultDAO extends EgovAbstractMapper {
	/**
	 * 시험년도 목록 조회
	 */
	public List<Map<String, Object>> selectTestYearList() {
		return selectList("EvaluationResultMapper.selectTestYearList");
	}
	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap) {
		return selectList("EvaluationResultMapper.selectList", paramMap);
	}
	/**
	 * 자동차 정보 조회
	 */
	public Map<String, Object> selectCarInfo(Map<String, Object> paramMap) {
		return selectOne("EvaluationResultMapper.selectCarInfo", paramMap);
	}
	/**
	 * 종합정보 조회
	 */
	public Map<String, Object> selectTotInfo(Map<String, Object> paramMap) {
		return selectOne("EvaluationResultMapper.selectTotInfo", paramMap);
	}
	/**
	 * 정보 조회(간략) 조회
	 */
	public Map<String, Object> selectInfo(Map<String, Object> paramMap) {
		return selectOne("EvaluationResultMapper.selectInfo", paramMap);
	}
	/**
	 * 항목별 평가 결과 목록 조회
	 */
	public List<Map<String, Object>> selectResultScoreList(Map<String, Object> paramMap) {
		return selectList("EvaluationResultMapper.selectResultScoreList", paramMap);
	}
	/**
	 * 배점 기준 목록 조회
	 */
	public List<Map<String, Object>> selectTestScoreList(Map<String, Object> paramMap) {
		return selectList("EvaluationResultMapper.selectTestScoreList", paramMap);
	}
	/**
	 * 자동차 결과여부 저장
	 */
	public void updateCarRlsYn(Map<String, Object> paramMap) {
		update("EvaluationResultMapper.updateCarRlsYn", paramMap);
	}
	/**
	 * 자동차시험결과 저장
	 */
	public void mergeResult(Map<String, Object> paramMap) {
		update("EvaluationResultMapper.mergeResult", paramMap);
	}
	
	
	
	/**
	 * 자동차 그룹 목록 조회
	 */
	public List<Map<String, Object>> selectCarGroupList() {
		return selectList("EvaluationResultMapper.selectCarGroupList");
	}
	/**
	 * 매핑차량 목록 조회
	 */
	public List<Map<String, Object>> selectMappingList(Map<String, Object> paramMap) {
		return selectList("EvaluationResultMapper.selectMappingList", paramMap);
	}
}
