package egovframework.com.cmm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.ui.Model;

import egovframework.com.cmm.interceptor.CustomAuthenticInterceptor;
import egovframework.com.cmm.service.ButtonService;
import egovframework.com.cmm.service.CodeService;
import egovframework.com.cmm.util.HttpUtil;
import egovframework.let.utl.fcc.service.EgovStringUtil;

public abstract class AbstractController {
	
	@Resource(name = "CodeService")
	protected CodeService codeService;
	@Resource(name="ButtonService")
	protected ButtonService buttonService;
	@Resource(name="CustomAuthenticInterceptor")
	protected CustomAuthenticInterceptor customAuthenticInterceptor;
	
	/**
	 * 화면 경로
	 */
	public String viewResolver(Object controller, Model model) {
		String className = controller.getClass().getCanonicalName(); // egovframework.app.car.info.web.CarInfoModelListController
		String menuPage = null;
		
		if (className.endsWith("PController")) { // 팝업에서 화면을 보여주기 위한 용도
			Map<String, Object> menu = customAuthenticInterceptor.getMenu(HttpUtil.getCurrentURI());
			menuPage = EgovStringUtil.nullConvert(menu.get("PAGE_URL_ADDR"));
			model.addAttribute("layoutName", "layout/popupLayout");
			
		}else { // 일반 Controller
			// Controller
			className = className.substring(className.indexOf("app."), className.length() - "Controller".length()); // app.car.info.web.CarInfoModelList
			className = className.replaceAll("\\.web\\.", ".");  // app.car.info.CarInfoModelList
			menuPage = className.replaceAll("\\.", "/"); // app/car/info/CarInfoModelList
			model.addAttribute("layoutName", "layout/baseLayout");
		}
		return menuPage;
	}
	
	/**
	 * DataTable 형식 데이터 만들기
	 */
	public Map<String, Object> makeDataTable(List<Map<String, Object>> list) {
		Map<String, Object> result = new HashMap<>();
		result.put("data", list);
		result.put("recordsTotal", list.size()); // 전체 데이터 개수
		result.put("recordsFiltered", list.size()); // 검색 조건이 적용된 후의 개수
		
		return result;
	}
	
}
