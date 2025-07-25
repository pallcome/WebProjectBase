package egovframework.app.car.info.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.springframework.stereotype.Service;

import egovframework.app.car.info.service.CarInfoMappingService;
import egovframework.com.cmm.AbstractServiceImpl;
import egovframework.com.cmm.service.EgovFileMngService;
import lombok.extern.slf4j.Slf4j;

/**
 * 자동차정보 비즈니스 구현 클래스
 * @author 이현우
 * @since 2025.04.29
 * @version 1.0
 * @see
 */
@Slf4j
@Service("CarInfoMappingService")
public class CarInfoMappingServiceImpl extends AbstractServiceImpl implements CarInfoMappingService {
	
	@Resource(name = "CarInfoMappingDAO")
	private CarInfoMappingDAO carInfoMappingDAO;
	
	@Resource(name = "EgovFileMngService")
	private EgovFileMngService egovFileMngService;
	
	@Override
	public List<Map<String, Object>> selectTreeList(Map<String, Object> paramMap) {
		return carInfoMappingDAO.selectTreeList(paramMap);
	}

	@Override
	public Map<String, Object> selectInfo(Map<String, Object> paramMap) {
		return carInfoMappingDAO.selectInfo(paramMap);
	}
	
	@Override
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap) {
		return carInfoMappingDAO.selectList(paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void save(Map<String, Object> paramMap) {
		
		// 공통 세션 값 세팅 함수
	    BiConsumer<Map<String, Object>, Map<String, Object>> setSessionInfo = (target, source) -> {
	        target.put("SESSION_USER_CD", source.get("SESSION_USER_CD"));
	        target.put("SESSION_USER_IP", source.get("SESSION_USER_IP"));
	    };

	    // 1. formData 처리
	    Optional.ofNullable((Map<String, Object>) paramMap.get("formData")).ifPresent(formMap -> {
	    		setSessionInfo.accept(formMap, paramMap);
	    		carInfoMappingDAO.mergeGroup(formMap);
	    });

	    Map<String, Object> formMap = (Map<String, Object>) paramMap.get("formData");
	    // 2. 맵핑차량 제거
	    List<Map<String, Object>> deleteRows = (List<Map<String, Object>>) paramMap.get("deleteRows");
	    if (deleteRows != null) {
	        deleteRows.forEach(deleteMap -> {
	            setSessionInfo.accept(deleteMap, paramMap);
	            deleteMap.put("CAR_MDL_GROUP_CD", formMap.get("CAR_MDL_GROUP_CD")); 
	            carInfoMappingDAO.deleteMpng(deleteMap);
	        });
	    }

	    // 3. 맵핑차량 저장 처리
	    List<Map<String, Object>> saveRows = (List<Map<String, Object>>) paramMap.get("saveRows");
	    if (saveRows != null) {
	        saveRows.forEach(mergeMap -> {
	            setSessionInfo.accept(mergeMap, paramMap);
	            mergeMap.put("CAR_MDL_GROUP_CD", formMap.get("CAR_MDL_GROUP_CD"));
	            carInfoMappingDAO.mergeMpng(mergeMap);
	        });
	    }
	}
	
	@Override
	public void delete(Map<String, Object> paramMap) {
		
		// 1. 맵핑차량 전체 제거
		carInfoMappingDAO.deleteMpngAll(paramMap);
		// 2. 그룹 제거
		carInfoMappingDAO.delete(paramMap);
	}
	
	
}