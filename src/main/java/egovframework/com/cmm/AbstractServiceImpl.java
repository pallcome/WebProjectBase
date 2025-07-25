package egovframework.com.cmm;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.let.utl.fcc.service.EgovStringUtil;

public abstract class AbstractServiceImpl extends EgovAbstractServiceImpl {
	@Autowired
	protected SqlSessionTemplate sqlSession;
	
	
	@Resource(name = "EgovFileMngUtil")
	private EgovFileMngUtil egovFileMngUtil;
	
	/**
	 * 이미지 미리보기
	 */
	protected void convertThumbPath(List<Map<String, Object>> list) {
		for(Map<String, Object> item : list) {
			convertThumbPath(item);
		}
	}
	protected void convertThumbPath(List<Map<String, Object>> list, String uploadFileNameKey, String fileExtKey) {
		for(Map<String, Object> item : list) {
			convertThumbPath(item, uploadFileNameKey, fileExtKey);
		}
	}
	protected void convertThumbPath(Map<String, Object> item) {
		String thumbPath = egovFileMngUtil.getThumbPath(EgovStringUtil.nullConvert(item.get("ULD_FILE_NM")), EgovStringUtil.nullConvert(item.get("FILE_EXPLN")));
		item.put("THUMB_PATH", thumbPath);
	}
	protected void convertThumbPath(Map<String, Object> item, String uploadFileNameKey, String fileExtKey) {
		String thumbPath = egovFileMngUtil.getThumbPath(EgovStringUtil.nullConvert(item.get(uploadFileNameKey)), EgovStringUtil.nullConvert(item.get(fileExtKey)));
		item.put(uploadFileNameKey + "_THUMB_PATH", thumbPath);
	}
	/**
	 * 키코드 조회
	 */
	protected String getKeyCode() {
		return sqlSession.selectOne("KeyCodeMapper.select");
	}
}
