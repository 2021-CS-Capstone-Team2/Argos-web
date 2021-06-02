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
@Table(name = "PROCESS_EVENT")
public class ProcessEventEntity extends CommonEntity {

	/**
	 * examCode	: 시험코드
	 * eventProcess	: 이벤트 프로세스
	 * detectedProcess	: 탐지된 프로세스
	 * mainTitle	: 이름
	 *  
	 */
	@Column(nullable = false)
	private String examCode;
	
	@Column
	private String eventProcess;
	
	@Column
	private String detectedProcess;	
	
	@Column
	private String mainTitle;
	
}
