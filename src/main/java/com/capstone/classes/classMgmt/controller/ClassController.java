package com.capstone.classes.classMgmt.controller;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.capstone.classes.classMgmt.constant.ClassConstant;
import com.capstone.classes.classMgmt.domain.dto.ClassDto;
import com.capstone.classes.classMgmt.domain.entity.ClassEntity;
import com.capstone.classes.classMgmt.domain.entity.ClassMemberEntity;
import com.capstone.classes.classMgmt.service.ClassService;
import com.capstone.member.service.MemberService;

import lombok.AllArgsConstructor;

/**
 * 과목 관리 컨트롤러
 * 
 * @since 2021.04.08
 * @author swkim
 * 
 */
@Controller
@AllArgsConstructor
public class ClassController {

	/**
	 * 과목 서비스
	 */
	@Autowired
	private ClassService classService;
	/**
	 * 사용자 서비스
	 */
	@Autowired
	private MemberService memberService;

	/**
	 * 과목 리스트 - 시스템 관리자 (SYSTEM)
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/argos/classMgmt/getClassList")
	public String getClassListAdmin(Model model) {
		model.addAttribute("classList", classService.getAllClasses());
		return "argos/classes/viewClassList";
	}

	/**
	 * 과목 생성
	 * 
	 * @param model
	 * @param classEntity
	 * @return
	 */
	@PostMapping("/argos/classMgmt/createClass")
	@ResponseBody
	public ClassDto createClass(Model model, ClassEntity classEntity) {
		ClassDto resultDto = new ClassDto();

		String result = "";
		try {
			result = classService.createClass(classEntity);
		} catch (Exception e) {
			result = ClassConstant.CLASS_PROCESS_RESULT.FAIL.toString();
		}
		resultDto.setResult(result);
		return resultDto;
	}

	/**
	 * 과목 삭제
	 * 
	 * @param model
	 * @param classEntity
	 * @return
	 */
	@DeleteMapping("/argos/classMgmt/deleteClass")
	@ResponseBody
	public ClassDto deleteClass(Model model, ClassEntity classEntity) {
		ClassDto resultDto = new ClassDto();

		String result = "";
		try {
			result = classService.deleteClass(classEntity);
		} catch (Exception e) {
			result = ClassConstant.CLASS_PROCESS_RESULT.FAIL.toString();
		}
		resultDto.setResult(result);
		return resultDto;
	}

	/**
	 * 과목 수정 - 시스템 관리자
	 * 
	 * @param model
	 * @param classEntity
	 * @return
	 */
	@PatchMapping("/argos/classMgmt/editClass")
	@ResponseBody
	public ClassDto editClass(Model model, ClassEntity classEntity) {
		ClassDto resultDto = new ClassDto();

		String result = "";
		try {
			result = classService.editClass(classEntity);
		} catch (Exception e) {
			result = ClassConstant.CLASS_PROCESS_RESULT.FAIL.toString();
		}
		resultDto.setResult(result);
		return resultDto;
	}

	/**
	 * 과목 구성원 등록 페이지
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/argos/classMgmt/getClassMemberList")
	public String getClassMemberList(Model model) {
		model.addAttribute("memberList", memberService.getClassMemberList());
		model.addAttribute("classList", classService.getAllClasses());

		return "argos/classes/viewClassMemberList";
	}

	/**
	 * 과목 구성원 등록
	 * 
	 * @param model
	 * @param memberList
	 * @param classId
	 * @param cmType
	 * @return
	 */
	@PostMapping("/argos/classMgmt/regiClassMember")
	@ResponseBody
	public ClassDto regiClassMember(Model model, String[] memberList, Long classId, String cmType) {
		ClassDto resultDto = new ClassDto();

		String result = "";
		try {
			result = classService.regiClassMember(memberList, classId, cmType);
		} catch (Exception e) {
			result = ClassConstant.CLASS_PROCESS_RESULT.FAIL.toString();
		}
		resultDto.setResult(result);

		return resultDto;
	}

	/**
	 * 과목 관리 페이지 - 과목 담당자 (MANAGER)
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/classMgmt/getClassMgmt")
	public String getClassList(Model model) {
		model.addAttribute("classList", classService.getMyClasses());
		return "classes/classMgmt/viewClassList";
	}

	/**
	 * 과목 수정 - 과목 담당자 (MANAGER)
	 * 
	 * @param model
	 * @param classEntity
	 * @param midterm
	 * @param finalE
	 * @return
	 * @throws ParseException
	 */
	@PatchMapping("/classMgmt/editMyClass")
	@ResponseBody
	public ClassDto editMyClass(Model model, ClassEntity classEntity, String midterm, String finalE) throws ParseException {
		ClassDto resultDto = new ClassDto();
		// 시간 설정
		LocalDateTime midtermExam = null;
		LocalDateTime finalExam = null;
		if (midterm.isBlank()) {
		} else {
			midtermExam = LocalDateTime.parse(midterm, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		}
		if (finalE.isBlank()) {
		} else {
			finalExam = LocalDateTime.parse(finalE, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		}

		classEntity.setMidtermExam(midtermExam);
		classEntity.setFinalExam(finalExam);
		String result = "";
		try {
			result = classService.editClass(classEntity);
		} catch (Exception e) {
			result = ClassConstant.CLASS_PROCESS_RESULT.FAIL.toString();
		}
		resultDto.setResult(result);
		return resultDto;
	}

	/**
	 * 과목 구성원 관리 페이지 - 과목 담당자 (MANAGER)
	 * 
	 * @param model
	 * @param classEntity
	 * @return
	 */
	@GetMapping("/classMgmt/classMemberMgmt")
	public String classMemberMgmt(Model model, ClassEntity classEntity) {
		ClassEntity selectClass = classService.getSelectClass(classEntity);
		if (selectClass != null) {
			model.addAttribute("selectClass", selectClass);
			model.addAttribute("classMember", classService.getClassMemberList(selectClass));
		} else {
			// 추후 에러페이지로
		}

		return "classes/classMgmt/viewClassMemberMgmt";
	}

	/**
	 * 과목 구성원 삭제 - 과목 담당자 (MANAGER)
	 * 
	 * @param model
	 * @param cmEntity
	 * @return
	 */
	@DeleteMapping("/classMgmt/deleteClassMember")
	@ResponseBody
	public ClassDto deleteClassMember(Model model, ClassMemberEntity cmEntity) {
		ClassDto resultDto = new ClassDto();

		String result = "";
		try {
			result = classService.deleteClassMember(cmEntity);
		} catch (Exception e) {
			result = ClassConstant.CLASS_PROCESS_RESULT.FAIL.toString();
		}
		resultDto.setResult(result);

		return resultDto;
	}

}
