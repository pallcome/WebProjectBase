package egovframework.com.cmm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.AbstractServiceImpl;
import egovframework.com.cmm.service.MenuService;
import egovframework.com.cmm.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 메뉴 비즈니스 구현 클래스
 * @author 양민우
 * @since 2025.04.02
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2025.04.02  양민우     최초 생성
 *
 *  </pre>
 */
@Slf4j
@Service("MenuService")
public class MenuServiceImpl extends AbstractServiceImpl implements MenuService {
	@Resource(name = "MenuDAO")
	private MenuDAO menuDAO;
	/**
	 * 메뉴 트리 조회
	 */
	@Override
	public List<Map<String, Object>> selectMenuTree() {
		List<Map<String, Object>> allMenuList = menuDAO.selectMenuTree(HttpUtil.getClientIp());
		return buildMenuTree(allMenuList, 2);
	}
	/**
	 * 메뉴 목록 조회
	 */
	@Override
	public List<Map<String, Object>> selectMenuList() {
		return menuDAO.selectMenuList(HttpUtil.getClientIp());
	}
	
	public List<Map<String, Object>> buildMenuTree(List<Map<String, Object>> allMenus, int startLevel) {
		// MENU_ID → 메뉴 map
		Map<String, Map<String, Object>> menuMap = new HashMap<>();
		for (Map<String, Object> menu : allMenus) {
			menuMap.put(menu.get("MENU_CD").toString(), menu);
		}

		// 부모 → 자식 매핑
		for (Map<String, Object> menu : allMenus) {
			String parentId = String.valueOf(menu.get("UP_MENU_CD"));
			if (parentId != null && menuMap.containsKey(parentId)) {
				Map<String, Object> parent = menuMap.get(parentId);
				List<Map<String, Object>> childList = (List<Map<String, Object>>) parent.get("childList");
				if (childList == null) {
					childList = new ArrayList<>();
					parent.put("childList", childList);
				}
				childList.add(menu);
			}
		}

		// 최상위 루트 찾기
		List<Map<String, Object>> result = new ArrayList<>();
		for (Map<String, Object> menu : allMenus) {
			int lv = Integer.parseInt(menu.get("LEVEL").toString());
			if (lv == startLevel) {
				result.add(menu);
			}
		}

		return result;
	}

}