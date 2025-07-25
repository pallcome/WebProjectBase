package egovframework.app.sample.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import egovframework.com.cmm.AbstractServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * 샘플 서비스
 */
@Slf4j
@Service("SampleService")
public class SampleService extends AbstractServiceImpl {
	/**
	 * 목록 조회
	 */
	public List<Map<String, Object>> selectList(Map<String, Object> paramMap) {
		return sqlSession.selectList("SampleMapper.selectList", paramMap);
	}
	/**
	 * 조회
	 */
	public Map<String, Object> select(Map<String, Object> paramMap) {
		return sqlSession.selectOne("SampleMapper.select", paramMap);
	}
	/**
	 * 저장
	 */
	public void save(Map<String, Object> paramMap) {
		sqlSession.insert("SampleMapper.insert", paramMap);
	}
	/**
	 * 삭제
	 * @throws IOException 
	 */
	public void delete(Map<String, Object> paramMap) {
		sqlSession.delete("SampleMapper.delete", paramMap);
	}
}