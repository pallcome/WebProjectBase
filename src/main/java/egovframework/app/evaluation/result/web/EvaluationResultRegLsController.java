package egovframework.app.evaluation.result.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.app.base.car.service.BaseCarCarTypeService;
import egovframework.app.base.car.service.BaseCarFuelTypeService;
import egovframework.app.base.car.service.BaseCarManufacturerService;
import egovframework.app.base.car.service.BaseCarTransmissionService;
import egovframework.app.evaluation.result.service.EvaluationResultService;
import egovframework.com.cmm.AbstractController;

/**
 * 대상 차량 평가 결과 등록 목록 컨트롤러 클래스
 * @author 양민우
 * @since 2025.04.07
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *  2025.04.07  양민우     최초 생성
 *
 * </pre>
 */
@RequestMapping("/evaluation/result/reg")
@Controller
public class EvaluationResultRegLsController extends AbstractController {
	@Resource(name = "EvaluationResultService")
	private EvaluationResultService evaluationResultService;
	@Resource(name = "BaseCarManufacturerService")
	private BaseCarManufacturerService baseCarManufacturerService;
	@Resource(name = "BaseCarCarTypeService")
	private BaseCarCarTypeService baseCarCarTypeService;
	@Resource(name = "BaseCarFuelTypeService")
	private BaseCarFuelTypeService baseCarFuelTypeService;
	@Resource(name = "BaseCarTransmissionService")
	private BaseCarTransmissionService baseCarTransmissionService;
	
	@GetMapping("")
	public String page(Model model) {
		Map<String, Object> paramMap = new HashMap<>();
		
		// 시험년도
		model.addAttribute("testYearList", evaluationResultService.selectTestYearList());
		// 제조사
		model.addAttribute("mkrCdList", baseCarManufacturerService.selectList(paramMap));
		// 차종 분류1
		paramMap.put("SEARCH_CLSF_SE_CD", List.of("1"));
		model.addAttribute("clsfSeCd1List", baseCarCarTypeService.selectList(paramMap));
		// 차종 분류2
		paramMap.put("SEARCH_CLSF_SE_CD", List.of("2"));
		model.addAttribute("clsfSeCd2List", baseCarCarTypeService.selectList(paramMap));
		// 연료
		model.addAttribute("fuelCdList", baseCarFuelTypeService.selectList(paramMap));
		// 변속기 분류
		model.addAttribute("trsmCdList", baseCarTransmissionService.selectList(paramMap));
		
		return viewResolver(this, model);
	}
	@PostMapping("/selectList")
	@ResponseBody
	public Map<String, Object> selectList(@RequestBody Map<String, Object> paramMap) {
		return makeDataTable(evaluationResultService.selectList(paramMap));
	}
}
