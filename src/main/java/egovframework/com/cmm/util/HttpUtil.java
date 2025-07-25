package egovframework.com.cmm.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import egovframework.com.cmm.SessionVO;
import egovframework.com.cmm.advice.CommonAdvice;

public class HttpUtil {
	// IP 조회
	public static String getClientIp() {
		HttpServletRequest request = getCurrentRequest();
		String[] headerNames = {
			"X-Forwarded-For",
			"Proxy-Client-IP",
			"WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR",
			"HTTP_X_FORWARDED",
			"HTTP_X_CLUSTER_CLIENT_IP",
			"HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR",
			"HTTP_FORWARDED",
			"HTTP_VIA",
			"REMOTE_ADDR"
		};

		for (String header : headerNames) {
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				// X-Forwarded-For는 "client, proxy1, proxy2" 이런 형태일 수 있음 → 맨 앞이 실제 IP
				return ip.split(",")[0].trim();
			}
		}

		return request.getRemoteAddr();
	}
	// 현재 Request 객체를 구한다.
	public static HttpServletRequest getCurrentRequest() {
		ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		return sra.getRequest();
	}
	// ContextPath 가져오기
	public static String getContextPath() {
		HttpServletRequest req = getCurrentRequest();
		return req.getContextPath();
	}
	// ContextPath 가져오기
	public static String getCurrentURI() {
		HttpServletRequest req = getCurrentRequest();
		return req.getRequestURI().replace(req.getContextPath(), "");
	}
	// SESSION VO 가져오기
	public static SessionVO getSessionVO() {
		HttpServletRequest req = getCurrentRequest();
		HttpSession session = req.getSession(); // 세션 가져오기 (없으면 생성됨)
		return (SessionVO) session.getAttribute(CommonAdvice.SESSION_VO_KEY);
	}
}
