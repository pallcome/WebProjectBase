package egovframework.com.cmm.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.egovframe.rte.fdl.cryptography.EgovCryptoService;
import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import egovframework.com.cmm.EgovWebUtil;
import egovframework.let.utl.fcc.service.EgovDateUtil;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

/**
 * @Class Name  : EgovFileMngUtil.java
 * @Description : 메시지 처리 관련 유틸리티
 * @Modification Information
 *
 *	 수정일		 수정자				   수정내용
 *	 -------		  --------		---------------------------
 *   2009.02.13	   이삼섭				  최초 생성
 *   2011.08.31  JJY			경량환경 템플릿 커스터마이징버전 생성
 *   2025.04.15  양민우			프로젝트 환경에 맞게 변경
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 02. 13
 * @version 1.0
 * @see
 *
 */
@Slf4j
@Component("EgovFileMngUtil")
public class EgovFileMngUtil {

	public static final int BUFF_SIZE = 2048;
	private final String FILE_STORE_PATH = EgovProperties.getProperty("Globals.fileStorePath");
	private final String ALGORITM_KEY = EgovProperties.getProperty("Globals.crypto.algoritm");
	// 파일 확장자
	private final String ALLOW_EXT_FILE = EgovProperties.getProperty("Globals.fileUpload.Extensions").toLowerCase();
	private final String ALLOW_EXT_IMAGE = EgovProperties.getProperty("Globals.fileUpload.Extensions.Images").toLowerCase();

	@Resource(name = "propertiesService")
	protected EgovPropertyService propertyService;
	
	/** 암호화서비스 */
	@Resource(name = "egovARIACryptoService")
	EgovCryptoService cryptoService;
	

	/**
	 * 첨부파일에 대한 목록 정보를 취득한다.
	 *
	 * @param files
	 * @return
	 * @throws IOException 
	 * @throws EgovBizException 
	 */
	public Map<String, Object> parseFileInf(MultipartFile file, Map<String, Object> thumb, String accept) throws IOException, EgovBizException {
	
		String yyyyMM = EgovDateUtil.getCurrentDate("yyyyMM");
		String storePathString = propertyService.getString("Globals.fileStorePath");
		String uploadPathString = "/" + yyyyMM;
	
		File saveFolder = new File(EgovWebUtil.filePathBlackList(storePathString + uploadPathString));
	
		if (!saveFolder.exists()) {
			FileUtils.forceMkdir(saveFolder);
		}
	
		Map<String, Object> fileMap = new HashMap<>();
	
	
		String orginFileName = file.getOriginalFilename();
		
		//--------------------------------------
		// 원 파일명이 null인 경우 처리
		//--------------------------------------
		if (EgovStringUtil.isEmpty(orginFileName)) {
			return null;
		}
		////------------------------------------
		
		int index = orginFileName.lastIndexOf(".");
		//String fileName = orginFileName.substring(0, index);
		String fileExt = orginFileName.substring(index + 1);
		String newName = EgovStringUtil.getTimeStamp() + ".tmp";
		long _size = file.getSize();
		File saveFile = null;

		if (!"".equals(orginFileName)) {
			String osName = System.getProperty("os.name").toLowerCase();
				
			String filePath = storePathString + uploadPathString + "/" + newName;
			Path winFilePath = Paths.get(filePath).toAbsolutePath();
			if (osName.contains("win")) {
				saveFile = new File(EgovWebUtil.filePathBlackList(winFilePath.toString()));
			} else {
				saveFile = new File(EgovWebUtil.filePathBlackList(filePath));
			}
			file.transferTo(saveFile);
		}
		
		fileMap.put("ULD_FILE_NM", uploadPathString + "/" + newName);
		fileMap.put("FILE_SZ", Long.toString(_size));
		fileMap.put("ORGNL_FILE_NM", orginFileName);
		fileMap.put("FILE_EXTN_NM", fileExt);
		
		
		String acceptType = accept.split("/")[0];
		if(!isAcceptAllowExt(file, acceptType) || !isFileAllowExt(acceptType, fileExt)) {
			throw new EgovBizException("허용된 확장자가 아닙니다.");
		}
		
		// 썸네일 생성
		String fileAcceptType = file.getContentType().split("/")[0];
		if("image".equals(fileAcceptType)) {
			// thumb 하위 폴더 생성
			String thumbDirPath = EgovWebUtil.filePathBlackList(storePathString + "/thumb" + uploadPathString);
			File thumbDir = new File(thumbDirPath);
			if (!thumbDir.exists()) {
				FileUtils.forceMkdir(thumbDir);
			}
			int width = EgovStringUtil.zeroConvert(thumb.get("width"));
			int height = EgovStringUtil.zeroConvert(thumb.get("height"));
			String thumbPath = EgovWebUtil.filePathBlackList(thumbDir + "/" + newName);
			File thumbFile = new File(thumbPath);
			BufferedImage thumbBI = Thumbnails.of(saveFile).size(width, height).asBufferedImage();
			ImageIO.write(thumbBI, fileExt, thumbFile); // 확장자 없이 저장
			
			// 사진의 width, height
			BufferedImage image = ImageIO.read(saveFile);
			
			fileMap.put("WDTH_SZ", image.getWidth());
			fileMap.put("VRTC_SZ", image.getHeight());
		}
		
		// 사용하지 않는 과거 임시파일 삭제
		deleteExpiredFiles(3);
	
		return fileMap;
	}
	
	// accept 확장자 제한
	private boolean isAcceptAllowExt(MultipartFile file, String acceptType) {
		String fileAcceptType = file.getContentType().split("/")[0];
		if("image".equals(acceptType) && !"image".equals(fileAcceptType)) {
			return false;
		}
		
		return true;
	}
	// 업로드 확장자 제한
	private boolean isFileAllowExt(String acceptType, String ext) {
		if (ext == null) return false;
		String lower = "." + ext.toLowerCase();
		
		if("image".equals(acceptType)) {
			return ALLOW_EXT_IMAGE.indexOf(lower) > -1;
		}else { // 파일
			return ALLOW_EXT_FILE.indexOf(lower) > -1;
		}
	}
	
	public String getThumbPath(String uploadFileName, String fileExt) {
		// 1. 암호화
		byte[] encryptedBytes = cryptoService.encrypt((uploadFileName + "." + fileExt).getBytes(), ALGORITM_KEY);
		// 2. Base64 인코딩 → 안전한 문자열
		String encodedString = Base64.getEncoder().encodeToString(encryptedBytes);
		
		return encodedString;
	}
	
	/**
	 * 지정한 루트 디렉토리(rootPath)로부터 시작하여 하위의 모든 디렉토리를 탐색하면서,
	 * expiredDay일이 지난 .tmp 파일을 삭제한다.
	 *
	 * @param expiredDay 파일이 마지막으로 수정된 후 삭제되기까지의 유효 일수
	 * @return 삭제된 파일의 총 개수
	 */
	public int deleteExpiredFiles(int expiredDays) {
		int deletedCount = 0;
		File rootDir = new File(FILE_STORE_PATH);

		if (!rootDir.exists() || !rootDir.isDirectory()) {
			return 0;
		}

		long now = System.currentTimeMillis();
		long expireMillis = expiredDays * 24L * 60 * 60 * 1000;

		deletedCount = deleteRecursively(rootDir, now, expireMillis);
		return deletedCount;
	}
	/**
	 * 주어진 디렉토리(dir)와 그 하위 디렉토리를 재귀적으로 순회하면서,
	 * .tmp 확장자를 가진 파일 중 expiredTime이 지난 파일을 삭제한다.
	 *
	 * @param dir 탐색을 시작할 디렉토리
	 * @param now 현재 시간 (System.currentTimeMillis())
	 * @param expireMillis expiredDay를 밀리초로 환산한 값 (삭제 유효 기준)
	 * @return 삭제된 파일의 개수
	 */
	private int deleteRecursively(File dir, long now, long expireMillis) {
		int count = 0;

		File[] files = dir.listFiles();
		if (files == null) return 0;

		for (File file : files) {
			if (file.isDirectory()) {
				count += deleteRecursively(file, now, expireMillis); // 하위 디렉토리 탐색
			} else if (file.getName().endsWith(".tmp")) {
				if (now - file.lastModified() > expireMillis) {
					if (file.delete()) {
						count++;
					} else {
						// 실패 로그 남기고 싶다면 여기에
						// logger.warn("삭제 실패: {}", file.getAbsolutePath());
					}
				}
			}
		}

		return count;
	}

}
