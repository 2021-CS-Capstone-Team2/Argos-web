package com.capstone.common.controller;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.capstone.common.constant.CommonConstant;
import com.capstone.common.domain.entity.MemberAuthEntity;
import com.capstone.common.domain.entity.MemberEntity;
import com.capstone.common.service.CommonService;
import com.capstone.member.dto.MemberDto;

import lombok.AllArgsConstructor;

/**
 * 공통 컨트롤러
 * 
 * @since 2021.04.08
 * @author swkim
 * 
 */
@Controller
@AllArgsConstructor
public class CommonController {

	/**
	 * 사용자 서비스
	 */
	@Autowired
	private CommonService commonService;
	
	// 메인 페이지 - 최초 진입
	@RequestMapping("/")
	public String index(Model model) {
		if(SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
			return "viewLogin";
		} else {
			return "redirect:/main";
		}
	}

	// 로그인 Submit
	@PostMapping("/login")
	public String loginPost(Model model) {
		return "viewLogin";
	}

	// 로그인 Submit
	@GetMapping("/login")
	public String login(Model model) {
		if(SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
			return "viewLogin";
		} else {
			return "redirect:/main";
		}
	}
	
	// 회원가입 페이지
	@RequestMapping("/signUp")
	public String signUp(Model model) {
		if(SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
			model.addAttribute("deptList", commonService.getAllDepartment());
			return "viewSignUp";
		} else {
			return "redirect:/main";
		}
	}
	
	// 로그아웃 결과 페이지
	@GetMapping("/user/logout/result")
	public String dispLogout() {
		return "redirect:/login";
	}

	@RequestMapping("/main/branch")
	public String branch(Model model, RedirectAttributes redirectAttributes, Locale locale, HttpSession session , Device device) {

		MemberEntity user = commonService.getUserInfo(SecurityContextHolder.getContext().getAuthentication().getName());
		
		session.setAttribute("memberName", user.getName());
    	session.setAttribute("lang", locale.getLanguage());
    	
		return "redirect:/main";
	}

	@RequestMapping("/main")
	public String main(Model model, HttpSession session, Locale locale, String darkMode) {
		if(SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser")) {
			return "viewLogin";
		} else {
			session.setAttribute("lang", locale.getLanguage());
			if(session.getAttribute("dMode") != null && darkMode == null) {
				darkMode = (String) session.getAttribute("dMode");
			} else if(session.getAttribute("dMode") == null) {
				session.setAttribute("dMode", "false");
			} else {
				session.setAttribute("dMode", darkMode);
			}
			return "common/main";
		}
	}

	// 접근 거부 페이지
	@GetMapping("/denied")
	public String dispDenied() {
		return "denied";
	}
	
	/**
	 * 회원가입
	 * 
	 * @param model
	 * @param param
	 * @param param2
	 * @return
	 */
	@PostMapping("/sign/signUp")
	@ResponseBody
	public MemberDto signUp(Model model, MemberEntity param) {
		MemberDto resultDto = new MemberDto();
		param.setStatus(CommonConstant.MEMBER_STATUS.N.toString());
		param.setType("MANAGER");
		Set<MemberAuthEntity> authSet = new HashSet<MemberAuthEntity>();

		authSet.add(new MemberAuthEntity("MANAGER"));
		String result = "";
		try {
			result = commonService.setMemberAndAuthSet(param, authSet);
			resultDto.setResult(result);
		} catch(Exception e) {
			result = CommonConstant.PROCESS_RESULT.FAIL.toString();
			resultDto.setResult(result);
			
			return resultDto;
		}

		return resultDto;
	}

	/**
	 * 회원가입 -> 로그인으로
	 * 
	 * @param model
	 * @param param
	 * @return
	 */
	@PostMapping("/sign/signIn")
	public String signIn(Model model, MemberEntity param) {

		return "redirect:/login";
	}
}