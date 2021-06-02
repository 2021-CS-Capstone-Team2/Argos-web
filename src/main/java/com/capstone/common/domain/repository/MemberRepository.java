package com.capstone.common.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.capstone.common.domain.entity.MemberEntity;
import com.capstone.member.dto.IMemberDto;


public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
	
	Optional<MemberEntity> findByEmail(String email);
	// 회원가입 시 중복된 이메일과 학번으로 가입 불가
	Optional<MemberEntity> findByEmailOrNumber(String email, String number);

	@Query(value = 
			"select m.name as name " + 
			"	   ,m.email as email " + 
			"	   ,m.number as studentId " +
			"	   ,m.create_date as createDate " + 
			"	   ,d.name_ko as deptNameKo " +
			"	   ,d.name_en as deptNameEn " +
			"	   ,d.code as deptCode " +
			"	   ,m.status as status " +
			"	   ,m.`type` as `type` " + 
			"  from member m " + 
			"      ,department d " + 
			" where m.dept_code = d.code" + 
			" order by d.code, m.number desc" +
			"  ", nativeQuery = true)
	List<IMemberDto> getMemberList();
	
	// 해당 이메일 사용자 삭제
	@Query(value = 
			"delete from member " +
			" where email in ?1" +
			"  ", nativeQuery = true)
	void deleteAllByEmailIn(String[] email);
	
	// 학생 리스트
	@Query(value = 
			"select m.name as name " + 
			"	   ,m.email as email " + 
			"	   ,m.number as studentId " + 
			"	   ,d.name_ko as deptNameKo " +
			"	   ,d.name_en as deptNameEn " +
			"	   ,d.code as deptCode " + 
			"	   ,m.`type` as `type` " + 
			"  from member m " + 
			"      ,department d " + 
			" where m.dept_code = d.code" + 
			" order by d.code, m.number desc" +
			"  ", nativeQuery = true)
	List<IMemberDto> getClassMemberList();	
}
