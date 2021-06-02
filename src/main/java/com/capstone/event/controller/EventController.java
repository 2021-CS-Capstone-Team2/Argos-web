package com.capstone.event.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.capstone.event.service.EventService;
import com.capstone.exam.service.ExamService;

import lombok.AllArgsConstructor;


/**
 * 이벤트 관리 컨트롤러
 * 
 * @since 2021.04.09
 * @author swkim
 * 
 */
@Controller
@AllArgsConstructor
@RequestMapping("/event")
public class EventController {

	/**
	 * 시험 서비스
	 */
	@Autowired
	private ExamService examService;
	/**
	 * 이벤트 서비스
	 */
	@Autowired
	private EventService eventService;	

	/**
	 * 디바이스 이벤트
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("/getDeviceEventList")
	public String getDeviceEventList(Model model) {
		// 로그인 유저 이메일
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginEmail = auth.getName();
		// 나의 시험 코드 리스트 가져오기
		List<String> examList = examService.getMyExamCodeList(loginEmail);
		

		model.addAttribute("classList", eventService.getDeviceEventClass(examList));
		model.addAttribute("eventDto", eventService.getDeviceEventLog(examList));
		return "event/viewDeviceEventList";
	}
	
	/**
	 * 프로세스 이벤트
	 * 
	 * @param model
	 * @param session
	 * @return
	 */
	@GetMapping("/getProcessEventList")
	public String getProcessEventList(Model model) {
		// 로그인 유저 이메일
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginEmail = auth.getName();
		// 나의 시험 코드 리스트 가져오기
		List<String> examList = examService.getMyExamCodeList(loginEmail);
		
		model.addAttribute("classList", eventService.getProcessEventClass(examList));
		model.addAttribute("eventDto", eventService.getProcessEventLog(examList));
		return "event/viewProcessEventList";
	}


}
