package egovframework.app.main.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import egovframework.com.cmm.AbstractController;

/**
 * 메인 컨트롤러 클래스
 * @author 양민우
 * @since 2025.04.02
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *  2025.04.02  양민우     최초 생성
 *
 * </pre>
 */
@Controller
public class MainController extends AbstractController {
	@GetMapping("/")
	public String page(Model model) {
		return "redirect:/evaluation/result/reg";
	}
	@GetMapping("/main")
	public String main(Model model) {
		return "redirect:/evaluation/result/reg";
	}
}
