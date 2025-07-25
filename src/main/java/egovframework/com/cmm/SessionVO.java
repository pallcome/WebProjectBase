package egovframework.com.cmm;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 세션 VO 클래스
 * @author 공통서비스 개발팀 박지욱
 * @since 2009.03.06
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2009.03.06  박지욱          최초 생성
 *
 *  </pre>
 */
@Getter
@Setter
public class SessionVO implements Serializable {

	private static final long serialVersionUID = -2848741427493626376L;
	
	/** 고유아이디 */
	private String userCd;
	/** 아이디 */
	private String userId;
	/** 이름 */
	private String userNm;
}