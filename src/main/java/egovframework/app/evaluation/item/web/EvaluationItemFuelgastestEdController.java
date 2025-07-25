package egovframework.app.evaluation.item.web;

import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.app.evaluation.item.service.EvaluationItemService;
import egovframework.com.cmm.AbstractController;

/**
 * 연비 온실가스 시험 항목
 * @author 이현우
 * @since 2025.04.16
 * @version 1.0
 * @see
 *
 */
@RequestMapping("/evaluation/item/fuelgastest/edit")
@Controller
public class EvaluationItemFuelgastestEdController extends AbstractController {
	
	@Resource(name = "EvaluationItemService")
	private EvaluationItemService evaluationItemService;
	
	@GetMapping("")
	public String page(Model model) {
		model.addAttribute("dsUnit", evaluationItemService.selectUnitList());
		return viewResolver(this, model);
	}
	
	@PostMapping("/selectList")
	@ResponseBody
	public Map<String, Object> selectList(@RequestBody Map<String, Object> paramMap) {
		paramMap.put("UP_TEST_ARTCL_CD", "GHG");
		paramMap.put("SYS_YN", "N");
		return makeDataTable(evaluationItemService.selectList(paramMap));
	}
	
	@PostMapping("/selectInfo")
	@ResponseBody
	public Map<String, Object> select(@RequestBody Map<String, Object> paramMap) {
		return evaluationItemService.selectInfo(paramMap);
	}
	
	@PostMapping("/selectApplyList")
	@ResponseBody
	public Map<String, Object> selectApplyList(@RequestBody Map<String, Object> paramMap) {
		return makeDataTable(evaluationItemService.selectApplyList(paramMap));
	}
	
	@PostMapping("/save")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Map<String, Object> save(@RequestBody Map<String, Object> paramMap) throws IOException {
		paramMap.put("UP_TEST_ARTCL_CD", "GHG");
		paramMap.put("SYS_YN", "N");
		evaluationItemService.save(paramMap);
		
		return (Map<String, Object>) paramMap.get("formData");
	}
	
	@PostMapping("/delete")
	@ResponseBody
	public void delete(@RequestBody Map<String, Object> paramMap) throws IOException {
		evaluationItemService.delete(paramMap);
	}
}
