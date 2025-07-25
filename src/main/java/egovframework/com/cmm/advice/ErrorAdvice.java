package egovframework.com.cmm.advice;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 오류 어드바이스 클래스
 * @author 양민우
 * @since 2025.05.08
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *  2025.05.08  양민우     최초 생성
 *
 * </pre>
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ErrorAdvice {
	// 1) DB 관련 예외 전용 핸들러
	@ExceptionHandler({DataAccessException.class, SQLException.class})
	public Object handleDatabase(HttpServletRequest request, Exception ex) {
		return message(request, ex, "오류가 발생했습니다.<br/>관리자에게 문의하시기 바랍니다.");
	}
	@ExceptionHandler(Exception.class)
	public Object handleAny(HttpServletRequest request, Exception ex) {
		return message(request, ex);
	}
	
	
	
	private Object message(HttpServletRequest request, Exception ex) {
		return message(request, ex, ex.getMessage());
	}
	private Object message(HttpServletRequest request, Exception ex, String message) {
		// 오류 로그 출력
		log.error("", ex);
		
		// AJAX 요청이면 JSON 으로 응답
		String ajaxHeader = request.getHeader("X-Requested-With");
		if ("XMLHttpRequest".equals(ajaxHeader)) {
			// ex.getMessage() 만 내려주거나, 더 복잡한 DTO로도 가능
			return ResponseEntity
				.status(460)
				.body(message);
		}
		
		// 페이지 요청이면 error.html 로 이동
		return "error"; // src/main/resources/templates/error.html
	}
}
