package com.capstone.classes.classMgmt.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.capstone.common.domain.entity.CommonEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "CLASS_MEMBER")
public class ClassMemberEntity extends CommonEntity {

	/**
	 * memberEmail	: 로그인 이메일
	 * classCode	: 과목코드
	 * semester		: 학기
	 * type			: STUDENT or MANAGER
	 *  
	 */

	@Column
	private String memberEmail;

	@Column
	private String classCode;

	@Column
	private String semester;

	@Column
	private String type;

}
