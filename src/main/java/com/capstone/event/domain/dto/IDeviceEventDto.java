package com.capstone.event.domain.dto;

import java.time.LocalDateTime;

/**
 * 
 * DEVICE EVENT DTO Interface
 * 
 * @author swkim
 *
 */
public interface IDeviceEventDto {

	/**
	 * 생성일
	 * 이름
	 * 학번
	 * 과목명
	 * type
	 * 상태
	 * 메시지
	 * 학기
	 * count
	 * 
	 */
	LocalDateTime getCreateDate();
	String getName();
	String getNumber();
	String getClassNameKo();
	String getClassNameEn();
	String getClassCode();
	String getType();
	String getStatus();
	String getMessage();
	String getSemester();
	String getExamType();
	Integer getCountEvent();
	
}
