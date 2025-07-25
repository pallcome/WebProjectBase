package egovframework.com.cmm.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.AbstractServiceImpl;
import egovframework.com.cmm.service.LogService;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그 비즈니스 구현 클래스
 * @author 양민우
 * @since 2025.04.10
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.04.10  양민우     최초 생성
 *
 *  </pre>
 */
@Slf4j
@Service("LogService")
public class LogServiceImpl extends AbstractServiceImpl implements LogService {
	@Resource(name = "LogDAO")
	private LogDAO logDAO;

	@Override
	public void insert(Map<String, Object> paramMap) {
		logDAO.insert(paramMap);
	}
}
