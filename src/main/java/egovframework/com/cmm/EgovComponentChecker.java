package egovframework.com.cmm;

import egovframework.com.cmm.AbstractServiceImpl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;


/**
 * EgovComUtil 클래스
 *
 * @author 서준식
 * @since 2011.09.15
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    -------------    ----------------------
 *   2011.09.15  서준식        최초 생성
 * </pre>
 */

@Service("egovUtil")
public class EgovComponentChecker extends AbstractServiceImpl implements ApplicationContextAware{


	public static ApplicationContext context;

	@Override
	@SuppressWarnings("static-access")
	public void setApplicationContext(ApplicationContext context)
		throws BeansException {

		this.context = context;
	}

	/**
	 * Spring MVC에서 설정한 빈이 아닌 서비스 빈(컴포넌트)만을 검색할 수 있음
	 *
	*/
	public static boolean hasComponent(String componentName){

		try{
			Object component = context.getBean(componentName);

			// Fix: Null pointers should not be dereferenced 이슈 수정
			if(ObjectUtils.isEmpty(component)){
				return false;
			}else{
				return true;
			}

		}catch(NoSuchBeanDefinitionException ex){// 해당 컴포넌트를 찾을 수없을 경우 false반환
			return false;
		}
	}

}
