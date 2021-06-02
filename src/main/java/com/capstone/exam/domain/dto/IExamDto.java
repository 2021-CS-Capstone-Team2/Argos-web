package com.capstone.exam.domain.dto;

import java.time.LocalDateTime;

/**
 * Exam DTO Interface
 * 
 * @author swkim
 * 
 */
public interface IExamDto {

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
	String getMemberName();
	String getNumber();
	String getClassNameKo();
	String getClassNameEn();
	String getClassCode();
	String getSemester();
	String getExamType();
	String getMemberIp();
	String getStatus();
	
}
