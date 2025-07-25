package egovframework.com.cmm.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.egovframe.rte.fdl.cmmn.exception.FdlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.AbstractServiceImpl;
import egovframework.com.cmm.EgovWebUtil;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.let.utl.fcc.service.EgovStringUtil;

/**
 * @Class Name : EgovFileMngServiceImpl.java
 * @Description : 파일정보의 관리를 위한 구현 클래스
 * @Modification Information
 *
 *	수정일	   수정자		 수정내용
 *	-------		-------	 -------------------
 *	2009. 3. 25.	 이삼섭	최초생성
 *	2025. 4. 15.	 양민우	프로젝트 환경에 맞게 변경
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 25.
 * @version
 * @see
 *
 */
@Service("EgovFileMngService")
public class EgovFileMngServiceImpl extends AbstractServiceImpl implements EgovFileMngService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Value("${file.path}")
	private String FILE_STORE_PATH;
	@Resource(name = "FileManageDAO")
	private FileManageDAO fileMngDAO;

	/**
	 * 하나의 파일에 대한 정보(속성 및 상세)를 등록한다.
	 * @throws IOException 
	 * @throws FdlException 
	 */
	@Override
	public String saveFile(Map<String, Object> file) throws IOException {
		String uldFileMm = EgovStringUtil.nullConvert(file.get("ULD_FILE_NM"));
		String orgnlUldFileNm = EgovStringUtil.nullConvert(file.get("ORGNL_ULD_FILE_NM"));
		String atchFileCd = EgovStringUtil.nullConvert(file.get("ATCH_FILE_CD"));
		
		// 첨부파일 저장을 할 수 없는 경우
		if(EgovStringUtil.isEmpty(uldFileMm) && EgovStringUtil.isEmpty(orgnlUldFileNm)) return "";
		
		// 신규
		if(EgovStringUtil.isEmpty(atchFileCd)) {
			atchFileCd = getKeyCode();
			file.put("ATCH_FILE_CD", atchFileCd);
			// 첨부파일 rename
			renameFile(file);
			
			fileMngDAO.insertFile(file);
			
		// 수정 : ORGNL_ULD_FILE_NM와 ULD_FILE_NM 다른 경우
		}else if(orgnlUldFileNm.equals(uldFileMm) == false && EgovStringUtil.isEmpty(uldFileMm) == false) {
			deleteFile(file);
			
			// 첨부파일 rename
			renameFile(file);
			
			fileMngDAO.insertFile(file);
			
		// 삭제
		}else if(EgovStringUtil.isEmpty(orgnlUldFileNm) == false && EgovStringUtil.isEmpty(uldFileMm) == true) {
			deleteFile(file);
		}
	
		return atchFileCd;
	}

	/**
	 * 파일을 삭제한다.
	 * @throws IOException 
	 */
	@Override
	public void deleteFile(Map<String, Object> file) throws IOException {
		String uldFileMm = EgovStringUtil.nullConvert(file.get("ORGNL_ULD_FILE_NM"));
		if(EgovStringUtil.isEmpty(uldFileMm)) return;
		
		fileMngDAO.deleteFile(file);
		
		// 업로드 파일
		File uldFile = new File(EgovWebUtil.filePathBlackList(FILE_STORE_PATH + uldFileMm)); // 저장
		FileUtils.deleteQuietly(uldFile);
		
		// 썸네일 파일
		File thumbUldFile = new File(EgovWebUtil.filePathBlackList(FILE_STORE_PATH + "/thumb" + uldFileMm)); // 저장
		FileUtils.deleteQuietly(thumbUldFile);
	}
	
	/**
	 * 파일에 대한 정보를 조회한다.
	 */
	@Override
	public Map<String, Object> selectFile(Map<String, Object> file) {
		return fileMngDAO.selectFile(file);
	}
	// 물리적인 파일 처리
	private void renameFile(Map<String, Object> file) throws IOException {
		String tmpUldFileMm = EgovStringUtil.nullConvert(file.get("ULD_FILE_NM"));
		String uldFileMm = tmpUldFileMm.substring(0, tmpUldFileMm.lastIndexOf("."));
		
		// 업로드 파일
		File tmpUldFile = new File(EgovWebUtil.filePathBlackList(FILE_STORE_PATH + tmpUldFileMm)); // 임시
		File uldFile = new File(EgovWebUtil.filePathBlackList(FILE_STORE_PATH + uldFileMm)); // 저장
		// 썸네일 파일
		File tmpThumbUldFile = new File(EgovWebUtil.filePathBlackList(FILE_STORE_PATH + "/thumb" + tmpUldFileMm)); // 임시
		File thumbUldFile = new File(EgovWebUtil.filePathBlackList(FILE_STORE_PATH + "/thumb" + uldFileMm)); // 저장
		
			
		if(tmpThumbUldFile.exists()) { // 썸네일
			try {
				Files.move(tmpThumbUldFile.toPath(), thumbUldFile.toPath());
			}catch(IOException e) {
				logger.error("IOException", e);
				throw e;
			}
		}
		if(tmpUldFile.exists()) { // 파일
			try {
				Files.move(tmpUldFile.toPath(), uldFile.toPath());
			}catch(IOException e) {
				logger.error("IOException", e);
				throw e;
			}
		}
		
		file.put("ULD_FILE_NM", uldFileMm);
	}
}
