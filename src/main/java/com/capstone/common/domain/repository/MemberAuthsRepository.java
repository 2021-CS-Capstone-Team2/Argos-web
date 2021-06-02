package com.capstone.common.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capstone.common.domain.entity.MemberAuthEntity;


public interface MemberAuthsRepository extends JpaRepository<MemberAuthEntity, Long> {
	
	List<MemberAuthEntity> findByMemberId(Long memberId);
	
	Optional<MemberAuthEntity> findByMemberIdAndAuthCode(Long memberId, String authCode);
	
	void deleteByMemberId(Long memberId);
	
}
