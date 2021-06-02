package com.capstone.classes.classMgmt.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.capstone.classes.classMgmt.domain.dto.IClassDto;
import com.capstone.classes.classMgmt.domain.entity.ClassMemberEntity;


public interface ClassMemberRepository extends JpaRepository<ClassMemberEntity, Long> {

	//최근 관리자 담당 과목 조회
	Optional<ClassMemberEntity> findTop1ByMemberEmailOrderBySemesterDesc(String memberEmail);
	
	// 이미 수강 등록되었는지 확인
	Optional<ClassMemberEntity> findByMemberEmailAndClassCodeAndSemester(String memberEmail, String classCode, String semester);
	
	// 관리자의 모든 과목
	List<ClassMemberEntity> findAllByMemberEmailAndTypeOrderBySemesterDesc(String memberEmail, String type);
	
	// 해당 과목 모든 학생
	List<ClassMemberEntity> findAllByClassCodeAndSemester(String classCode, String semester);
	
	// 학생 수
	Integer countByClassCodeAndSemesterAndType(String classCode, String semester, String type);
	
	// 이벤트 발생 학생
	@Query(value = 
			"select m.number " + 
			"  from member m " + 
			"      ,class_member cm " + 
			" where m.email = cm.member_email " + 
			"   and cm.class_code = ?1 " + 
			"   and cm.semester = ?2 " + 
			"   and cm.`type` = 'STUDENT' " + 
			" order by m.number" +
			"  ", nativeQuery = true)
	List<String> getStudentList(String classCode, String semester);
	
	// 수강 구성원 리스트
	@Query(value = 
			"select m.name as name " + 
			"	   ,m.number as studentId " + 
			"	   ,d.name_ko as deptNameKo " +
			"	   ,d.name_en as deptNameEn " +
			"	   ,d.code as deptCode " + 
			"	   ,cm.id as cmId " + 
			"	   ,cm.`type` as `type` " + 
			"  from class_member cm " + 
			"      ,member m " + 
			"      ,department d " + 
			" where cm.class_code = ?1 " + 
			"   and cm.semester = ?2 " + 
			"   and cm.member_email = m.email " + 
			"   and m.dept_code = d.code" + 
			" order by cm.`type`, m.number desc" +
			"  ", nativeQuery = true)
	List<IClassDto> getClassMemberList(String classCode, String semester);	
	
	// 해당 이메일 전체 삭제
	@Query(value = 
			"delete from class_member " +
			" where member_email in ?1" +
			"  ", nativeQuery = true)
	void deleteAllByEmailIn(String[] email);
	
}
