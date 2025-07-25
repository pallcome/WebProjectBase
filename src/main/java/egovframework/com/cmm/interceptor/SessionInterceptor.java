package egovframework.com.cmm.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import egovframework.com.cmm.SessionVO;
import egovframework.com.cmm.advice.CommonAdvice;
import egovframework.com.cmm.service.UserService;
import egovframework.com.cmm.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 세션 인터셉터
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
 *  2025.04.10  양민우          최초 생성
 *  </pre>
 */

@Slf4j
@Component("SessionInterceptor")
public class SessionInterceptor implements HandlerInterceptor {
	@Resource(name = "UserService")
	private UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		SessionVO session = HttpUtil.getSessionVO();
		if(session == null) {
			SessionVO sessionVO = userService.selectUserInfo();
			HttpUtil.getCurrentRequest().getSession().setAttribute(CommonAdvice.SESSION_VO_KEY, sessionVO); // 세션에 저장
		}
		
		return true;
	}

}