package egovframework.app.base.car.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.app.base.car.service.BaseCarFuelTypeService;
import egovframework.com.cmm.AbstractServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * 연료 비즈니스 구현 클래스
 * @author 양민우
 * @since 2025.04.17
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.04.17  양민우     최초 생성
 *
 *  </pre>
 */
@Slf4j
@Service("BaseCarFuelTypeService")
public class BaseCarFuelTypeServiceImpl extends AbstractServiceImpl implements BaseCarFuelTypeService {
	@Resource(name = "BaseCarFuelTypeDAO")
	private BaseCarFuelTypeDAO baseCarFuelTypeDAO;
	/**
	 * 연료 분류 목록 조회
	 */
	@Override
	public List<Map<String, Object>> selectFuelTypeGroupList() {
		return baseCarFuelTypeDAO.selectFuelTypeGroupList();
	}
	/**
	 * 목록 조회
	 */
	@Override
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap) {
		return baseCarFuelTypeDAO.selectList(paramMap);
	}
	/**
	 * 조회
	 */
	@Override
	public Map<String, Object> select(Map<String, Object> paramMap) {
		return baseCarFuelTypeDAO.select(paramMap);
	}
	/**
	 * 저장
	 */
	@Override
	public void save(Map<String, Object> paramMap) {
		baseCarFuelTypeDAO.merge(paramMap);
	}
	/**
	 * 삭제
	 * @throws IOException 
	 */
	@Override
	public void delete(Map<String, Object> paramMap) {
		for(Map<String, Object> item : (List<Map<String, Object>>)paramMap.get("list")) {
			baseCarFuelTypeDAO.delete(item);
		}
	}
}