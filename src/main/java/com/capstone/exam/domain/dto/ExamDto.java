package com.capstone.exam.domain.dto;

import com.capstone.classes.classMgmt.domain.entity.ClassEntity;
import com.capstone.exam.domain.entity.ExamEntity;
import com.capstone.exam.domain.entity.ExamEventSettingEntity;
import com.capstone.exam.domain.entity.ExamLogEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamDto implements Comparable<ExamDto> {

	/**
	 * 과목 Entity
	 */
	ClassEntity classEntity;

	/**
	 * 시험 Entity
	 */
	ExamEntity examEntity;

	/**
	 * 시험 로그 Entity
	 */
	ExamLogEntity examLogEntity;

	/**
	 * 시험 설정 Entity
	 */
	ExamEventSettingEntity examEventSettingEntity;

	/**
	 * 디데이
	 */
	String dDay;

	/**
	 * 디데이 (정렬의 기준)
	 */
	long realDay;

	/**
	 * 학생 수
	 */
	Integer countStd;

	/**
	 * 결과
	 */
	String result;

	/**
	 * 디데이를 기준으로 정렬
	 */
	@Override
	public int compareTo(ExamDto s) {
		if (this.realDay < s.getRealDay()) {
			return -1;
		} else if (this.realDay > s.getRealDay()) {
			return 1;
		}
		return 0;
	}
}
