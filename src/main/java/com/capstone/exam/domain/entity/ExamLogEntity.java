package com.capstone.exam.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.capstone.common.domain.entity.CommonEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "EXAM_LOG")
public class ExamLogEntity extends CommonEntity {

	/**
	 * memberEmail	: 로그인 이메일
	 * memberIp		: 사용자 아이피
	 * examCode		: 시험코드
	 * status		: 시험 상태 (시험중, 시험종료)
	 *  
	 */

	@Column
	private String memberEmail;
	
	@Column
	private String memberIp;

	@Column
	private String examCode;

	@Column
	private String status;

}
