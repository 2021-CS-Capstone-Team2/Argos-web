package com.capstone.member.dto;

import java.sql.Timestamp;

/**
 * 
 * Member DTO Interface
 * 
 * @author swkim
 *
 */
public interface IMemberDto {

	/**
	 * 이름
	 * 이메일
	 * 생성일자
	 * 학번
	 * 학과 명
	 * 학과 코드
	 * 유형
	 * 
	 */
	String getName();
	String getEmail();
	Timestamp getCreateDate();
	String getStudentId();
	String getDeptNameKo();
	String getDeptNameEn();
	String getDeptCode();
	String getStatus();
	String getType();
	
}
