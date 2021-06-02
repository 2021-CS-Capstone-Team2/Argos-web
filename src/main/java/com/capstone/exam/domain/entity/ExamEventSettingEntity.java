package com.capstone.exam.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.capstone.common.domain.entity.CommonEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "EXAM_EVENT_SETTING")
public class ExamEventSettingEntity extends CommonEntity {

	/**
	 * examId		: 시험 ID
	 * processEvent	: 프로세스 이벤트 사용
	 * keyboardEvent: 키보드 이벤트 사용
	 * mouseEvent	: 마우스 이벤트 사용
	 * audioEvent	: 오디오 이벤트 사용
	 * eyeTrackingEvent	: 아이트래킹 사용
	 *  
	 */

	@ManyToOne
	@JoinColumn(name = "examId", nullable = false)
	private ExamEntity exam;

	@Column
	private String processEvent;

	@Column
	private String keyboardEvent;

	@Column
	private String mouseEvent;

	@Column
	private String audioEvent;

	@Column
	private String eyeTrackingEvent;

}
