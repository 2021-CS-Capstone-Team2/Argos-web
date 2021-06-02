package com.capstone.classes.classMgmt.domain.entity;

import java.time.LocalDateTime;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.capstone.common.domain.entity.CommonEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CLASS")
public class ClassEntity extends CommonEntity {

	/**
	 *
	 * name		 	: 과목 명
	 * code			: 과목 코드
	 * semester		: 학기
	 * midtermExam	: 중간고사
	 * finalExam	: 기말고사
	 *  
	 */

	@Column(nullable = false)
	private String nameKo;
	
	@Column(nullable = false)
	private String nameEn;
	
	@Column(nullable = false)
	private String code;

	@Column(nullable = false)
	private String semester;

	@Column
	private LocalDateTime midtermExam;

	@Column
	private LocalDateTime finalExam;
	
}
