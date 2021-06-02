package com.capstone.classes.classMgmt.domain.dto;

import java.time.LocalDateTime;

/**
 * 
 * Class DTO Interface
 * 
 * @author swkim
 *
 */
public interface IClassDto {

	/**
	 * 이름
	 * 학번
	 * 학과 명
	 * 학과 코드
	 * 과목명
	 * 과목코드
	 * 학기
	 * 중간고사
	 * 기말고사
	 * 유형
	 * 클래스 멤버 아이디
	 * 
	 */
	String getName();
	String getStudentId();
	String getDeptNameKo();
	String getDeptNameEn();
	String getDeptCode();
	String getClassName();
	String getClassCode();
	String getSemester();
	String getType();
	Long getCmId();
	LocalDateTime getMidtermExam();
	LocalDateTime getFinalExam();

}
