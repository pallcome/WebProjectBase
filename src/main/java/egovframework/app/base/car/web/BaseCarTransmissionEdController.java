package egovframework.app.base.car.web;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.app.base.car.service.BaseCarTransmissionService;
import egovframework.com.cmm.AbstractController;

/**
 * 변속기 컨트롤러 클래스
 * @author 양민우
 * @since 2025.04.17
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *  2025.04.17  양민우     최초 생성
 *
 * </pre>
 */
@RequestMapping("/base/car/transmission/edit")
@Controller
public class BaseCarTransmissionEdController extends AbstractController {
	@Resource(name = "BaseCarTransmissionService")
	private BaseCarTransmissionService baseCarTransmissionService;
	
	@GetMapping("")
	public String page(Model model) {
		return viewResolver(this, model);
	}
	@PostMapping("/selectList")
	@ResponseBody
	public Map<String, Object> selectList(@RequestBody Map<String, Object> paramMap) {
		return makeDataTable(baseCarTransmissionService.selectList(paramMap));
	}
	@PostMapping("/select")
	@ResponseBody
	public Map<String, Object> select(@RequestBody Map<String, Object> paramMap) {
		return baseCarTransmissionService.select(paramMap);
	}
	@PostMapping("/save")
	@ResponseBody
	public Map<String, Object> save(@RequestBody Map<String, Object> paramMap) {
		baseCarTransmissionService.save(paramMap);
		return paramMap;
	}
	@PostMapping("/delete")
	@ResponseBody
	public void delete(@RequestBody Map<String, Object> paramMap) {
		baseCarTransmissionService.delete(paramMap);
	}
}
