package egovframework.app.evaluation.result.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.app.evaluation.result.service.EvaluationResultService;
import egovframework.com.cmm.AbstractServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * 시험결과 목록
 * @author 이현우
 * @since 2025.04.17
 * @version 1.0
 * @see
 */
@Slf4j
@Service("EvaluationResultService")
public class EvaluationResultServiceImpl extends AbstractServiceImpl implements EvaluationResultService {
	@Resource(name = "EvaluationResultDAO")
	private EvaluationResultDAO evaluationResultDAO;
	
	/**
	 * 시험년도 목록 조회
	 */
	public List<Map<String, Object>> selectTestYearList() {
		return evaluationResultDAO.selectTestYearList();
	}
	/**
	 * 목록 조회
	 */
	@Override
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap) {
		return evaluationResultDAO.selectList(paramMap);
	}
	/**
	 * 자동차 정보 조회
	 */
	@Override
	public Map<String, Object> selectCarInfo(Map<String, Object> paramMap) {
		return evaluationResultDAO.selectCarInfo(paramMap);
	}
	/**
	 * 종합정보 조회
	 */
	@Override
	public Map<String, Object> selectTotInfo(Map<String, Object> paramMap) {
		return evaluationResultDAO.selectTotInfo(paramMap);
	}
	/**
	 * 정보 조회(간략) 조회
	 */
	@Override
	public Map<String, Object> selectInfo(Map<String, Object> paramMap) {
		return evaluationResultDAO.selectInfo(paramMap);
	}
	/**
	 * 항목별 평가 결과 목록 조회
	 */
	@Override
	public List<Map<String, Object>> selectResultScoreList(Map<String, Object> paramMap) {
		return evaluationResultDAO.selectResultScoreList(paramMap);
	}
	/**
	 * 배점 기준 목록 조회
	 */
	@Override
	public List<Map<String, Object>> selectTestScoreList(Map<String, Object> paramMap) {
		return evaluationResultDAO.selectTestScoreList(paramMap);
	}
	/**
	 * 저장
	 */
	@Override
	public void save(Map<String, Object> paramMap) {
		// 자동차 결과여부
		evaluationResultDAO.updateCarRlsYn((Map<String, Object>) paramMap.get("carForm"));
		// 자동차시험결과
		mergeResult(paramMap, "tot"); // 종합정보
		mergeResult(paramMap, "ghg"); // 연비/온실가스
		mergeResult(paramMap, "lca"); // LCA 전생애주기
		mergeResult(paramMap, "air"); // 차내실내공기질
	}
	private void mergeResult(Map<String, Object> paramMap, String tabType) {
		Map<String, Object> form = (Map<String, Object>) paramMap.get(tabType + "Form");
		form.put("SESSION_USER_CD", paramMap.get("SESSION_USER_CD"));
		form.put("SESSION_USER_IP", paramMap.get("SESSION_USER_IP"));
		evaluationResultDAO.mergeResult(form);
		
		if(paramMap.containsKey(tabType + "Rows")) {
			Map<String, Object> rows = (Map<String, Object>) paramMap.get(tabType + "Rows");
			List<Map<String, Object>> list = (List<Map<String, Object>>) rows.get("saveRows");
			for(Map<String, Object> item : list) {
				item.put("SESSION_USER_CD", paramMap.get("SESSION_USER_CD"));
				item.put("SESSION_USER_IP", paramMap.get("SESSION_USER_IP"));
				evaluationResultDAO.mergeResult(item);
			}
		}
	}
	
	
	/**
	 * 자동차 그룹 목록 조회
	 */
	public List<Map<String, Object>> selectCarGroupList() {
		return evaluationResultDAO.selectCarGroupList();
	}
	/**
	 * 매핑차량 목록 조회
	 */
	@Override
	public List<Map<String, Object>> selectMappingList(Map<String, Object> paramMap) {
		return evaluationResultDAO.selectMappingList(paramMap);
	}
}