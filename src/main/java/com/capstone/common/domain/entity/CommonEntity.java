package com.capstone.common.domain.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class CommonEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createDate;

	@Column(length = 500)
	private String createMemberId;

	@LastModifiedDate
	private LocalDateTime updateDate;

	@Column(length = 500)
	private String updateMemberId;
	
	public CommonEntity() {
		super();
		this.createDate = createDate != null ? createDate : LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS);
		this.createMemberId = StringUtils.hasLength(createMemberId) ? createMemberId :  "admin";
	}

}
