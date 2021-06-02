package com.capstone.common.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.common.domain.entity.DepartmentEntity;


public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {
	
	Optional<DepartmentEntity> findByCode(String code);

}
