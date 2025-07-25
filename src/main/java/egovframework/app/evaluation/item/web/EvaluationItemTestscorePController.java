package egovframework.app.evaluation.item.web;

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
@RequestMapping("/evaluation/item/testscore/popup")
@Controller
public class EvaluationItemTestscorePController extends AbstractController {
	
	@Resource(name = "EvaluationItemService")
	private EvaluationItemService evaluationItemService;
	
	@GetMapping("")
	public String page(Model model, @RequestParam String id) {
		
		model.addAttribute("id", id);
		
		return viewResolver(this, model);
	}
	
	@PostMapping("/selectScoreList")
	@ResponseBody
	public Map<String, Object> selectList(@RequestBody Map<String, Object> paramMap) {
		return makeDataTable(evaluationItemService.selectScoreList(paramMap));
	}
	
	@PostMapping("/selectScoreInfo")
	@ResponseBody
	public Map<String, Object> select(@RequestBody Map<String, Object> paramMap) {
		return evaluationItemService.selectScoreInfo(paramMap);
	}
}
