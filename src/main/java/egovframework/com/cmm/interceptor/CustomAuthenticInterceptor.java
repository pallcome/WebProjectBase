package egovframework.com.cmm.interceptor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import egovframework.com.cmm.service.MenuService;
import egovframework.com.cmm.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 인증여부 체크 인터셉터
 * @author 공통서비스 개발팀 서준식
 * @since 2011.07.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2011.07.01  서준식          최초 생성
 *  2011.09.07  서준식          인증이 필요없는 URL을 패스하는 로직 추가
 *  2025.04.02  양민우          ip인증방식 적용
 *  </pre>
 */

@Slf4j
@Component("CustomAuthenticInterceptor")
public class CustomAuthenticInterceptor implements HandlerInterceptor {
	private List<Map<String, Object>> menuRuleList = null;
	
	@Resource(name = "MenuService")
	private MenuService menuService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		if(menuRuleList == null) {
			refresh();
		}
		
		if (!isAllowed(request.getRequestURI())) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "접근이 허용되지 않은 IP입니다");
			return false;
		}
		
		return true;
	}
	
	// 요청한 사용자의 IP가 허용된 IP 리스트에 포함되는지 판단
	private boolean isAllowed(String uri) {
		uri = uri.substring(HttpUtil.getContextPath().length()); // contextPath 제거
		final String normalizedUri = "/".equals(uri) ? "/main" : uri; // '/'는 main페이지로 이동
		
		if(menuRuleList == null) {
			return false;
		}
		
		boolean matched = menuRuleList.stream()
				.map(rule -> rule.get("MENU_URL_ADDR"))
				.filter(value -> value != null)
				.map(Object::toString)
				.anyMatch(url -> normalizedUri.startsWith(url));
		
		return matched;
	}
	public void refresh() {
		menuRuleList = menuService.selectMenuList();
	}
	public Map<String, Object> getMenu(String url) {
		return menuRuleList.stream()
				.filter(map -> url.equals(map.get("MENU_URL_ADDR")))
				.findFirst()
				.orElse(null);
	}
}