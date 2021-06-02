package com.capstone.exam.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.exam.domain.entity.ExamEntity;


public interface ExamRepository extends JpaRepository<ExamEntity, Long> {
	
	// 과목에 대한 시험 모두 조회
	List<ExamEntity> findAllByClassCodeAndSemester(String classCode, String semester);
	
	// 시험 조회
	Optional<ExamEntity> findByClassCodeAndSemesterAndType(String classCode, String semester, String type);
	
	Optional<ExamEntity> findTop1ByClassCodeAndSemesterOrderByExamDateDesc(String classCode, String semester);
}
