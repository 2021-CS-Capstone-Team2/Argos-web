package com.capstone.exam.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.capstone.exam.domain.dto.IExamDto;
import com.capstone.exam.domain.entity.ExamLogEntity;


public interface ExamLogRepository extends JpaRepository<ExamLogEntity, Long> {
	
	@Query(value = 
			"select m.name as memberName " + 
			"		,el.create_date as createDate " + 
			"		,m.number as number " + 
			"	    ,c.name_ko as classNameKo " +
			"	    ,c.name_en as classNameEn " + 
			"		,c.code as classCode " + 
			"		,c.semester as semester " + 
			"		,e.`type` as examType " + 
			"		,el.member_ip as memberIp " + 
			"		,el.`status` as `status` " + 
			"  from exam_log el " + 
			"  		,exam e " + 
			"  		,member m " + 
			"  		,class_member cm " + 
			"  		,class c " + 
			" where el.exam_code IN ?1 " + 
			"   and el.exam_code = e.code " + 
			" 	and el.member_email = m.email " + 
			" 	and m.email = cm.member_email " + 
			" 	and cm.`type` = ?2 " + 
			"   and e.class_code = c.code " + 
			"   and e.semester = c.semester " + 
			" 	and cm.class_code = c.code " + 
			" 	and cm.semester = c.semester " + 
			"	order by el.create_date desc " +
			"  ", nativeQuery = true)
	List<IExamDto> getExamLog(List<String> examCode, String type);

	// 시험종료 학생 수
	Integer countByExamCodeAndStatus(String examCode, String status);
}
