package com.capstone.exam.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.capstone.classes.classMgmt.service.ClassService;
import com.capstone.exam.constant.ExamConstant;
import com.capstone.exam.domain.dto.ExamDto;
import com.capstone.exam.domain.entity.ExamEntity;
import com.capstone.exam.domain.entity.ExamEventSettingEntity;
import com.capstone.exam.service.ExamService;

import lombok.AllArgsConstructor;

/**
 * 시험 관리 컨트롤러
 * 
 * @since 2021.04.23
 * @author swkim
 * 
 */
@Controller
@AllArgsConstructor
@RequestMapping("/exam")
public class ExamController {

	/**
	 * 시험 관리 서비스
	 */
	@Autowired
	private ExamService examService;

	/**
	 * 과목 관리 서비스
	 */
	@Autowired
	private ClassService classService;

	/**
	 * 시험 관리 페이지 - 과목 담당자 (MANAGER)
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/getExamMgmt")
	public String getExamMgmt(Model model) {
		model.addAttribute("examDto", examService.getMyExamList());
		model.addAttribute("classList", classService.getMyClasses());

		return "classes/exam/viewExamMgmt";
	}

	/**
	 * 시험 로그 페이지 - 과목 담당자 (MANAGER)
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/getExamLog")
	public String getExamLog(Model model) {
		model.addAttribute("iDto", examService.getMyExamLog());

		return "classes/exam/viewExamLog";
	}

	/**
	 * 시험 생성
	 * 
	 * @param model
	 * @param examEntity
	 * @return
	 */
	@PostMapping("/createExam")
	@ResponseBody
	public ExamDto createExam(Model model, ExamEntity examEntity, Long classId, String exam) {
		ExamDto resultDto = new ExamDto();
		LocalDateTime examDate = LocalDateTime.parse(exam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

		examEntity.setExamDate(examDate);
		String result = "";
		try {
			result = examService.createExam(examEntity, classId);
		} catch (Exception e) {
			result = ExamConstant.EXAM_PROCESS_RESULT.FAIL.toString();
		}
		resultDto.setResult(result);
		return resultDto;
	}

	/**
	 * 시험 삭제
	 * 
	 * @param model
	 * @param examId
	 * @return
	 */
	@DeleteMapping("/deleteExam")
	@ResponseBody
	public ExamDto deleteExam(Model model, Long examId) {
		ExamDto resultDto = new ExamDto();

		String result = "";
		try {
			result = examService.deleteExam(examId);
		} catch (Exception e) {
			result = ExamConstant.EXAM_PROCESS_RESULT.FAIL.toString();
		}
		resultDto.setResult(result);
		return resultDto;
	}

	/**
	 * 시험 수정
	 * 
	 * @param model
	 * @param examEntity
	 * @return
	 */
	@PatchMapping("/editExam")
	@ResponseBody
	public ExamDto editExam(Model model, ExamEntity examEntity, String exam) {
		ExamDto resultDto = new ExamDto();

		LocalDateTime examDate = LocalDateTime.parse(exam, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

		examEntity.setExamDate(examDate);

		String result = "";
		try {
			result = examService.editExam(examEntity);
		} catch (Exception e) {
			result = ExamConstant.EXAM_PROCESS_RESULT.FAIL.toString();
		}
		resultDto.setResult(result);
		return resultDto;
	}

	/**
	 * 시험 이벤트 설정 저장
	 * 
	 * @param model
	 * @param examEventSettingEntity
	 * @return
	 */
	@PatchMapping("/updateExamEventSetting")
	@ResponseBody
	public ExamDto updateExamEventSetting(Model model, ExamEventSettingEntity examEventSettingEntity) {
		ExamDto resultDto = new ExamDto();

		String result = "";
		try {
			result = examService.updateExamEventSetting(examEventSettingEntity);
		} catch (Exception e) {
			result = ExamConstant.EXAM_PROCESS_RESULT.FAIL.toString();
		}
		resultDto.setResult(result);
		return resultDto;
	}
	
	/**
	 * 개인 시험 일정을 가져온다.
	 * 
	 * @param model
	 * @return
	 */
	@PostMapping("/getExamCalendar")
	@ResponseBody
	public ArrayList<ExamDto> getExamCalendar(Model model) {
		return examService.getMyExamList();
	}
	
}
