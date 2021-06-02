package com.capstone.department.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.capstone.classes.classMgmt.constant.ClassConstant;
import com.capstone.classes.classMgmt.domain.dto.ClassDto;
import com.capstone.common.domain.entity.DepartmentEntity;
import com.capstone.department.service.DepartmentService;

import lombok.AllArgsConstructor;

/**
 * 학과 관리 컨트롤러
 * 
 * @since 2021.05.21
 * @author swkim
 * 
 */
@Controller
@AllArgsConstructor
public class DepartmentController {

	/**
	 * 사용자 서비스
	 */
	@Autowired
	private DepartmentService departmentService;

	/**
	 * 학과 리스트
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/argos/department/getDepartmentList")
	public String getDepartmentList(Model model) {
		model.addAttribute("deptList", departmentService.getAllDept());
		return "argos/department/viewDepartmentList";
	}

	/**
	 * 학과  생성
	 * 
	 * @param model
	 * @param classEntity
	 * @return
	 */
	@PostMapping("/argos/department/createDepartment")
	@ResponseBody
	public ClassDto createDepartment(Model model, DepartmentEntity departmentEntity) {
		ClassDto resultDto = new ClassDto();

		String result = "";
		try {
			result = departmentService.createDepartment(departmentEntity);
		} catch (Exception e) {
			result = ClassConstant.CLASS_PROCESS_RESULT.FAIL.toString();
		}
		resultDto.setResult(result);
		return resultDto;
	}

	/**
	 * 학과 수정 - 시스템 관리자
	 * 
	 * @param model
	 * @param classEntity
	 * @return
	 */
	@PatchMapping("/argos/department/editDepartment")
	@ResponseBody
	public ClassDto editDepartment(Model model, DepartmentEntity departmentEntity) {
		ClassDto resultDto = new ClassDto();

		String result = "";
		try {
			result = departmentService.editDepartment(departmentEntity);
		} catch (Exception e) {
			result = ClassConstant.CLASS_PROCESS_RESULT.FAIL.toString();
		}
		resultDto.setResult(result);
		return resultDto;
	}

}
