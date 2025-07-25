package egovframework.app.car.info.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;

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
public interface CarInfoService {
	/* 제조사 콤보박스 */
	public List<Map<String, Object>> selectCarMakerList();
	/* 제조사 콤보박스 */
	public List<Map<String, Object>> selectCarN1KindList();
	/* 제조사 콤보박스 */
	public List<Map<String, Object>> selectCarN2KindList();
	/* 제조사 콤보박스 */
	public List<Map<String, Object>> selectCarFuelList();
	/* 제조사 콤보박스 */
	public List<Map<String, Object>> selectCarTransList();
	/* 자동차모델 벨리데이션 체크 */
	public String selectChkValid(Map<String, Object> paramMap);
	
	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap);
	/**
	 * 조회
	 */
	public Map<String, Object> selectInfo(Map<String, Object> paramMap);
	/**
	 * 신규/수정
	 * @throws EgovBizException 
	 */
	public void save(Map<String, Object> paramMap) throws IOException, EgovBizException;
	/**
	 * 삭제
	 */
	public void delete(Map<String, Object> paramMap);
	
	
}
