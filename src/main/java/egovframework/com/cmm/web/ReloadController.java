package egovframework.com.cmm.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.com.cmm.AbstractController;
import egovframework.com.cmm.advice.CommonAdvice;

/**
 * 새로고침 컨트롤러 클래스
 * @author 양민우
 * @since 2025.04.10
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *  2025.04.10  양민우     최초 생성
 *
 * </pre>
 */
@RequestMapping("/reload")
@Controller
public class ReloadController extends AbstractController {
	@Autowired
	private CommonAdvice commonAdvice;
	
	@GetMapping("")
	public String page(Model model) {
		commonAdvice.refresh();
		
		return "redirect:/main";
	}
}
