package egovframework.com.cmm.advice;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 응답 어드바이스 클래스
 * @author 양민우
 * @since 2025.04.11
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *  2025.04.11  양민우     최초 생성
 *
 * </pre>
 */
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true; // 모든 JSON 응답에 적용
	}

	@Override
	public Object beforeBodyWrite(Object body
		, MethodParameter returnType
		, MediaType selectedContentType
		, Class<? extends HttpMessageConverter<?>> selectedConverterType
		, ServerHttpRequest request
		, ServerHttpResponse response) {

		if (body == null) { // return void 응답 처리
			return new HashMap<>();
		}

		// body가 이미 응답할 값이면 그대로 넘김
		return body;
	}
}
