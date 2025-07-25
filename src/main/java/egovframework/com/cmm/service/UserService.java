package egovframework.com.cmm.service;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.AbstractServiceImpl;
import egovframework.com.cmm.SessionVO;
import egovframework.com.cmm.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 세션 비즈니스 구현 클래스
 */
@Slf4j
@Service("UserService")
public class UserService extends AbstractServiceImpl {
	/**
	 * 코드 목록 조회
	 */
	public SessionVO selectUserInfo() {
		return sqlSession.selectOne("UserMapper.selectUserInfo", HttpUtil.getClientIp());
	}
}