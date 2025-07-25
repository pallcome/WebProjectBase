package egovframework.app.evaluation.item.web;

import java.io.IOException;
import java.util.HashMap;
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
 * 주요 시험항목
 * @author 이현우
 * @since 2025.04.16
 * @version 1.0
 * @see
 *
 */
@RequestMapping("/evaluation/item/maintest/edit")
@Controller
public class EvaluationItemMaintestEdController extends AbstractController {
	
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
		paramMap.put("SYS_YN", "Y");
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
	
	@SuppressWarnings("unchecked")
	@PostMapping("/save")
	@ResponseBody
	public Map<String, Object> save(@RequestBody Map<String, Object> paramMap) throws IOException {
		evaluationItemService.save(paramMap);
		return (Map<String, Object>) paramMap.get("formData");
	}
}
