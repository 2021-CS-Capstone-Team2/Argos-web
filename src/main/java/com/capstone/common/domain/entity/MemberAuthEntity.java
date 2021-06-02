package com.capstone.common.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "MEMBER_AUTH")
public class MemberAuthEntity extends CommonEntity {

	/**
	 * auth_code : 권한 코드 (SYSTEM, MANAGER, STUDENT)
	 * member_id : MEMBER ID
	 */
	@Column(nullable = false)
	private String authCode; 

	@ManyToOne
	@JoinColumn(name = "memberId", nullable = false)
	private MemberEntity member;

	public MemberAuthEntity() {
		super();
	}
	
	public MemberAuthEntity(String authCode) {
		super();
		this.authCode = authCode;
	}
}
