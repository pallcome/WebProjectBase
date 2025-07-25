package egovframework.com.cmm.service.impl;

import java.util.Map;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

/**
 * @Class Name : EgovFileMngDAO.java
 * @Description : 파일정보 관리를 위한 데이터 처리 클래스
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 3. 25.     이삼섭    최초생성
 *    2025. 4. 15.     양민우    프로젝트 환경에 맞게 변경
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 25.
 * @version
 * @see
 *
 */
@Repository("FileManageDAO")
public class FileManageDAO extends EgovAbstractMapper {
	/**
	 * 하나의 파일에 대한 정보(속성 및 상세)를 등록한다.
	 *
	 * @param vo
	 */
	public void insertFile(Map<String, Object> paramMap) {
		insert("FileManageMapper.insertFile", paramMap);
	}
	/**
	 * 하나의 파일에 대한 정보(속성 및 상세)를 수정한다.
	 *
	 * @param vo
	 */
	public void updateFile(Map<String, Object> paramMap) {
		update("FileManageMapper.updateFile", paramMap);
	}

	/**
	 * 하나의 파일을 삭제한다.
	 *
	 * @param fvo
	 */
	public void deleteFile(Map<String, Object> paramMap) {
		delete("FileManageMapper.deleteFile", paramMap);
	}

	/**
	 * 파일에 대한 정보를 조회한다.
	 *
	 * @param vo
	 * @return
	 */
	public Map<String, Object> selectFile(Map<String, Object> paramMap) {
		return selectOne("FileManageMapper.selectFile", paramMap);
	}
}
