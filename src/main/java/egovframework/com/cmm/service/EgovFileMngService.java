package egovframework.com.cmm.service;

import java.io.IOException;
import java.util.Map;

import org.egovframe.rte.fdl.cmmn.exception.FdlException;

/**
 * @Class Name : EgovFileMngService.java
 * @Description : 파일정보의 관리를 위한 서비스 인터페이스
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
public interface EgovFileMngService {
	/**
	 * 하나의 파일에 대한 정보(속성 및 상세)를 등록한다.
	 * @throws IOException 
	 * @throws FdlException 
	 */
	public String saveFile(Map<String, Object> file) throws IOException;

	/**
	 * 파일을 삭제한다.
	 * @throws IOException 
	 */
	public void deleteFile(Map<String, Object> file) throws IOException;
	
	/**
	 * 파일에 대한 정보를 조회한다.
	 */
	public Map<String, Object> selectFile(Map<String, Object> file);
}
