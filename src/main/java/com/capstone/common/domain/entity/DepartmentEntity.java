package com.capstone.common.domain.entity;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DEPARTMENT")
public class DepartmentEntity extends CommonEntity {

	/**
	 * name	: 학과명
	 * code	: 학과코드
	 */
	@Column(nullable = false)
	private String nameKo;
	
	@Column(nullable = false)
	private String nameEn;
	
	@Column(nullable = false)
	private String code;

}