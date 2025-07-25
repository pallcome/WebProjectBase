package egovframework.app.evaluation.item.service.impl;

import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

/**
 * 시험항목( 주요시험항목, 연비온실가스, LCA, 차실내공기질 )
 * @author 이현우
 * @since 2025.04.17
 * @version 1.0
 * @see
 */
@Repository("EvaluationItemDAO")
public class EvaluationItemDAO extends EgovAbstractMapper {
	/* 단위 데이터셋 조회 */
	public List<Map<String, Object>> selectUnitList() {
		return selectList("EvaluationItemMapper.selectUnitList");
	}
	/* 목록 조회 */
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap) {
		return selectList("EvaluationItemMapper.selectList", paramMap);
	}
	/* 상세조회 */
	public Map<String, Object> selectInfo(Map<String, Object> paramMap) {
		return selectOne("EvaluationItemMapper.selectInfo", paramMap);
	}
	/* 적용기간: 조회 */
	public List<Map<String, Object>> selectApplyList(Map<String, Object> paramMap) {
		return selectList("EvaluationItemMapper.selectApplyList", paramMap);
	}
	/* info: 추가/수정 */
	public void merge(Map<String, Object> paramMap) {
		update("EvaluationItemMapper.merge", paramMap);
	}
	/* info: 삭제 */
	public void delete(Map<String, Object> paramMap) {
		delete("EvaluationItemMapper.delete", paramMap);
	}
	/* 적용기간: 삭제 */
	public void deletePrd(Map<String, Object> paramMap) {
		delete("EvaluationItemMapper.deletePrd", paramMap);
	}
	/* 적용기간: 저장 */
	public void mergePrd(Map<String, Object> paramMap) {
		update("EvaluationItemMapper.mergePrd", paramMap);
	}
	/* 적용기간: 전체삭제(시험항목제거) */
	public void deletePrdAll(Map<String, Object> paramMap) {
		delete("EvaluationItemMapper.deletePrdAll", paramMap);
	}
	
	/* 팝업: 배점기준 info */
	public Map<String, Object> selectScoreInfo(Map<String, Object> paramMap) {
		return selectOne("EvaluationItemMapper.selectScoreInfo", paramMap);
	}
	/* 팝업: 배점 리스트 조회 */
	public List<Map<String, Object>> selectScoreList(Map<String, Object> paramMap) {
		return selectList("EvaluationItemMapper.selectScoreList", paramMap);
	}
	/* 팝업: 배점 추가/수정 */
	public void mergeScore(Map<String, Object> paramMap) {
		update("EvaluationItemMapper.mergeScore", paramMap);
	}
	/* 팝업: 배점 제거 */
	public void deleteScore(Map<String, Object> paramMap) {
		delete("EvaluationItemMapper.deleteScore", paramMap);
	}
	/* 적용기간: 단일여부 update */
	public void updatePrdIndiv(Map<String, Object> paramMap) {
		update("EvaluationItemMapper.updatePrdIndiv", paramMap);
	}
	/* 팝업: 배점 전부제거 */
	public void deleteScoreAll(Map<String, Object> paramMap) {
		delete("EvaluationItemMapper.deleteScoreAll", paramMap);
	}
	
}
