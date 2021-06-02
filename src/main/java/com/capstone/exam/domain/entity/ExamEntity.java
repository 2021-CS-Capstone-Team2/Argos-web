package com.capstone.exam.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.capstone.common.domain.entity.CommonEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "EXAM")
public class ExamEntity extends CommonEntity {

	/**
	 * code			: 시험코드
	 * classCode	: 과목코드
	 * semester		: 학기
	 * examDate		: 시험 일시
	 * type			: 시험 종류 (퀴즈, 중간고사, 기말고사 등)
	 *  
	 */

	@Column
	private String code;

	@Column
	private String classCode;

	@Column
	private String semester;
	
	@Column
	private LocalDateTime examDate;	

	@Column
	private String type;

}
