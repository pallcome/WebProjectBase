package egovframework.com.cmm.service;

import java.util.List;
import java.util.Map;

public interface CodeService {
	public List<Map<String, Object>> selectList(String clsGroupCd);
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap);
}
