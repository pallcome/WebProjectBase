package egovframework.com.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import egovframework.com.cmm.interceptor.CustomAuthenticInterceptor;
import egovframework.com.cmm.interceptor.LogInterceptor;
import egovframework.com.cmm.interceptor.SessionInterceptor;

/**
 * @ClassName : EgovConfigWebDispatcherServlet.java
 * @Description : DispatcherServlet 설정
 *
 * @author : 윤주호
 * @since  : 2021. 7. 20
 * @version : 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일              수정자               수정내용
 *  -------------  ------------   ---------------------
 *   2021. 7. 20    윤주호               최초 생성
 * </pre>
 *
 */
@Configuration
@ComponentScan(basePackages = "egovframework", excludeFilters = {
	@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Service.class),
	@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Repository.class),
	@ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)
})
public class EgovConfigWebDispatcherServlet implements WebMvcConfigurer {

	// =====================================================================
	// RequestMappingHandlerMapping 설정
	// =====================================================================
	// -------------------------------------------------------------
	// RequestMappingHandlerMapping 설정 - Interceptor 추가
	// -------------------------------------------------------------
	@Resource(name="SessionInterceptor")
	private SessionInterceptor sessionInterceptor;
	@Resource(name="LogInterceptor")
	private LogInterceptor logInterceptor;
	@Resource(name="CustomAuthenticInterceptor")
	private CustomAuthenticInterceptor customAuthenticInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		final String[] EXCLUDE_RESOURCE = {"/css/**", "/fonts/**", "/img/**", "/js/**", "/plugin/**", "/image/**", "/file/**"};
		final String[] EXCLUDE_PAGE = {"/", "/main/**", "/reload", "/error"};
		
		registry.addInterceptor(sessionInterceptor)
		.addPathPatterns("/**")
		.excludePathPatterns(EXCLUDE_RESOURCE);
		
		registry.addInterceptor(logInterceptor)
		.addPathPatterns("/**")
		.excludePathPatterns(EXCLUDE_RESOURCE)
		.excludePathPatterns(EXCLUDE_PAGE);
		
		registry.addInterceptor(customAuthenticInterceptor)
		.addPathPatterns("/**")
		.excludePathPatterns(EXCLUDE_RESOURCE)
		.excludePathPatterns(EXCLUDE_PAGE);
	}

	// -------------------------------------------------------------
	// RequestMappingHandlerMapping 설정 View Controller 추가
	// -------------------------------------------------------------
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/cmmn/validator.do")
			.setViewName("cmmn/validator");
//		registry.addViewController("/").setViewName("app/main/main");
	}
}
