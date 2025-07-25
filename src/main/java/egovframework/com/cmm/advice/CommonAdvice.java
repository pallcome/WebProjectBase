package egovframework.com.cmm.advice;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import egovframework.com.cmm.SessionVO;
import egovframework.com.cmm.service.MenuService;
import egovframework.com.cmm.util.HttpUtil;

/**
 * 공통 어드바이스 클래스
 * @author 양민우
 * @since 2025.04.02
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *  2025.04.02  양민우     최초 생성
 *
 * </pre>
 */
@ControllerAdvice
public class CommonAdvice {
	public static final String SESSION_VO_KEY = "SESSION_USER_IP";
	private List<Map<String, Object>> menuList = null;
	
	@Resource(name = "MenuService")
	private MenuService menuService;
	
	@ModelAttribute("cmmMenuList")
	public List<Map<String, Object>> cmmMenuList() {
		if (menuList == null) {
			refresh();
		}
		return Collections.unmodifiableList(menuList);
	}
	
	@ModelAttribute("cmmVO")
	public Map<String, Object> cmmVO() {
		Map<String, Object> result = new HashMap<>();
		
		SessionVO session = HttpUtil.getSessionVO();
		result.put("SESSION_USER_CD", session.getUserCd());
		result.put("SESSION_USER_IP", HttpUtil.getClientIp());
		result.put("CONTEXT_PATH", HttpUtil.getContextPath());
		result.put("CURRENT_URI", HttpUtil.getCurrentURI());
		
		return result;
	}
	
	public void refresh() {
		menuList = menuService.selectMenuTree();
	}
}
