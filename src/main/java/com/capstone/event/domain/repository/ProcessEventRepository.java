package com.capstone.event.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.capstone.event.domain.dto.IProcessEventDto;
import com.capstone.event.domain.entity.ProcessEventEntity;


public interface ProcessEventRepository extends JpaRepository<ProcessEventEntity, Long> {
	
	// 과목에 따른 프로세스 이벤트
	@Query(value = 
			"select m.name as name " + 
			"		,m.number as number " + 
			"      ,pe.create_date as createDate " + 
			"	   ,c.name_ko as classNameKo " +
			"	   ,c.name_en as classNameEn " +
			"      ,c.code as classCode " + 
			"      ,pe.detected_process as detectedProcess " + 
			"      ,pe.event_process as eventProcess " + 
			"      ,pe.main_title as mainTitle " + 
			"      ,c.semester as semester " + 
			"  from exam e " + 
			"		,process_event pe " + 
			"		,member m " +
			"		,class c " + 
			" where e.code in ?1 " +
			"   and e.code = pe.exam_code " + 
			"   and pe.create_member_id = m.id " +
			"   and e.semester = c.semester " + 
			"   and e.class_code = c.code " + 
			" order by pe.create_date desc" +
			"  ", nativeQuery = true)
	List<IProcessEventDto> getProcessEventList(List<String> examList);

	// 전체 프로세스 이벤트 발생 과목들
	@Query(value = 
			"select distinct" +
			"	    c.name_ko as classNameKo " +
			"	    ,c.name_en as classNameEn " +
			"		,c.code as classCode " + 
			"		,c.semester as semester " + 
			"       ,e.type as examType " + 
			"  from exam e " + 
			"  		,process_event pe " + 
			"  		,class c " + 
			" where e.code in ?1 " + 
			"   and e.code = pe.exam_code " + 
			"   and e.class_code = c.code " + 
			"   and e.semester = c.semester " + 
			" order by c.semester desc" + 
			"  ", nativeQuery = true)
	List<IProcessEventDto> getProcessEventClass(List<String> examList);
	
	// 해당 시험에서의 프로세스 이벤트 수
	Integer countByExamCode(String examCode);
	
}
