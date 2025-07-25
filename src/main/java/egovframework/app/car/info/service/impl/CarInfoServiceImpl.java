package egovframework.app.car.info.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.springframework.stereotype.Service;

import egovframework.app.car.info.service.CarInfoService;
import egovframework.com.cmm.AbstractServiceImpl;
import egovframework.com.cmm.service.EgovFileMngService;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@Service("CarInfoService")
public class CarInfoServiceImpl extends AbstractServiceImpl implements CarInfoService {
	
	@Resource(name = "CarInfoDAO")
	private CarInfoDAO carInfoDAO;
	
	@Resource(name = "EgovFileMngService")
	private EgovFileMngService egovFileMngService;
	
	/* 제조사 콤보박스 */
	@Override
	public List<Map<String, Object>> selectCarMakerList() {
		return carInfoDAO.selectCarMakerList();
	}
	/* 차종분류1 리스트 */
	@Override
	public List<Map<String, Object>> selectCarN1KindList() {
		return carInfoDAO.selectCarN1KindList();
	}
	/* 차종분류2 리스트 */
	@Override
	public List<Map<String, Object>> selectCarN2KindList() {
		return carInfoDAO.selectCarN2KindList();
	}
	/* 연료 리스트 */
	@Override
	public List<Map<String, Object>> selectCarFuelList() {
		return carInfoDAO.selectCarFuelList();
	}
	/* 변속기 리스트 */
	@Override
	public List<Map<String, Object>> selectCarTransList() {
		return carInfoDAO.selectCarTransList();
	}
	/* 벨리데이션 */
	@Override
	public String selectChkValid(Map<String, Object> paramMap) {
		return carInfoDAO.selectChkValid(paramMap);
	}
	
	/**
	 * 목록 조회
	 */
	@Override
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap) {
		return carInfoDAO.selectList(paramMap);
	}
	/**
	 * 조회
	 */
	@Override
	public Map<String, Object> selectInfo(Map<String, Object> paramMap) {
		return carInfoDAO.selectInfo(paramMap);
	}
	/**
	 * 신규/수정
	 * @throws IOException 
	 * @throws EgovBizException 
	 */
	@Override
	public void save(Map<String, Object> paramMap) throws IOException, EgovBizException {
		// 동일한 모델 벨리데이션 체크(제조사,시험년도,모델명)
		if("Y".equals(carInfoDAO.selectChkValid(paramMap))) {
			throw new EgovBizException("동일한 모델이 존재합니다.");
		}
		
		egovFileMngService.saveFile(paramMap);
		
		carInfoDAO.merge(paramMap);
	}
	
	/**
	 * 삭제
	 */
	@Override
	public void delete(Map<String, Object> paramMap) {
		for(Map<String, Object> item : (List<Map<String, Object>>)paramMap.get("list")) {
			carInfoDAO.delete(item);
		}
	}
	
	
}