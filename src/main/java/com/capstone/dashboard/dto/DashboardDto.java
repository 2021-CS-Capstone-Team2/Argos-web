package com.capstone.dashboard.dto;

import java.util.List;

import com.capstone.classes.classMgmt.domain.entity.ClassEntity;
import com.capstone.exam.domain.entity.ExamEntity;
import com.capstone.exam.domain.entity.ExamEventSettingEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardDto {

	/**
	 * 과목 Entity
	 */
	private ClassEntity classEntity;

	/**
	 * 시험 Entity
	 */
	private ExamEntity examEntity;

	/**
	 * 시험 이벤트 Entity
	 */
	private ExamEventSettingEntity examEventSettingEntity;

	/**
	 * 전체 이벤트 수
	 */
	private Integer eventCount;

	/**
	 * 전체 학생 수
	 */
	private Integer studentCount;
	
	/**
	 * 시험종료 학생 수
	 */
	private Integer finStudentCount;

	/**
	 * 이벤트 발생 학생 리스트
	 */
	private List<IDashboardDto> iDto;

	/**
	 * 이벤트 발생 타임라인
	 */
	private List<IDashboardDto> timeLine;
	
	/**
	 * 다국어
	 */
	private String lang;

	/**
	 * 결과
	 */
	private String result;
}
