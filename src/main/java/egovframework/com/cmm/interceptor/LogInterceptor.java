package egovframework.com.cmm.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import egovframework.com.cmm.SessionVO;
import egovframework.com.cmm.service.LogService;
import egovframework.com.cmm.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 로그 인터셉터
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
@Component("LogInterceptor")
public class LogInterceptor implements HandlerInterceptor {
	@Value("${spring.profiles.active:local}")
	private String profile;
	@Resource(name = "LogService")
	private LogService logService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if("dev,prod".indexOf(profile) == -1) return true;
		
		Map<String, Object> paramMap = new HashMap<>();
		String uri = HttpUtil.getCurrentURI();
		SessionVO sessionVO = HttpUtil.getSessionVO();
		
		
		
		paramMap.put("SESSION_USER_CD", sessionVO.getUserCd());
		paramMap.put("SESSION_USER_IP", HttpUtil.getClientIp());
		paramMap.put("URI", uri);
		
		logService.insert(paramMap);
		
		return true;
	}

}