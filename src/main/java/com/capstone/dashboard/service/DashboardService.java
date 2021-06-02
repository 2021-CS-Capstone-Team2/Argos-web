package com.capstone.dashboard.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.capstone.classes.classMgmt.domain.entity.ClassEntity;
import com.capstone.classes.classMgmt.domain.entity.ClassMemberEntity;
import com.capstone.classes.classMgmt.domain.repository.ClassMemberRepository;
import com.capstone.classes.classMgmt.domain.repository.ClassRepository;
import com.capstone.dashboard.dto.DashboardDto;
import com.capstone.dashboard.dto.IDashboardDto;
import com.capstone.dashboard.repository.DashboardRepository;
import com.capstone.event.domain.repository.DeviceEventRepository;
import com.capstone.event.domain.repository.ProcessEventRepository;
import com.capstone.exam.constant.ExamConstant;
import com.capstone.exam.domain.entity.ExamEntity;
import com.capstone.exam.domain.entity.ExamEventSettingEntity;
import com.capstone.exam.domain.repository.ExamEventSettingRepository;
import com.capstone.exam.domain.repository.ExamLogRepository;
import com.capstone.exam.domain.repository.ExamRepository;

import lombok.AllArgsConstructor;

/**
 * 대시보드 관리 서비스
 * 
 * @since 2021.04.09
 * @author swkim
 * 
 */
@AllArgsConstructor
@Service
@Transactional
public class DashboardService {

	private ClassRepository classRepository;
	private ClassMemberRepository classMemberRepository;
	private DeviceEventRepository deviceEventRepository;
	private ProcessEventRepository processEventRepository;
	private DashboardRepository dashboardRepository;
	private ExamRepository examRepository;
	private ExamLogRepository examLogRepository;
	private ExamEventSettingRepository examEventSettingRepository;

	/**
	 * 선택한 과목
	 * 
	 * @param cMEntity
	 * @return
	 */
	public DashboardDto getSelectClass(ExamEntity examEntity) {
		DashboardDto dto = new DashboardDto();
		Optional<ClassEntity> oC;
		Optional<ExamEntity> oE;
		Optional<ClassMemberEntity> oMC;

		String classCode = null;
		String semester = null;
		if (examEntity.getId() == null) {
			// 관리자의 최근 관리 과목 리스트 가져오기
			oMC = classMemberRepository.findTop1ByMemberEmailOrderBySemesterDesc(SecurityContextHolder.getContext().getAuthentication().getName());
			if (oMC.isPresent()) {
				classCode = oMC.get().getClassCode();
				semester = oMC.get().getSemester();
				// 과목 선택
				oC = classRepository.findByCodeAndSemester(classCode, semester);
				dto.setClassEntity(oC.get());
				// 시험 선택
				oE = examRepository.findTop1ByClassCodeAndSemesterOrderByExamDateDesc(classCode, semester);
				if(oE.isEmpty()) {
					return null;
				}
				dto.setExamEntity(oE.get());

				// 첫번째 row
				// 설정한 이벤트 객체
				Optional<ExamEventSettingEntity> oEES = examEventSettingRepository.findByExamId(oE.get().getId());
				if(oEES.isEmpty()) {
					return null;
				}
				dto.setExamEventSettingEntity(oEES.get());
				// 발생 이벤트 수
				int eventCount = getEventCount(oE.get().getCode());
				dto.setEventCount(eventCount);
				// 학생 수
				dto.setStudentCount(getStudentCount(oE.get().getCode()));
				// 시험 종료 학생 수 
				dto.setFinStudentCount(getFinishedStudentCount(oE.get().getCode()));
				
				// 두번째 row
				// 학생 이벤트 현황
				dto.setIDto(dashboardRepository.getStudentEventList(oE.get().getCode()));

			} else {
				// 나중에 처리
				return null;
			}
		} else {
			// 선택된 과목
			ExamEntity selectExam = examRepository.findById(examEntity.getId()).get();
			oC = classRepository.findByCodeAndSemester(selectExam.getClassCode(), selectExam.getSemester());
			if (oC.isPresent()) {
				classCode = oC.get().getCode();
				semester = oC.get().getSemester();
				// 과목 선택
				dto.setClassEntity(oC.get());

				// 시험 선택
				dto.setExamEntity(selectExam);

				// 첫번째 row
				// 설정한 이벤트 객체
				Optional<ExamEventSettingEntity> oEES = examEventSettingRepository.findByExamId(selectExam.getId());
				if(oEES.isEmpty()) {
					return null;
				}
				dto.setExamEventSettingEntity(oEES.get());
				// 발생 이벤트 수
				int eventCount = getEventCount(selectExam.getCode());
				dto.setEventCount(eventCount);
				// 시험 시작 학생 수
				dto.setStudentCount(getStudentCount(selectExam.getCode()));
				// 시험 종료 학생 수 
				dto.setFinStudentCount(getFinishedStudentCount(selectExam.getCode()));
				
				// 두번째 row
				// 학생 이벤트 현황
				dto.setIDto(dashboardRepository.getStudentEventList(selectExam.getCode()));
			} else {
				// 나중에 처리
				return null;
			}
		}
		if (oC.isEmpty()) {
			// 나중에 처리
			return null;
		} else {
			return dto;
		}
	};

	/**
	 * 발생 이벤트 수
	 * 
	 * @param examCode
	 * @return
	 */
	public Integer getEventCount(String examCode) {
		return processEventRepository.countByExamCode(examCode) + deviceEventRepository.countByExamCode(examCode);
	}
	
	/**
	 * 실시간 이벤트 가져오기
	 * 발생 이벤트 수, 종료 학생 수, 	부정행위 판독, 학생 이벤트 현황
	 * 
	 * @param examCode
	 * @return
	 */
	public DashboardDto getRealTimeEvent(ExamEntity eEntity) {
		DashboardDto resultDto = new DashboardDto();
		ExamEntity examEntity = examRepository.findById(eEntity.getId()).get();
		
		// 발생 이벤트 수
		Integer allEventCount = getEventCount(examEntity.getCode());
		resultDto.setEventCount(allEventCount);
		
		// 전체 학생 수
		Integer studentCount = getStudentCount(examEntity.getCode());
		resultDto.setStudentCount(studentCount);
		
		// 종료 학생 수
		Integer finStudentCount = getFinishedStudentCount(examEntity.getCode());
		resultDto.setFinStudentCount(finStudentCount);
		
		// 학생 이벤트 현황
		resultDto.setIDto(dashboardRepository.getStudentEventList(examEntity.getCode()));
		resultDto.setResult(ExamConstant.EXAM_PROCESS_RESULT.SUCCESS.toString());
		
		return resultDto;
	}

	/**
	 * 전체 학생 수
	 * 
	 * @param examCode
	 * @return
	 */
	public Integer getStudentCount(String examCode) {
		Integer studentCount = examLogRepository.countByExamCodeAndStatus(examCode, ExamConstant.EXAM_LOG_STATUS.PROCEED.toString());

		return studentCount;
	}
	
	/**
	 * 시험 종료 학생 수
	 * 
	 * @param examCode
	 * @return
	 */
	public Integer getFinishedStudentCount(String examCode) {
		Integer finStudentCount = examLogRepository.countByExamCodeAndStatus(examCode, ExamConstant.EXAM_LOG_STATUS.FINISH.toString());

		return finStudentCount;
	}	

	/**
	 * 최근 기록
	 * 
	 * @param examEntity
	 * @return
	 */
	public List<IDashboardDto> getEventTimeLine(ExamEntity eEntity) {
		ExamEntity examEntity = examRepository.findById(eEntity.getId()).get();
		return dashboardRepository.getTimeLine(examEntity.getCode());
	}

	/**
	 * 디바이스 이벤트 타임라인
	 * 
	 * @param eEntity
	 * @return
	 */
	public DashboardDto getDeviceEventTime(ExamEntity eEntity) {
		DashboardDto resultDto = new DashboardDto();
		ExamEntity examEntity = examRepository.findById(eEntity.getId()).get();
		
		resultDto.setTimeLine(dashboardRepository.getEventTime(examEntity.getCode(), examEntity.getExamDate()));
		resultDto.setResult(ExamConstant.EXAM_PROCESS_RESULT.SUCCESS.toString());
		
		return resultDto;
	}
}
