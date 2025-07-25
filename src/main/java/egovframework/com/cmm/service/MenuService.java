package egovframework.com.cmm.service;

import java.util.List;
import java.util.Map;

public interface MenuService {
	/**
	 * 메뉴 트리 조회
	 */
	public List<Map<String, Object>> selectMenuTree();
	/**
	 * 메뉴 목록 조회
	 */
	public List<Map<String, Object>> selectMenuList();
}
