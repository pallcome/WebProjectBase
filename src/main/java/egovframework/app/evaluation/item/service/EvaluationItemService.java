package egovframework.app.evaluation.item.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 시험항목( 주요시험항목, 연비온실가스, LCA, 차실내공기질 )
 * @author 이현우
 * @since 2025.04.17
 * @version 1.0
 * @see
 */
public interface EvaluationItemService {
	/**
	 * 단위 데이터셋 조회
	 */
	public List<Map<String, Object>> selectUnitList();
	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap);
	/**
	 * 상세 조회
	 */
	public Map<String, Object> selectInfo(Map<String, Object> paramMap);
	/**
	 * 적용기간리스트 조회
	 */
	public List<Map<String, Object>> selectApplyList(Map<String, Object> paramMap);
	/**
	 * 저장
	 */
	public void save(Map<String, Object> paramMap) throws IOException;
	/**
	 * 삭제 
	 */
	public void delete(Map<String, Object> paramMap) throws IOException;
	
	/* 팝업: 상세 조회 (단일여부) */
	public Map<String, Object> selectScoreInfo(Map<String, Object> paramMap);
	/* 팝업: 배점기준 리스트 조회 */
	public List<Map<String, Object>> selectScoreList(Map<String, Object> paramMap);
	
}
