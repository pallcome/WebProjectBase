package egovframework.com.cmm.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.com.cmm.AbstractController;
import egovframework.com.cmm.service.EgovFileMngUtil;
import egovframework.let.utl.fcc.service.EgovStringUtil;

@RequestMapping("/file/upload")
@Controller
public class FileUploadController extends AbstractController {
	@Resource(name = "EgovFileMngUtil")
	private EgovFileMngUtil egovFileMngUtil;
	
	@PostMapping("")
	@ResponseBody
	public List<Map<String, Object>> upload(MultipartHttpServletRequest mptRequest) throws EgovBizException, IOException {
		List<MultipartFile> mfList = mptRequest.getFiles("fileUpload");
		
		// JSON 문자열 꺼내기
		String thumbStr = mptRequest.getParameter("thumb");
		Map<String, Object> thumb = null;
		if(EgovStringUtil.isEmpty(thumbStr) == false) {
			// Jackson이나 Gson으로 파싱
			ObjectMapper mapper = new ObjectMapper();
			thumb = mapper.readValue(thumbStr, Map.class);
		}else {
			thumb = new HashMap<>();
			thumb.put("width", 200);
			thumb.put("height", 200);
		}
		
		// accept 확인
		String accept = EgovStringUtil.nullConvert(mptRequest.getParameter("accept"));
		List<Map<String, Object>> list = new ArrayList<>();
		for(MultipartFile mf : mfList) {
			Map<String, Object> fileMap = egovFileMngUtil.parseFileInf(mf, thumb, accept);
			
			if(fileMap.isEmpty() == false) {
				list.add(fileMap);
			}
		}
		
		return list;
	}
}
