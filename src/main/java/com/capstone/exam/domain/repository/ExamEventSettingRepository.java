package com.capstone.exam.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.exam.domain.entity.ExamEventSettingEntity;


public interface ExamEventSettingRepository extends JpaRepository<ExamEventSettingEntity, Long> {
	
	// 시험 셋팅 조회
	Optional<ExamEventSettingEntity> findByExamId(Long examId);
	
}
