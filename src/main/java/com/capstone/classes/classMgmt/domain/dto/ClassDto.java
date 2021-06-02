package com.capstone.classes.classMgmt.domain.dto;

import com.capstone.classes.classMgmt.domain.entity.ClassEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassDto {

	/**
	 * 과목 Entity
	 */
	private ClassEntity classEntity;
	
	/**
	 * 결과
	 */
	private String result;
}
