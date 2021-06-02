package com.capstone.dashboard.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.capstone.dashboard.dto.IDashboardDto;
import com.capstone.event.domain.entity.ProcessEventEntity;


public interface DashboardRepository extends JpaRepository<ProcessEventEntity, Long> {
	@Query(value = 
			" select m.name as name     " + 
			"       ,m.number as number     " + 
			" 		 ,(select count(de.id) from device_event de where de.create_member_id=m.id and de.exam_code=e.code) as deviceEvent      " + 
			" 		 ,(select count(de.id) from device_event de where de.create_member_id=m.id and de.exam_code=e.code and (de.`status` LIKE 'DAN%' or de.`type` = 'USB')) as deviceCheating      " + 
			" 		 ,(select count(de.id) from device_event de where de.create_member_id=m.id and de.exam_code=e.code and (de.`status` LIKE 'WAR%' or de.`type` in ('EYETRACKING', 'AUDIO'))) as deviceWarning      " + 
			" 		 ,(select count(pe.id) from process_event pe where pe.create_member_id=m.id and pe.exam_code=e.code) as processEvent      " + 
			"   from exam e " + 
			"		,class_member cm " +
			"		,member m " +
			"  where e.code = ?1      " + 
			"    and cm.class_code = e.class_code  " + 
			"    and cm.semester = e.semester      " + 
			"    and cm.`type` = 'STUDENT'      " + 
			"    and m.email = cm.member_email      " + 
			"  order by m.name    " +
			"  ", nativeQuery = true)
	List<IDashboardDto> getStudentEventList(String examCode);
	
	@Query(value = 
			"(select '프로세스 이벤트' as `eventKo`    " + 
			"		,'Process Event' as `eventEn`    " + 
			"		,pe.detected_process as `type`    " + 
			"		,pe.main_title as message    " + 
			"		,m.name as name    " + 
			"		,pe.create_date as createDate    " + 
			"   from exam e    " +
			"      ,member m    " + 
			"      ,process_event pe    " + 
			" where e.code = ?1   " + 
			"   and e.code = pe.exam_code    " + 
			"   and m.id = pe.create_member_id)    " + 
			"union    " + 
			"(select '디바이스 이벤트' as `eventKo`    " + 
			"		,'Device Event' as `eventEn`    " + 
			"		 ,de.`type` as `type`    " + 
			"		 ,de.message as `message`    " + 
			"		 ,m.name as name    " + 
			"		 ,de.create_date as createDate    " + 
			"   from exam e    " +
			"      ,member m    " + 
			"      ,device_event de    " + 
			" where e.code = ?1   " + 
			"   and e.code = de.exam_code    " + 
			"   and m.id = de.create_member_id )    " + 
			"order by createDate desc, message desc    " + 
			"limit 10   " +
			"  ", nativeQuery = true)
	List<IDashboardDto> getTimeLine(String examCode);	
	
	@Query(value = 
			 " select ifnull(A.eventTime, B.eventTime) as eventTime  "  + 
			 " 		,ifnull(A.cnt, 0) as deviceEvent  "  + 
			 " 		,ifnull(B.cntProc, 0) as processEvent  "  + 
			 " from (  "  + 
			 " select  "  + 
			 " 		case when substr(de.create_date,15,2)<'10' then concat(substr(de.create_date,12,2),':00')  "  + 
			 "     		  when substr(de.create_date,15,2) >='10' and substr(de.create_date,15,2)<'20' then concat(substr(de.create_date,12,2),':10')  "  + 
			 "        	  when substr(de.create_date,15,2) >='20' and substr(de.create_date,15,2)<'30' then concat(substr(de.create_date,12,2),':20')  "  + 
			 "        	  when substr(de.create_date,15,2) >='30' and substr(de.create_date,15,2)<'40' then concat(substr(de.create_date,12,2),':30')  "  + 
			 "        	  when substr(de.create_date,15,2) >='40' and substr(de.create_date,15,2)<'50' then concat(substr(de.create_date,12,2),':40')  "  + 
			 "        	  when substr(de.create_date,15,2) >='50' and substr(de.create_date,15,2)<'60' then concat(substr(de.create_date,12,2),':50')  "  + 
			 "      	END as eventTime,  "  + 
			 "      count(distinct de.id) as cnt  "  + 
			 " from device_event de  "  + 
			 " where de.exam_code = ?1 AND de.create_date > ?2  "  + 
			 " group by substr(de.create_date,1,10) ,  "  + 
			 "     case when substr(de.create_date,15,2)<'10' then concat(substr(de.create_date,12,2),':00')  "  + 
			 "     	 when substr(de.create_date,15,2) >='10' and substr(de.create_date,15,2)<'20' then concat(substr(de.create_date,12,2),':10')  "  + 
			 "          when substr(de.create_date,15,2) >='20' and substr(de.create_date,15,2)<'30' then concat(substr(de.create_date,12,2),':20')  "  + 
			 "          when substr(de.create_date,15,2) >='30' and substr(de.create_date,15,2)<'40' then concat(substr(de.create_date,12,2),':30')  "  + 
			 "          when substr(de.create_date,15,2) >='40' and substr(de.create_date,15,2)<'50' then concat(substr(de.create_date,12,2),':40')  "  + 
			 "          when substr(de.create_date,15,2) >='50' and substr(de.create_date,15,2)<'60' then concat(substr(de.create_date,12,2),':50')  "  + 
			 "      END  "  + 
			 " order by eventTime  "  + 
			 " ) A  "  + 
			 " left join  "  + 
			 " (  "  + 
			 " select  "  + 
			 " 		case when substr(pe.create_date,15,2)<'10' then concat(substr(pe.create_date,12,2),':00')  "  + 
			 "     		  when substr(pe.create_date,15,2) >='10' and substr(pe.create_date,15,2)<'20' then concat(substr(pe.create_date,12,2),':10')  "  + 
			 "        	  when substr(pe.create_date,15,2) >='20' and substr(pe.create_date,15,2)<'30' then concat(substr(pe.create_date,12,2),':20')  "  + 
			 "        	  when substr(pe.create_date,15,2) >='30' and substr(pe.create_date,15,2)<'40' then concat(substr(pe.create_date,12,2),':30')  "  + 
			 "        	  when substr(pe.create_date,15,2) >='40' and substr(pe.create_date,15,2)<'50' then concat(substr(pe.create_date,12,2),':40')  "  + 
			 "        	  when substr(pe.create_date,15,2) >='50' and substr(pe.create_date,15,2)<'60' then concat(substr(pe.create_date,12,2),':50')  "  + 
			 "      	END as eventTime,  "  + 
			 "      count(distinct pe.id) as cntProc  "  + 
			 " from process_event pe  "  + 
			 " where pe.exam_code = ?1 AND pe.create_date > ?2  "  + 
			 " group by substr(pe.create_date,1,10) ,  "  + 
			 "     case when substr(pe.create_date,15,2)<'10' then concat(substr(pe.create_date,12,2),':00')  "  + 
			 "     	 when substr(pe.create_date,15,2) >='10' and substr(pe.create_date,15,2)<'20' then concat(substr(pe.create_date,12,2),':10')  "  + 
			 "          when substr(pe.create_date,15,2) >='20' and substr(pe.create_date,15,2)<'30' then concat(substr(pe.create_date,12,2),':20')  "  + 
			 "          when substr(pe.create_date,15,2) >='30' and substr(pe.create_date,15,2)<'40' then concat(substr(pe.create_date,12,2),':30')  "  + 
			 "          when substr(pe.create_date,15,2) >='40' and substr(pe.create_date,15,2)<'50' then concat(substr(pe.create_date,12,2),':40')  "  + 
			 "          when substr(pe.create_date,15,2) >='50' and substr(pe.create_date,15,2)<'60' then concat(substr(pe.create_date,12,2),':50')  "  + 
			 "      END  "  + 
			 " order by eventTime  "  + 
			 " ) B  "  + 
			 " ON A.eventTime = B.eventTime  "  + 
			 " union  "  + 
			 " select ifnull(A.eventTime, B.eventTime) as eventTime  "  + 
			 " 		,ifnull(A.cnt, 0) as deviceEvent  "  + 
			 " 		,ifnull(B.cntProc, 0) as processEvent  "  + 
			 " from (  "  + 
			 " select  "  + 
			 " 		case when substr(de.create_date,15,2)<'10' then concat(substr(de.create_date,12,2),':00')  "  + 
			 "     		  when substr(de.create_date,15,2) >='10' and substr(de.create_date,15,2)<'20' then concat(substr(de.create_date,12,2),':10')  "  + 
			 "        	  when substr(de.create_date,15,2) >='20' and substr(de.create_date,15,2)<'30' then concat(substr(de.create_date,12,2),':20')  "  + 
			 "        	  when substr(de.create_date,15,2) >='30' and substr(de.create_date,15,2)<'40' then concat(substr(de.create_date,12,2),':30')  "  + 
			 "        	  when substr(de.create_date,15,2) >='40' and substr(de.create_date,15,2)<'50' then concat(substr(de.create_date,12,2),':40')  "  + 
			 "        	  when substr(de.create_date,15,2) >='50' and substr(de.create_date,15,2)<'60' then concat(substr(de.create_date,12,2),':50')  "  + 
			 "      	END as eventTime,  "  + 
			 "      count(distinct de.id) as cnt  "  + 
			 " from device_event de  "  + 
			 " where de.exam_code = ?1 AND de.create_date > ?2  "  + 
			 " group by substr(de.create_date,1,10) ,  "  + 
			 "     case when substr(de.create_date,15,2)<'10' then concat(substr(de.create_date,12,2),':00')  "  + 
			 "     	 when substr(de.create_date,15,2) >='10' and substr(de.create_date,15,2)<'20' then concat(substr(de.create_date,12,2),':10')  "  + 
			 "          when substr(de.create_date,15,2) >='20' and substr(de.create_date,15,2)<'30' then concat(substr(de.create_date,12,2),':20')  "  + 
			 "          when substr(de.create_date,15,2) >='30' and substr(de.create_date,15,2)<'40' then concat(substr(de.create_date,12,2),':30')  "  + 
			 "          when substr(de.create_date,15,2) >='40' and substr(de.create_date,15,2)<'50' then concat(substr(de.create_date,12,2),':40')  "  + 
			 "          when substr(de.create_date,15,2) >='50' and substr(de.create_date,15,2)<'60' then concat(substr(de.create_date,12,2),':50')  "  + 
			 "      END  "  + 
			 " order by eventTime  "  + 
			 " ) A  "  + 
			 " right join  "  + 
			 " (  "  + 
			 " select  "  + 
			 " 		case when substr(pe.create_date,15,2)<'10' then concat(substr(pe.create_date,12,2),':00')  "  + 
			 "     		  when substr(pe.create_date,15,2) >='10' and substr(pe.create_date,15,2)<'20' then concat(substr(pe.create_date,12,2),':10')  "  + 
			 "        	  when substr(pe.create_date,15,2) >='20' and substr(pe.create_date,15,2)<'30' then concat(substr(pe.create_date,12,2),':20')  "  + 
			 "        	  when substr(pe.create_date,15,2) >='30' and substr(pe.create_date,15,2)<'40' then concat(substr(pe.create_date,12,2),':30')  "  + 
			 "        	  when substr(pe.create_date,15,2) >='40' and substr(pe.create_date,15,2)<'50' then concat(substr(pe.create_date,12,2),':40')  "  + 
			 "        	  when substr(pe.create_date,15,2) >='50' and substr(pe.create_date,15,2)<'60' then concat(substr(pe.create_date,12,2),':50')  "  + 
			 "      	END as eventTime,  "  + 
			 "      count(distinct pe.id) as cntProc  "  + 
			 " from process_event pe  "  + 
			 " where pe.exam_code = ?1 AND pe.create_date > ?2  "  + 
			 " group by substr(pe.create_date,1,10) ,  "  + 
			 "     case when substr(pe.create_date,15,2)<'10' then concat(substr(pe.create_date,12,2),':00')  "  + 
			 "     	 when substr(pe.create_date,15,2) >='10' and substr(pe.create_date,15,2)<'20' then concat(substr(pe.create_date,12,2),':10')  "  + 
			 "          when substr(pe.create_date,15,2) >='20' and substr(pe.create_date,15,2)<'30' then concat(substr(pe.create_date,12,2),':20')  "  + 
			 "          when substr(pe.create_date,15,2) >='30' and substr(pe.create_date,15,2)<'40' then concat(substr(pe.create_date,12,2),':30')  "  + 
			 "          when substr(pe.create_date,15,2) >='40' and substr(pe.create_date,15,2)<'50' then concat(substr(pe.create_date,12,2),':40')  "  + 
			 "          when substr(pe.create_date,15,2) >='50' and substr(pe.create_date,15,2)<'60' then concat(substr(pe.create_date,12,2),':50')  "  + 
			 "      END  "  + 
			 " order by eventTime  "  + 
			 " ) B  "  + 
			 " ON A.eventTime = B.eventTime "  + 
			"  ", nativeQuery = true)
	List<IDashboardDto> getEventTime(String examCode, LocalDateTime examDate);	

}
