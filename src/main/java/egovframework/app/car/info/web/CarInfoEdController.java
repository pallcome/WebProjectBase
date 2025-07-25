package egovframework.app.car.info.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.fdl.cmmn.exception.EgovBizException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.app.car.info.service.CarInfoService;
import egovframework.com.cmm.AbstractController;

/**
 * 자동차정보
 * @author 이현우
 * @since 2025.04.28
 * @version 1.0
 * @see
 */
@RequestMapping("/car/info/edit")
@Controller
public class CarInfoEdController extends AbstractController {
	@Resource(name = "CarInfoService")
	private CarInfoService carInfoService;
	
	@GetMapping("")
	public String page(Model model) {
		model.addAttribute("carMakerList", carInfoService.selectCarMakerList());
		model.addAttribute("carN1KindList", carInfoService.selectCarN1KindList());
		model.addAttribute("carN2KindList", carInfoService.selectCarN2KindList());
		model.addAttribute("carFuelList", carInfoService.selectCarFuelList());
		model.addAttribute("carTransList", carInfoService.selectCarTransList());
		
		return viewResolver(this, model);
	}
	@PostMapping("/selectList")
	@ResponseBody
	public Map<String, Object> selectList(@RequestBody Map<String, Object> paramMap) {
		return makeDataTable(carInfoService.selectList(paramMap));
	}
	@PostMapping("/selectInfo")
	@ResponseBody
	public Map<String, Object> selectInfo(@RequestBody Map<String, Object> paramMap) {
		return carInfoService.selectInfo(paramMap);
	}
	@PostMapping("/save")
	@ResponseBody
	public Map<String, Object> save(@RequestBody Map<String, Object> paramMap) throws IOException, EgovBizException {
		carInfoService.save(paramMap);
		return paramMap;
	}
	@PostMapping("/delete")
	@ResponseBody
	public void delete(@RequestBody Map<String, Object> paramMap) {
		carInfoService.delete(paramMap);
	}
	@PostMapping("/valid")
	@ResponseBody
	public Map<String, Object> valid(@RequestBody Map<String, Object> paramMap) throws IOException {
		paramMap.put("EXIST_YN", carInfoService.selectChkValid(paramMap));
		return paramMap;
	}
}
