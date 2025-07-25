package egovframework.app.car.info.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.app.car.info.service.CarInfoService;
import egovframework.com.cmm.AbstractController;

/**
 * 자동차 모델 찾기 팝업
 * @author 이현우
 * @since 2025.04.29
 * @version 1.0
 * @see
 */
@RequestMapping("/car/info/popup")
@Controller
public class CarInfoPController extends AbstractController {

	@Resource(name = "CarInfoService")
	private CarInfoService carInfoService;
	
	@GetMapping("")
	public String page(Model model, @RequestParam String id) {
		model.addAttribute("id", id);
		
		model.addAttribute("carMakerList", carInfoService.selectCarMakerList());
		model.addAttribute("carN1KindList", carInfoService.selectCarN1KindList());
		model.addAttribute("carN2KindList", carInfoService.selectCarN2KindList());
		model.addAttribute("carFuelList", carInfoService.selectCarFuelList());
		model.addAttribute("carTransList", carInfoService.selectCarTransList());
		
		return viewResolver(this, model);
	}
	@PostMapping("/selectList")
	@ResponseBody
	public List<Map<String, Object>> selectList(@RequestBody Map<String, Object> paramMap) {
		return carInfoService.selectList(paramMap);
	}
}
