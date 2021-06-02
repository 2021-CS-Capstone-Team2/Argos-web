package com.capstone.member.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.capstone.member.constant.MemberConstant;
import com.capstone.member.dto.MemberDto;
import com.capstone.member.service.MemberService;

import lombok.AllArgsConstructor;


/**
 * 사용자 관리 컨트롤러
 * 
 * @since 2021.04.09
 * @author swkim
 * 
 */
@Controller
@AllArgsConstructor
@RequestMapping("/argos/member")
public class MemberController {


	/**
	 * 사용자 서비스
	 */
	@Autowired
	private MemberService memberService;
	
	/**
	 * 사용자 리스트
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/getMemberList")
	public String getMemberList(Model model, Locale locale) {
		model.addAttribute("deptList", memberService.getDeptList());
		model.addAttribute("memberDto", memberService.getMemberList(locale));		
		return "argos/member/viewMemberList";
	}

	/**
	 * 사용자 수정
	 * 
	 * @param model
	 * @param memberDto
	 * @return
	 */
	@PatchMapping("/editMemberInfo")
	@ResponseBody
	public MemberDto saveUserInfo(Model model, MemberDto memberDto) {
		MemberDto resultDto = new MemberDto();
		try {
			resultDto.setResult(memberService.editMemberInfo(memberDto));
		} catch (Exception e) {
			resultDto.setResult(MemberConstant.MEMBER_MGMT_RESULT.FAIL.toString());
		}
		return resultDto;
	}
	
	/**
	 * 사용자 삭제
	 * 
	 * @param model
	 * @param memberDto
	 * @return
	 */
	@DeleteMapping("/deleteMember/{memberList}")
	@ResponseBody
	public MemberDto deleteCompany(Model model, MemberDto memberDto) {
		MemberDto resultDto = new MemberDto();
		String[] memberList = memberDto.getMemberList();

		try {
			resultDto.setResult(memberService.deleteMember(memberList));
		} catch (Exception e) {
			resultDto.setResult(MemberConstant.MEMBER_MGMT_RESULT.FAIL.toString());
		}
		return resultDto;
	}
	
}
