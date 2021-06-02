package com.capstone.event.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.capstone.event.domain.dto.IDeviceEventDto;
import com.capstone.event.domain.entity.DeviceEventEntity;


public interface DeviceEventRepository extends JpaRepository<DeviceEventEntity, Long> {
	
	// 과목에 따른 전체 디바이스 이벤트
	@Query(value = 
			"select m.name as name " + 
			"		,m.number as number " + 
			"		,de.create_date as createDate " + 
			"		,c.name_ko as classNameKo " +
			"		,c.name_en as classNameEn " +
			"		,c.code as classCode " + 
			"		,de.`type` as type " + 
			"		,de.`status` as status " + 
			"		,de.message as message " + 
			"		,c.semester as semester " + 
			"  from exam e " + 
			"		,device_event de " + 
			"		,member m " +
			"       ,class c " + 
			" where e.code in ?1 " +
			"   and e.code = de.exam_code " + 
			"   and e.semester = c.semester " + 
			"   and e.class_code = c.code " + 
			"   and de.create_member_id = m.id " + 
			" order by de.create_date desc " +
			"  ", nativeQuery = true)
	List<IDeviceEventDto> getDeviceEventList(List<String> examList);

	// 전체 디바이스 이벤트 발생 과목들
	@Query(value = 
			" select distinct" +
			"	   c.name_ko as classNameKo " +
			"	   ,c.name_en as classNameEn " +
			"      ,c.code as classCode" + 
			"      ,c.semester as semester" + 
			"      ,e.type as examType " + 
			"  from exam e " + 
			"  		,device_event de " + 
			"  		,class c " + 
			" where e.code in ?1 " + 
			"   and e.code = de.exam_code " + 
			"   and e.class_code = c.code " + 
			"   and e.semester = c.semester " + 
			" order by c.semester desc" + 
			"  ", nativeQuery = true)
	List<IDeviceEventDto> getDeviceEventClass(List<String> examList);
	
	// 해당 시험에서의 디바이스 이벤트 수
	Integer countByExamCode(String examCode);
}
