package com.capstone.classes.classMgmt.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.classes.classMgmt.domain.entity.ClassEntity;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {

	// 코드와 학기로 과목 조회 (Unique)
	Optional<ClassEntity> findByCodeAndSemester(String code, String semester);

}
