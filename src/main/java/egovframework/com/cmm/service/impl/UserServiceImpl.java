package egovframework.com.cmm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.AbstractServiceImpl;
import egovframework.com.cmm.SessionVO;
import egovframework.com.cmm.service.UserService;
import egovframework.com.cmm.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 세션 비즈니스 구현 클래스
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
@Slf4j
@Service("UserService")
public class UserServiceImpl extends AbstractServiceImpl implements UserService {
	@Resource(name = "UserDAO")
	private UserDAO userDAO;
	/**
	 * 코드 목록 조회
	 */
	@Override
	public SessionVO selectUserInfo() {
		return userDAO.selectUserInfo(HttpUtil.getClientIp());
	}
}