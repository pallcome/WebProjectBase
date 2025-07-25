package egovframework.com.cmm.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.AbstractServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그 비즈니스 구현 클래스
 */
@Slf4j
@Service("LogService")
public class LogService extends AbstractServiceImpl {
	public void insert(Map<String, Object> paramMap) {
		sqlSession.insert("LogMapper.insert", paramMap);
	}
}
