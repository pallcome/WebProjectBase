package egovframework.let.utl.sim.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

import egovframework.com.cmm.EgovWebUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Base64인코딩/디코딩 방식을 이용한 데이터를 암호화/복호화하는 Business Interface class
 * @author 공통서비스개발팀 박지욱
 * @since 2009.01.19
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.01.19  박지욱          최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */
@Slf4j
public class EgovFileScrty {
	// 파일구분자
	static final String FILE_SEPARATOR = System.getProperty("file.separator");
	// 버퍼사이즈
	static final int BUFFER_SIZE = 1024;

	/**
	 * 파일을 암호화하는 기능
	 *
	 * @param source 암호화할 파일
	 * @param target 암호화된 파일
	 * @return boolean result 암호화여부 True/False
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @exception Exception
	 */
	public static boolean encryptFile(String source, String target) throws FileNotFoundException, IOException {

		// 암호화 여부
		boolean result = false;

		String sourceFile = EgovWebUtil.filePathBlackList(source.replace("\\", FILE_SEPARATOR).replace("/", FILE_SEPARATOR));
		String targetFile = EgovWebUtil.filePathBlackList(target.replace("\\", FILE_SEPARATOR).replace("/", FILE_SEPARATOR));
		File srcFile = new File(sourceFile);

		byte[] buffer = new byte[BUFFER_SIZE];

		// try-with-resources 적용
		try (
			FileInputStream fis = new FileInputStream(srcFile);
			BufferedInputStream input = new BufferedInputStream(fis);
			FileOutputStream fos = new FileOutputStream(targetFile);
			BufferedOutputStream output = new BufferedOutputStream(fos);
		) {
			if (srcFile.exists() && srcFile.isFile()) {
				int length = 0;
				while ((length = input.read(buffer)) >= 0) {
					byte[] data = new byte[length];
					System.arraycopy(buffer, 0, data, 0, length);
					output.write(encodeBinary(data).getBytes());
					output.write(System.getProperty("line.separator").getBytes());
				}
				result = true;
			}
		}
		// catch 블록 필요시 여기에 추가
		return result;
	}


	/**
	 * 파일을 복호화하는 기능
	 *
	 * @param source 복호화할 파일
	 * @param target 복호화된 파일
	 * @return boolean result 복호화여부 True/False
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * @exception Exception
	 */
	public static boolean decryptFile(String source, String target) throws FileNotFoundException, IOException {

		// 복호화 여부
		boolean result = false;

		String sourceFile = source.replace("\\", FILE_SEPARATOR).replace("/", FILE_SEPARATOR);
		String targetFile = target.replace("\\", FILE_SEPARATOR).replace("/", FILE_SEPARATOR);
		File srcFile = new File(sourceFile);

		String line = null;

		// try-with-resources 적용
		try (
			FileInputStream fis = new FileInputStream(srcFile);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader input = new BufferedReader(isr);
			FileOutputStream fos = new FileOutputStream(targetFile);
			BufferedOutputStream output = new BufferedOutputStream(fos);
		) {
			if (srcFile.exists() && srcFile.isFile()) {
				while ((line = input.readLine()) != null) {
					byte[] data = line.getBytes();
					output.write(decodeBinary(new String(data)));
				}
				result = true;
			}
		}
		// 필요시 catch 블록에서 로깅, 예외변환 추가

		return result;
	}

	/**
	 * 데이터를 암호화하는 기능
	 *
	 * @param data 암호화할 데이터
	 * @return String result 암호화된 데이터
	 * @exception Exception
	 */
	public static String encodeBinary(byte[] data) {
		if (data == null) {
			return "";
		}

		return new String(Base64.encodeBase64(data));
	}

	/**
	 * 데이터를 암호화하는 기능
	 *
	 * @param data 암호화할 데이터
	 * @return String result 암호화된 데이터
	 * @exception Exception
	 */
	public static String encode(String data) {
		return encodeBinary(data.getBytes());
	}

	/**
	 * 데이터를 복호화하는 기능
	 *
	 * @param data 복호화할 데이터
	 * @return String result 복호화된 데이터
	 * @exception Exception
	 */
	public static byte[] decodeBinary(String data) {
		return Base64.decodeBase64(data.getBytes());
	}

	/**
	 * 데이터를 복호화하는 기능
	 *
	 * @param String data 복호화할 데이터
	 * @return String result 복호화된 데이터
	 * @exception Exception
	 */
	public static String decode(String data) {
		return new String(decodeBinary(data));
	}

    /**
     * 비밀번호를 암호화하는 기능(복호화가 되면 안되므로 SHA-256 인코딩 방식 적용)
     *
     * @param password 암호화될 패스워드
     * @param id salt로 사용될 사용자 ID 지정
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public static String encryptPassword(String password, String id) throws NoSuchAlgorithmException {

		if (password == null) {
		    return "";
		}

		byte[] hashValue = null; // 해쉬값

		MessageDigest md = MessageDigest.getInstance("SHA-256");

		md.reset();
		md.update(id.getBytes());

		hashValue = md.digest(password.getBytes());

		return new String(Base64.encodeBase64(hashValue));
    }

    /**
     * 비밀번호를 암호화하는 기능(복호화가 되면 안되므로 SHA-256 인코딩 방식 적용)
     * @param data 암호화할 비밀번호
     * @param salt Salt
     * @return 암호화된 비밀번호
     * @throws NoSuchAlgorithmException 
     */
    public static String encryptPassword(String data, byte[] salt) throws NoSuchAlgorithmException {

		if (data == null) {
		    return "";
		}

		byte[] hashValue = null; // 해쉬값

		MessageDigest md = MessageDigest.getInstance("SHA-256");

		md.reset();
		md.update(salt);

		hashValue = md.digest(data.getBytes());

		return new String(Base64.encodeBase64(hashValue));
    }

    /**
     * 비밀번호를 암호화된 패스워드 검증(salt가 사용된 경우만 적용).
     *
     * @param data 원 패스워드
     * @param encoded 해쉬처리된 패스워드(Base64 인코딩)
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public static boolean checkPassword(String data, String encoded, byte[] salt) throws NoSuchAlgorithmException {
    	byte[] hashValue = null; // 해쉬값

    	MessageDigest md = MessageDigest.getInstance("SHA-256");

    	md.reset();
    	md.update(salt);
    	hashValue = md.digest(data.getBytes());

    	return MessageDigest.isEqual(hashValue, Base64.decodeBase64(encoded.getBytes()));
    }
}