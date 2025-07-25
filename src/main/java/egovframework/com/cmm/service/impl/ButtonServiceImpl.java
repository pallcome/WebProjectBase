package egovframework.com.cmm.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.AbstractServiceImpl;
import egovframework.com.cmm.service.ButtonService;

@Service("ButtonService")
public class ButtonServiceImpl extends AbstractServiceImpl implements ButtonService {
	@Resource(name = "ButtonDAO")
	private ButtonDAO buttonDAO;

	/**
	 * 사용자의 메뉴별 버튼 권한 목록 조회.
	 * @param paramMap
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectAuthBtnList(Map<String, Object> paramMap) {
		return buttonDAO.selectAuthBtnList(paramMap);
	}
}
