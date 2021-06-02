package com.capstone.event.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.capstone.common.domain.entity.CommonEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "DEVICE_EVENT")
public class DeviceEventEntity extends CommonEntity {

	/**
	 * examCode	: 시험코드
	 * type		: 이벤트 타입
	 * status	: 이벤트 상태 (insert or remove)
	 * message	: 이벤트 메시지
	 *  
	 */

	@Column(nullable = false)
	private String examCode;
	
	@Column(nullable = false)
	private String type;	
	
	@Column
	private String status;
	
	@Column
	private String message;
	
}
