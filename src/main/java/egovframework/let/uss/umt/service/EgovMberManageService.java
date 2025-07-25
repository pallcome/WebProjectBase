package egovframework.let.uss.umt.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.exception.FdlException;
import org.egovframe.rte.fdl.idgnr.EgovIdGnrService;
import org.springframework.stereotype.Service;

import egovframework.com.cmm.AbstractServiceImpl;
import egovframework.let.utl.sim.service.EgovFileScrty;

/**
 * 일반회원관리에 관한비지니스클래스를 정의한다.
 */
@Service("mberManageService")
public class EgovMberManageService extends AbstractServiceImpl {
	/** egovUsrCnfrmIdGnrService */
	@Resource(name="egovUsrCnfrmIdGnrService")
	private EgovIdGnrService idgenService;

	/**
	 * 사용자의 기본정보를 화면에서 입력하여 항목의 정합성을 체크하고 데이터베이스에 저장
	 * @param mberManageVO 일반회원 등록정보
	 * @return result 등록결과
	 * @throws FdlException 
	 * @throws NoSuchAlgorithmException 
	 */
	public int insert(MberManageVO mberManageVO) throws FdlException, NoSuchAlgorithmException {
		//고유아이디 셋팅
		String uniqId = idgenService.getNextStringId();
		mberManageVO.setUniqId(uniqId);
		//패스워드 암호화
		String pass = EgovFileScrty.encryptPassword(mberManageVO.getPassword(), mberManageVO.getMberId());
		mberManageVO.setPassword(pass);

		int result = sqlSession.insert("mberManageDAO.insertMber_S", mberManageVO);
		return result;
	}

	/**
	 * 기 등록된 사용자 중 검색조건에 맞는 일반회원의 정보를 데이터베이스에서 읽어와 화면에 출력
	 * @param uniqId 상세조회대상 일반회원아이디
	 * @return mberManageVO 일반회원상세정보
	 */
	public MberManageVO selectMber(String uniqId) {
		MberManageVO mberManageVO = sqlSession.selectOne("mberManageDAO.selectMber_S", uniqId);
		return mberManageVO;
	}

	/**
	 * 기 등록된 회원 중 검색조건에 맞는 회원들의 정보를 데이터베이스에서 읽어와 화면에 출력
	 * @param userSearchVO 검색조건
	 * @return List<MberManageVO> 일반회원목록정보
	 */
	public List<MberManageVO> selectMberList(UserDefaultVO userSearchVO) {
		return sqlSession.selectList("mberManageDAO.selectMberList", userSearchVO);
	}

    /**
     * 일반회원 총 갯수를 조회한다.
     * @param userSearchVO 검색조건
     * @return 일반회원총갯수(int)
     */
	public int selectMberListTotCnt(UserDefaultVO userSearchVO) {
    	return sqlSession.selectOne("mberManageDAO.selectMberListTotCnt", userSearchVO);
    }

	/**
	 * 화면에 조회된 일반회원의 기본정보를 수정하여 항목의 정합성을 체크하고 수정된 데이터를 데이터베이스에 반영
	 * @param mberManageVO 일반회원수정정보
	 * @throws NoSuchAlgorithmException 
	 */
	public void updateMber(MberManageVO mberManageVO) throws NoSuchAlgorithmException {
		//패스워드 암호화
		if(mberManageVO.getPassword().isEmpty() || mberManageVO.getPassword().equals("")) {
			//업데이트 시 암호가 공백이면 암호화 과정 건너띈다. 
		} else {
			String pass = EgovFileScrty.encryptPassword(mberManageVO.getPassword(), mberManageVO.getMberId());
			mberManageVO.setPassword(pass);
		}
		sqlSession.update("mberManageDAO.updateMber_S", mberManageVO);
	}

	/**
	 * 화면에 조회된 사용자의 정보를 데이터베이스에서 삭제
	 * @param checkedIdForDel 삭제대상 일반회원아이디
	 */
	public void deleteMber(String checkedIdForDel)  {
		sqlSession.delete("mberManageDAO.deleteMber_S", checkedIdForDel);
	}

	/**
	 * 일반회원 약관확인
	 * @param stplatId 일반회원약관아이디
	 * @return 일반회원약관정보(List)
	 */
	public List<?> selectStplat(String stplatId)  {
        return sqlSession.selectList("mberManageDAO.selectStplat_S", stplatId);
	}

	/**
	 * 일반회원암호수정
	 * @param mberManageVO 일반회원수정정보(비밀번호)
	 */
	public void updatePassword(MberManageVO mberManageVO) {
		sqlSession.update("mberManageDAO.updatePassword_S", mberManageVO);
	}

	/**
	 * 일반회원이 비밀번호를 기억하지 못할 때 비밀번호를 찾을 수 있도록 함
	 * @param passVO 일반회원암호 조회조건정보
	 * @return mberManageVO 일반회원암호정보
	 */
	public MberManageVO selectPassword(MberManageVO passVO) {
		MberManageVO mberManageVO = sqlSession.selectOne("mberManageDAO.selectPassword_S", passVO);
		return mberManageVO;
	}

	/**
	 * 입력한 사용자아이디의 중복여부를 체크하여 사용가능여부를 확인
	 * @param checkId 중복여부 확인대상 아이디
	 * @return 사용가능여부(아이디 사용회수 int)
	 */
	public int checkIdDplct(String checkId) {
		return sqlSession.selectOne("mberManageDAO.checkIdDplct_S", checkId);
	}

}