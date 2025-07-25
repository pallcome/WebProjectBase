package egovframework.app.base.car.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 제조사 비즈니스 구현 클래스
 * @author 양민우
 * @since 2025.04.14
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.04.14  양민우     최초 생성
 *
 *  </pre>
 */
public interface BaseCarManufacturerService {
	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap);
	/**
	 * 조회
	 */
	public Map<String, Object> select(Map<String, Object> paramMap);
	/**
	 * 저장
	 */
	public void save(Map<String, Object> paramMap) throws IOException;
	/**
	 * 삭제
	 * @throws IOException 
	 */
	public void delete(Map<String, Object> paramMap) throws IOException;
}
