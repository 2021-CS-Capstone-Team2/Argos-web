package com.capstone.dashboard.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.capstone.dashboard.dto.DashboardDto;
import com.capstone.dashboard.dto.IDashboardDto;
import com.capstone.dashboard.service.DashboardService;
import com.capstone.exam.constant.ExamConstant;
import com.capstone.exam.domain.entity.ExamEntity;
import com.capstone.exam.service.ExamService;

import lombok.AllArgsConstructor;


/**
 * 대시보드 관리 컨트롤러
 * 
 * @since 2021.04.09
 * @author swkim
 * 
 */
@Controller
@AllArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

	/**
	 * 이벤트 서비스
	 */
	@Autowired
	private DashboardService dashboardService;
	
	/**
	 * 시험 서비스
	 */
	@Autowired
	private ExamService examService;

	/**
	 * 시험 선택
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("/getTotalDashboard")
	public String getTotalDashboard(Model model, ExamEntity examEntity) {
		model.addAttribute("dto", dashboardService.getSelectClass(examEntity));
		model.addAttribute("allExam", examService.getMyExamList());
		return "dashboard/viewTotalDashboard";
	}

	/**
	 * 시험 선택
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@PostMapping("/getSelectDashboard")
	public String getSelectDashboard(Model model, ExamEntity examEntity) {
		model.addAttribute("dto", dashboardService.getSelectClass(examEntity));
		model.addAttribute("allExam", examService.getMyExamList());
		return "dashboard/viewTotalDashboard";
	}
	
	/**
	 * 최근 기록
	 * 
	 * @param model
	 * @param examEntity
	 * @return
	 */
	@PostMapping("/getEventTimeLine")
	@ResponseBody
	public List<IDashboardDto> getEventTimeLine(Model model, ExamEntity examEntity) {
		return dashboardService.getEventTimeLine(examEntity);
	}
	
	/**
	 * 실시간 이벤트 가져오기
	 * 발생 이벤트 수, 종료 학생 수, 	부정행위 판독, 학생 이벤트 현황
	 * 
	 * @param model
	 * @param examEntity
	 * @return
	 */
	@PostMapping("/getRealTimeEvent")
	@ResponseBody
	public DashboardDto getRealTimeEvent(Model model, ExamEntity examEntity) {
		DashboardDto resultDto = new DashboardDto();
		
		try {
			resultDto = dashboardService.getRealTimeEvent(examEntity);
		} catch(Exception e) {
			resultDto.setResult(ExamConstant.EXAM_PROCESS_RESULT.FAIL.toString());
			return resultDto;
		}
		
		return resultDto;
	}
	
	/**
	 * 디바이스 이벤트 가져오기
	 * 
	 * @param model
	 * @param examEntity
	 * @return
	 */
	@PostMapping("/getDeviceEventTime")
	@ResponseBody	
	public DashboardDto getDeviceEventTime(Model model, ExamEntity examEntity) {
		DashboardDto resultDto = new DashboardDto();
		
		try {
			resultDto = dashboardService.getDeviceEventTime(examEntity);
		} catch(Exception e) {
			resultDto.setResult(ExamConstant.EXAM_PROCESS_RESULT.FAIL.toString());
			return resultDto;
		}
		
		return resultDto;
	}
}
