package egovframework.com.cmm.web;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.egovframe.rte.fdl.cryptography.EgovCryptoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import egovframework.com.cmm.SessionVO;
import egovframework.com.cmm.service.EgovFileMngService;
import egovframework.let.utl.fcc.service.EgovStringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @Class Name : EgovImageProcessController.java
 * @Description :
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 4. 2.     이삼섭
 *    2011.08.31.     JJY        경량환경 템플릿 커스터마이징버전 생성
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 4. 2.
 * @version
 * @see
 *
 */
@Slf4j
@Controller
@Tag(name="EgovImageProcessController",description = "이미지 처리")
public class EgovImageProcessController extends HttpServlet {

	/**
	 *  serialVersion UID
	 */
	private static final long serialVersionUID = -6339945210971171173L;

	@Resource(name = "EgovFileMngService")
	private EgovFileMngService fileService;
	
	/** 암호화서비스 */
    @Resource(name="egovARIACryptoService")
    EgovCryptoService cryptoService;
    
    @Value("${file.path}")
	private String FILE_STORE_PATH;
    @Value("${crypto.algoritm}")
	private String ALGORITM_KEY;

	/**
	 * 첨부된 이미지에 대한 미리보기 기능을 제공한다.
	 *
	 * @param atchFileId
	 * @param fileSn
	 * @param sessionVO
	 * @param model
	 * @param response
	 * @throws IOException 
	 */
    @Operation(
			summary = "이미지 미리보기",
			description = "첨부된 이미지에 대한 미리보기 기능을 제공",
			tags = {"EgovImageProcessController"}
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "성공")
	})
    @GetMapping("/file/image")
	public void getImageInf(
			@Parameter(hidden = true)SessionVO sessionVO, 
			ModelMap model, 
			@Parameter(
					in = ParameterIn.QUERY,
					schema = @Schema(type = "object",
							additionalProperties = Schema.AdditionalPropertiesValue.TRUE, 
							ref = "#/components/schemas/fileMap"),
					style = ParameterStyle.FORM,
					explode = Explode.TRUE
			) @RequestParam Map<String, Object> commandMap,
		HttpServletResponse response) throws IOException {
		String path = (String) commandMap.get("path");
//		path = path.replaceAll(" ", "+");
//		byte[] decodedBytesPath = Base64.getDecoder().decode(path);
//		String decodedPath = new String(cryptoService.decrypt(decodedBytesPath, ALGORITM_KEY));
		
		if (EgovStringUtil.isEmpty(path)) {
			return;
		}

		// 경로 및 확장자 검증 코드 추가 필요(화이트리스트 등)
		String uploadFileName = path.substring(0, path.lastIndexOf("."));
		String fileExtsn = path.substring(path.lastIndexOf(".") + 1).toLowerCase();
		String storePathString = FILE_STORE_PATH + "/thumb" + uploadFileName;
		File file = new File(storePathString);

		if (!file.exists() || !file.isFile()) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "파일이 존재하지 않습니다.");
			return;
		}

		try (
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream in = new BufferedInputStream(fis);
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		) {
			int imgByte;
			while ((imgByte = in.read()) != -1) {
				bStream.write(imgByte);
			}

			String type = "application/octet-stream";
			if ("jpg".equals(fileExtsn) || "jpeg".equals(fileExtsn)) {
				type = "image/jpeg";
			} else if ("png".equals(fileExtsn)) {
				type = "image/png";
			} else if ("gif".equals(fileExtsn)) {
				type = "image/gif";
			} // 등등 화이트리스트 확장자만 허용

			response.setHeader("Content-Type", type);
			response.setContentLength(bStream.size());

			bStream.writeTo(response.getOutputStream());
			response.getOutputStream().flush();
			// close()는 하지 않음
		} catch (IOException e) {
			log.error("이미지 파일 전송 중 오류 발생");
		}
	}
}
