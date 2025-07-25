package egovframework.app.evaluation.result.web;

import java.util.HashMap;
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

import egovframework.app.evaluation.result.service.EvaluationResultService;
import egovframework.com.cmm.AbstractController;

/**
 * 대상 차량 평가 결과 등록 상세
 * @author 이현우
 * @since 2025.04.18
 * @version 1.0
 * @see
 */
@RequestMapping("/evaluation/result/reg/edit")
@Controller
public class EvaluationResultRegEdController extends AbstractController {
	@Resource(name = "EvaluationResultService")
	private EvaluationResultService evaluationResultService;
	
	@GetMapping("")
	public String page(Model model, @RequestParam String id) {
		model.addAttribute("CAR_MDL_CD", id);
		model.addAttribute("editable", true);
		return viewResolver(this, model);
	}
	@PostMapping("/select")
	@ResponseBody
	public Map<String, Object> select(@RequestBody Map<String, Object> paramMap) {
		Map<String, Object> result = new HashMap<>();
		
		// 자동차 정보
		result.put("carInfo", evaluationResultService.selectCarInfo(paramMap));
		
		// 종합정보
		paramMap.put("TEST_ARTCL_CD", "TOT");
		Map<String, Object> totInfo = evaluationResultService.selectTotInfo(paramMap);
		result.put("totInfo", totInfo);
		// 종합정보 배점기준
		result.put("totTestScoreList", makeDataTable(evaluationResultService.selectTestScoreList(totInfo)));
		
		// 연비/온실가스
		paramMap.put("TEST_ARTCL_CD", "GHG");
		Map<String, Object> ghgInfo = evaluationResultService.selectInfo(paramMap);
		result.put("ghgInfo", ghgInfo);
		// 연비/온실가스 항목별 평가 결과
		result.put("ghgResultScoreList", makeDataTable(evaluationResultService.selectResultScoreList(ghgInfo)));
		
		// LCA 전생애주기
		paramMap.put("TEST_ARTCL_CD", "LCA");
		Map<String, Object> lcaInfo = evaluationResultService.selectInfo(paramMap);
		result.put("lcaInfo", lcaInfo);
		// LCA 전생애주기 항목별 평가 결과
		result.put("lcaResultScoreList", makeDataTable(evaluationResultService.selectResultScoreList(lcaInfo)));
		
		// 차내실내공기질
		paramMap.put("TEST_ARTCL_CD", "AIR");
		Map<String, Object> airInfo = evaluationResultService.selectInfo(paramMap);
		result.put("airInfo", airInfo);
		// 차내실내공기질 항목별 평가 결과
		result.put("airResultScoreList", makeDataTable(evaluationResultService.selectResultScoreList(airInfo)));
		
		return result;
	}
	@PostMapping("/selectTestScore")
	@ResponseBody
	public Map<String, Object> selectTestScore(@RequestBody Map<String, Object> paramMap) {
		return makeDataTable(evaluationResultService.selectTestScoreList(paramMap));
	}
	@PostMapping("/save")
	@ResponseBody
	public void save(@RequestBody Map<String, Object> paramMap) {
		evaluationResultService.save(paramMap);
	}
}
