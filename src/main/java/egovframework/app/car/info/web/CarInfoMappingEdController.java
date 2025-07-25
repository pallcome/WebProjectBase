package egovframework.app.car.info.web;

import java.io.IOException;
import java.util.List;
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

import egovframework.app.car.info.service.CarInfoMappingService;
import egovframework.com.cmm.AbstractController;

/**
 * 자동차 모델 매핑
 * @author 이현우
 * @since 2025.04.29
 * @version 1.0
 * @see
 */
@RequestMapping("/car/info/mapping/edit")
@Controller
public class CarInfoMappingEdController extends AbstractController {
	
	@Resource(name = "CarInfoMappingService")
	private CarInfoMappingService carInfoMappingService;
	
	@GetMapping("")
	public String page(Model model) {
		return viewResolver(this, model);
	}
	@PostMapping("/selectTree")
	@ResponseBody
	public List<Map<String, Object>> selectTreeList(@RequestBody Map<String, Object> paramMap) {
		return carInfoMappingService.selectTreeList(paramMap);
	}
	@PostMapping("/selectInfo")
	@ResponseBody
	public Map<String, Object> selectInfo(@RequestBody Map<String, Object> paramMap) {
		return carInfoMappingService.selectInfo(paramMap);
	}
	@PostMapping("/selectList")
	@ResponseBody
	public Map<String, Object> selectList(@RequestBody Map<String, Object> paramMap) {
		return makeDataTable(carInfoMappingService.selectList(paramMap));
	}
	@PostMapping("/save")
	@ResponseBody
	public Map<String, Object> save(@RequestBody Map<String, Object> paramMap) {
		carInfoMappingService.save(paramMap);
		return paramMap;
	}
	@PostMapping("/delete")
	@ResponseBody
	public void delete(@RequestBody Map<String, Object> paramMap) {
		carInfoMappingService.delete(paramMap);
	}
}
