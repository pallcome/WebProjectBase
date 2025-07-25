package egovframework.app.evaluation.item.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.app.evaluation.item.service.EvaluationItemService;
import egovframework.com.cmm.AbstractServiceImpl;
import egovframework.com.cmm.service.EgovFileMngService;
import lombok.extern.slf4j.Slf4j;

/**
 * 시험항목( 주요시험항목, 연비온실가스, LCA, 차실내공기질 )
 * @author 이현우
 * @since 2025.04.17
 * @version 1.0
 * @see
 */
@Slf4j
@Service("EvaluationItemService")
public class EvaluationItemServiceImpl extends AbstractServiceImpl implements EvaluationItemService {
	
	@Resource(name = "EvaluationItemDAO")
	private EvaluationItemDAO evaluationItemDAO;
	
	@Resource(name = "EgovFileMngService")
	private EgovFileMngService egovFileMngService;
	
	/* 단위 데이터셋 조회 */
	@Override
	public List<Map<String, Object>> selectUnitList() {
		return evaluationItemDAO.selectUnitList();
	}
	/* 목록 조회 */
	@Override
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap) {
		return evaluationItemDAO.selectList(paramMap);
	}
	/* 상세조회 */
	@Override
	public Map<String, Object> selectInfo(Map<String, Object> paramMap) {
		return evaluationItemDAO.selectInfo(paramMap);
	}
	/* 적용기간 리스트 조회*/
	@Override
	public List<Map<String, Object>> selectApplyList(Map<String, Object> paramMap) {
		return evaluationItemDAO.selectApplyList(paramMap);
	}
	/* 저장 */
	@Override	
	@SuppressWarnings("unchecked")
	public void save(Map<String, Object> paramMap) throws IOException {
		
		// 공통 세션 값 세팅 함수
	    BiConsumer<Map<String, Object>, Map<String, Object>> setSessionInfo = (target, source) -> {
	        target.put("SESSION_USER_CD", source.get("SESSION_USER_CD"));
	        target.put("SESSION_USER_IP", source.get("SESSION_USER_IP"));
	        target.put("UP_TEST_ARTCL_CD", source.get("UP_TEST_ARTCL_CD"));
	    };

	    // 1. formData 처리
	    Optional.ofNullable((Map<String, Object>) paramMap.get("formData")).ifPresent(formMap -> {
	    	try {
	    		setSessionInfo.accept(formMap, paramMap);
				egovFileMngService.saveFile(formMap);
				evaluationItemDAO.merge(formMap);
			} catch (IOException e) {
				e.printStackTrace();
			}
	    });

	    // 2. 적용기간 삭제 처리
	    List<Map<String, Object>> deleteRows = (List<Map<String, Object>>) paramMap.get("deleteRows");
	    if (deleteRows != null) {
	        deleteRows.forEach(deleteMap -> {
	            setSessionInfo.accept(deleteMap, paramMap);
	            evaluationItemDAO.deleteScoreAll(deleteMap);
	            evaluationItemDAO.deletePrd(deleteMap);
	        });
	    }

	    // 3. 저장 처리
	    List<Map<String, Object>> saveRows = (List<Map<String, Object>>) paramMap.get("saveRows");
	    Map<String, Object> formMap = (Map<String, Object>) paramMap.get("formData");
	    if (saveRows != null) {
	        saveRows.forEach(mergeMap -> {
	            setSessionInfo.accept(mergeMap, paramMap);
	            mergeMap.put("TEST_ARTCL_CD", formMap.get("TEST_ARTCL_CD"));

	            evaluationItemDAO.mergePrd(mergeMap);
	            
	            //직접 입력시 하위데이터 제거
	            if("Y".equals(mergeMap.get("DRCT_INPT_YN"))) {
	            	evaluationItemDAO.deleteScoreAll(mergeMap);
	            } else {
	            	// 자식 항목 처리
	            	Optional.ofNullable((Map<String, Object>) mergeMap.get("CHILD")).ifPresent(childMap -> {
	            		setSessionInfo.accept(childMap, paramMap);
	            		childMap.put("TEST_ARTCL_CD", mergeMap.get("TEST_ARTCL_CD"));
	            		childMap.put("TEST_ARTCL_PRD_CD", mergeMap.get("TEST_ARTCL_PRD_CD"));
	            		
	            		saveScore(childMap);
	            	});
	            }
	        });
	    }
	}
	
	/* 삭제 */
	@Override
	public void delete(Map<String, Object> paramMap) throws IOException {
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> deleteRows = (List<Map<String, Object>>) paramMap.get("list");
	    if (deleteRows != null) {
	        deleteRows.forEach(deleteMap -> {
	        	/* 하위 데이터 제거 */
	        	for(Map<String, Object> selectMap : evaluationItemDAO.selectApplyList(deleteMap)) {
	        		//배점항목 데이터 제거
	        		evaluationItemDAO.deleteScoreAll(selectMap);
	        	}
	        	//적용기간 데이터 제거
	            evaluationItemDAO.deletePrdAll(deleteMap);
	            //시험항목 제거
	            evaluationItemDAO.delete(deleteMap);
	        });
	    }
	}
	
	/* 팝업 : 배점기준 info */
	@Override
	public Map<String, Object> selectScoreInfo(Map<String, Object> paramMap) {
		return evaluationItemDAO.selectScoreInfo(paramMap);
	}
	/* 팝업 : 배점기준 리스트 조회 */
	@Override
	public List<Map<String, Object>> selectScoreList(Map<String, Object> paramMap) {
		return evaluationItemDAO.selectScoreList(paramMap);
	}
	/* 팝업 : 배점기준 삭제/수정/저장 */
	@SuppressWarnings("unchecked")
	public void saveScore(Map<String, Object> paramMap) {
		
		// 1. 삭제
	    Optional.ofNullable((List<Map<String, Object>>) paramMap.get("deleteRows")).ifPresent(deleteRows ->
	        deleteRows.forEach(evaluationItemDAO::deleteScore)
	    );

	    // 2. 저장(Merge)
	    List<Map<String, Object>> saveRows = (List<Map<String, Object>>) paramMap.get("saveRows");
	    if (saveRows != null) {
	        saveRows.forEach(mergeMap -> {
	            mergeMap.put("TEST_ARTCL_PRD_CD", paramMap.get("TEST_ARTCL_PRD_CD"));
	            mergeMap.put("INDIV_YN", paramMap.get("INDIV_YN"));

	            if ("Y".equals(mergeMap.get("INDIV_YN"))) {
	                mergeMap.put("MIN_SCR", mergeMap.get("MIN_MAX_SCR"));
	                mergeMap.put("MAX_SCR", mergeMap.get("MIN_MAX_SCR"));
	            }

	            evaluationItemDAO.mergeScore(mergeMap);
	        });

	        // 3. 단일 여부 저장
	        if (!saveRows.isEmpty()) {
	            evaluationItemDAO.updatePrdIndiv(paramMap);
	        }
	    }
	    
	}
	
}