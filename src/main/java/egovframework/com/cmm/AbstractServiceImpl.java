package egovframework.com.cmm;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;

import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.com.cmm.service.impl.KeyCodeDAO;
import egovframework.let.utl.fcc.service.EgovStringUtil;

public abstract class AbstractServiceImpl extends EgovAbstractServiceImpl {
	@Resource(name = "EgovFileMngUtil")
	private EgovFileMngUtil egovFileMngUtil;
	@Resource(name = "KeyCodeDAO")
	private KeyCodeDAO keyCodeDAO;
	
	/**
	 * 이미지 미리보기
	 */
	public void convertThumbPath(List<Map<String, Object>> list) {
		for(Map<String, Object> item : list) {
			convertThumbPath(item);
		}
	}
	public void convertThumbPath(List<Map<String, Object>> list, String uploadFileNameKey, String fileExtKey) {
		for(Map<String, Object> item : list) {
			convertThumbPath(item, uploadFileNameKey, fileExtKey);
		}
	}
	public void convertThumbPath(Map<String, Object> item) {
		String thumbPath = egovFileMngUtil.getThumbPath(EgovStringUtil.nullConvert(item.get("ULD_FILE_NM")), EgovStringUtil.nullConvert(item.get("FILE_EXPLN")));
		item.put("THUMB_PATH", thumbPath);
	}
	public void convertThumbPath(Map<String, Object> item, String uploadFileNameKey, String fileExtKey) {
		String thumbPath = egovFileMngUtil.getThumbPath(EgovStringUtil.nullConvert(item.get(uploadFileNameKey)), EgovStringUtil.nullConvert(item.get(fileExtKey)));
		item.put(uploadFileNameKey + "_THUMB_PATH", thumbPath);
	}
	/**
	 * 키코드 조회
	 */
	public String getKeyCode() {
		return keyCodeDAO.select();
	}
}
