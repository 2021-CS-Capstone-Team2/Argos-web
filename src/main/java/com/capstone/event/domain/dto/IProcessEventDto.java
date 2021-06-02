package com.capstone.event.domain.dto;

import java.time.LocalDateTime;

/**
 * 
 * PROCESS EVENT DTO Interface
 * 
 * @author swkim
 *
 */
public interface IProcessEventDto {

	/**
	 * 생성일
	 * 이름
	 * 학번
	 * 과목명
	 * 감지된 프로세스
	 * 이벤트 프로세스
	 * 타이틀
	 * 
	 */
	LocalDateTime getCreateDate();
	String getName();
	String getNumber();
	String getClassNameKo();
	String getClassNameEn();
	String getClassCode();
	String getEventProcess();
	String getDetectedProcess();
	String getMainTitle();
	String getExamType();
	String getSemester();
}
