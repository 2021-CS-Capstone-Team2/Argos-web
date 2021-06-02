package com.capstone.member.dto;

import java.util.ArrayList;

import com.capstone.classes.classMgmt.domain.entity.ClassEntity;
import com.capstone.common.domain.entity.MemberEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {

	/**
	 * 과목 Entity
	 */
	private ClassEntity classEntity;
	
	/**
	 * 사용자 Entity
	 */
	private MemberEntity memberEntity;
	
	/**
	 * Member Interface
	 */
	private IMemberDto iMemberDto;
	
	/**
	 * 과목 리스트
	 */
	private ArrayList<String> classList;
	
	/**
	 * 사용자 체크박스 
	 */
	private String[] memberList;
	
	/**
	 * 전체 이벤트 수
	 */
	private Integer eventCount;

	/**
	 * 전체 학생 수
	 */
	private Integer studentCount;
	
	/**
	 * 결과
	 */
	private String result;
}
