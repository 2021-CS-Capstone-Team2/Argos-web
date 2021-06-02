package com.capstone.exam.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Base64;

import javax.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.capstone.classes.classMgmt.constant.ClassConstant;
import com.capstone.classes.classMgmt.domain.entity.ClassEntity;
import com.capstone.classes.classMgmt.domain.entity.ClassMemberEntity;
import com.capstone.classes.classMgmt.domain.repository.ClassMemberRepository;
import com.capstone.classes.classMgmt.domain.repository.ClassRepository;
import com.capstone.common.domain.repository.MemberRepository;
import com.capstone.exam.constant.ExamConstant;
import com.capstone.exam.domain.dto.ExamDto;
import com.capstone.exam.domain.dto.IExamDto;
import com.capstone.exam.domain.entity.ExamEntity;
import com.capstone.exam.domain.entity.ExamEventSettingEntity;
import com.capstone.exam.domain.repository.ExamEventSettingRepository;
import com.capstone.exam.domain.repository.ExamLogRepository;
import com.capstone.exam.domain.repository.ExamRepository;

import lombok.AllArgsConstructor;

/**
 * 시험 관리 서비스 - 자신이 MANAGER로 있는 과목만 관리 가능
 * 
 * @author swkim
 *
 */
@AllArgsConstructor
@Service
@Transactional
public class ExamService {

	/**
	 * Repository
	 */
	private MemberRepository memberRepository;
	private ClassRepository classRepository;
	private ClassMemberRepository classMemberRepository;
	private ExamRepository examRepository;
	private ExamLogRepository examLogRepository;
	private ExamEventSettingRepository examEventSettingRepository;

	/**
	 * 나의 시험 관리 리스트 조회
	 * 
	 * @return
	 */
	public ArrayList<ExamDto> getMyExamList() {
		// 로그인 유저 이메일
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginEmail = auth.getName();

		// DTO ArrayList 생성
		ArrayList<ExamDto> resultDtoList = new ArrayList<ExamDto>();

		// 나의 관리 과목 가져옴
		List<ClassMemberEntity> classMember = classMemberRepository.findAllByMemberEmailAndTypeOrderBySemesterDesc(loginEmail, ClassConstant.CLASS_MEMBER_TYPE.MANAGER.toString());

		// 과목 리스트 DTO 등록
		// 시험 과목 DTO 등록
		for (ClassMemberEntity cM : classMember) {
			Optional<ClassEntity> oC = classRepository.findByCodeAndSemester(cM.getClassCode(), cM.getSemester());
			Integer countStd = classMemberRepository.countByClassCodeAndSemesterAndType(cM.getClassCode(), cM.getSemester(), ClassConstant.CLASS_MEMBER_TYPE.STUDENT.toString());
			List<ExamEntity> oE = examRepository.findAllByClassCodeAndSemester(cM.getClassCode(), cM.getSemester());

			if (oC.isPresent()) {
				for (ExamEntity examEntity : oE) {
					if (examEntity != null) {
						// DTO 생성
						ExamDto examDto = new ExamDto();

						// 시험 설정 가져오기
						Optional<ExamEventSettingEntity> eesEntity = examEventSettingRepository.findByExamId(examEntity.getId());

						examDto.setClassEntity(oC.get());
						examDto.setExamEntity(examEntity);
						examDto.setExamEventSettingEntity(eesEntity.get());

						// D-day 설정
						LocalDate fromDate = LocalDate.now();
						LocalDate toDate = LocalDate.from(examEntity.getExamDate());

						long realDay = 0;
						String dDay = null;

						if (fromDate.isEqual(toDate)) {
							dDay = "D-day";
						} else if (fromDate.isBefore(toDate)) {
							realDay = ChronoUnit.DAYS.between(toDate, fromDate);
							dDay = "D" + Long.toString(realDay);
						} else {
							realDay = ChronoUnit.DAYS.between(toDate, fromDate);
							dDay = "D+" + Long.toString(realDay);
						}

						examDto.setCountStd(countStd);
						examDto.setRealDay(realDay);
						examDto.setDDay(dDay);

						resultDtoList.add(examDto);
					}
				}
			} else {
				// 과목이 없는 경우
				return null;
			}
		}
		// D-day에 따라 시험 정렬
		Collections.sort(resultDtoList);

		return resultDtoList;
	}
	
	/**
	 * 나의 시험 코드 리스트 가져오기
	 * 
	 * @param loginEmail
	 * @return
	 */
	public List<String> getMyExamCodeList(String loginEmail) {
		
		// 나의 과목 목록 가져옴
		List<ClassMemberEntity> classMember = classMemberRepository.findAllByMemberEmailAndTypeOrderBySemesterDesc(loginEmail, ClassConstant.CLASS_MEMBER_TYPE.MANAGER.toString());
		List<String> examList = new ArrayList<String>();

		for (ClassMemberEntity cM : classMember) {
			Optional<ClassEntity> oC = classRepository.findByCodeAndSemester(cM.getClassCode(), cM.getSemester());
			List<ExamEntity> oE = examRepository.findAllByClassCodeAndSemester(cM.getClassCode(), cM.getSemester());

			if (oC.isPresent()) {
				for (ExamEntity examEntity : oE) {
					if (examEntity != null) {
						examList.add(examEntity.getCode());
					}
				}
			} else {
				// 과목이 없는 경우
				return null;
			}
		}
		
		return examList;
	}
	

	/**
	 * 나의 시험 로그 리스트 조회
	 * 
	 * @return
	 */
	public List<IExamDto> getMyExamLog() {
		// 로그인 유저 이메일
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginEmail = auth.getName();

		// 나의 시험코드 리스트 가져오기
		List<String> examList = getMyExamCodeList(loginEmail);
		
		return examLogRepository.getExamLog(examList, ClassConstant.CLASS_MEMBER_TYPE.STUDENT.toString());
	}

	/**
	 * 새 시험 등록
	 *
	 * @param examEntity
	 * @return
	 * @throws IOException 
	 */
	public String createExam(ExamEntity examEntity, Long classId) throws IOException {
		// 로그인 유저 이메일
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginEmail = auth.getName();
		// 로그인 풀린 상태
		if (loginEmail.equals("anonymousUser")) {
			return ExamConstant.EXAM_PROCESS_RESULT.DIFFERENT.toString();
		}
		String userId = memberRepository.findByEmail(loginEmail).get().getId().toString();
		// 과목이 존재하는지 체크
		Optional<ClassEntity> oC = classRepository.findById(classId);
		if (oC.isEmpty()) {
			// 과목이 존재하지 않음.
			return ExamConstant.EXAM_PROCESS_RESULT.EMPTY.toString();
		}
		
		ClassEntity classEntity = oC.get();

		ExamEntity eEntity = new ExamEntity();

		// 시험이 존재하는지 체크
		Optional<ExamEntity> oE = examRepository.findByClassCodeAndSemesterAndType(classEntity.getCode(), classEntity.getSemester(), examEntity.getType());

		// 존재하지 않다면 생성
		if (oE.isEmpty()) {
			// 시험 생성
			eEntity.setCreateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));
			eEntity.setCreateMemberId(memberRepository.findByEmail(loginEmail).get().getId().toString());
			eEntity.setClassCode(classEntity.getCode());
			eEntity.setSemester(classEntity.getSemester());
			eEntity.setExamDate(examEntity.getExamDate());
			eEntity.setType(examEntity.getType());
			String code = base64Encode(classEntity.getCode()+classEntity.getSemester()+examEntity.getType());
			eEntity.setCode(code);
			
			examRepository.save(eEntity);
			
			if(ExamConstant.EXAM_TYPE.MIDTERM.toString().equals(examEntity.getType())) {
				classEntity.setMidtermExam(examEntity.getExamDate());
				classEntity.setUpdateMemberId(userId);
				classEntity.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));
				
				classRepository.save(classEntity);
				
			} else if(ExamConstant.EXAM_TYPE.FINAL.toString().equals(examEntity.getType())) {
				classEntity.setFinalExam(examEntity.getExamDate());
				
				classEntity.setUpdateMemberId(userId);
				classEntity.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));
				
				classRepository.save(classEntity);
			}

			// 시험 셋팅 생성
			ExamEventSettingEntity eesEntity = new ExamEventSettingEntity();

			eesEntity.setCreateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));
			eesEntity.setCreateMemberId(memberRepository.findByEmail(loginEmail).get().getId().toString());
			eesEntity.setKeyboardEvent(ExamConstant.EXAM_EVENT_SETTING.N.toString());
			eesEntity.setMouseEvent(ExamConstant.EXAM_EVENT_SETTING.N.toString());
			eesEntity.setAudioEvent(ExamConstant.EXAM_EVENT_SETTING.N.toString());
			eesEntity.setEyeTrackingEvent(ExamConstant.EXAM_EVENT_SETTING.N.toString());
			eesEntity.setProcessEvent(ExamConstant.EXAM_EVENT_SETTING.N.toString());			
			eesEntity.setExam(eEntity);

			examEventSettingRepository.save(eesEntity);

		} else {
			// 이미 존재하는 시험
			return ExamConstant.EXAM_PROCESS_RESULT.EXISTS.toString();
		}

		return ExamConstant.EXAM_PROCESS_RESULT.SUCCESS.toString();
	}

	/**
	 * 시험 삭제
	 * 
	 * @param examId
	 * @return
	 */
	public String deleteExam(Long examId) {
		// 로그인 유저 이메일
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginEmail = auth.getName();
		// 로그인 풀린 상태
		if (loginEmail.equals("anonymousUser")) {
			return ExamConstant.EXAM_PROCESS_RESULT.DIFFERENT.toString();
		}

		// 시험 설정이 존재하는지 체크
		Optional<ExamEventSettingEntity> oEES = examEventSettingRepository.findByExamId(examId);

		if (oEES.isPresent()) {
			// 시험 설정부터 삭제
			examEventSettingRepository.delete(oEES.get());
		}

		// 시험 조회
		Optional<ExamEntity> oE = examRepository.findById(examId);

		if (oE.isEmpty()) {
			// 이미 삭제된 시험
			return ExamConstant.EXAM_PROCESS_RESULT.EMPTY.toString();
		} else {
			// 시험 삭제
			examRepository.delete(oE.get());
		}

		return ExamConstant.EXAM_PROCESS_RESULT.SUCCESS.toString();
	}

	/**
	 * 시험 수정
	 * 
	 * @param examEntity
	 * @return
	 */
	public String editExam(ExamEntity examEntity) {
		// 로그인 유저 이메일
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginEmail = auth.getName();
		// 로그인 풀린 상태
		if (loginEmail.equals("anonymousUser")) {
			return ExamConstant.EXAM_PROCESS_RESULT.DIFFERENT.toString();
		}
		String userId = memberRepository.findByEmail(loginEmail).get().getId().toString();

		// 시험이 존재하는지 체크
		Optional<ExamEntity> oEE = examRepository.findById(examEntity.getId());

		if (oEE.isEmpty()) {
			// 존재하지 않습니다.
			return ExamConstant.EXAM_PROCESS_RESULT.EMPTY.toString();
		} else {
			// 존재하는 시험
			ExamEntity setEntity = oEE.get();

			setEntity.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));
			setEntity.setUpdateMemberId(userId);
			setEntity.setExamDate(examEntity.getExamDate());

			examRepository.save(setEntity);
			
			ClassEntity classEntity = classRepository.findByCodeAndSemester(setEntity.getClassCode(), setEntity.getSemester()).get();
			
			if(ExamConstant.EXAM_TYPE.MIDTERM.toString().equals(setEntity.getType())) {
				classEntity.setMidtermExam(examEntity.getExamDate());
				classEntity.setUpdateMemberId(userId);
				classEntity.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));
				
				classRepository.save(classEntity);
			} else if(ExamConstant.EXAM_TYPE.FINAL.toString().equals(setEntity.getType())) {
				classEntity.setFinalExam(examEntity.getExamDate());
				classEntity.setUpdateMemberId(userId);
				classEntity.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));	
				
				classRepository.save(classEntity);
			}
			
		}

		return ExamConstant.EXAM_PROCESS_RESULT.SUCCESS.toString();
	}

	/**
	 * 시험 이벤트 설정 저장
	 * 
	 * @param examEventSettingEntity
	 * @return
	 */
	public String updateExamEventSetting(ExamEventSettingEntity examEventSettingEntity) {
		// 로그인 유저 이메일
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginEmail = auth.getName();
		// 로그인 풀린 상태
		if (loginEmail.equals("anonymousUser")) {
			return ExamConstant.EXAM_PROCESS_RESULT.DIFFERENT.toString();
		}
		String userId = memberRepository.findByEmail(loginEmail).get().getId().toString();

		// 시험이 존재하는지 체크
		Optional<ExamEventSettingEntity> oEES = examEventSettingRepository.findById(examEventSettingEntity.getId());

		if (oEES.isEmpty()) {
			// 존재하지 않습니다.
			return ExamConstant.EXAM_PROCESS_RESULT.EMPTY.toString();
		} else {
			// 존재하는 시험
			ExamEventSettingEntity setEntity = oEES.get();

			setEntity.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));
			setEntity.setUpdateMemberId(userId);
			setEntity.setKeyboardEvent(examEventSettingEntity.getKeyboardEvent());
			setEntity.setMouseEvent(examEventSettingEntity.getMouseEvent());
			setEntity.setAudioEvent(examEventSettingEntity.getAudioEvent());
			setEntity.setEyeTrackingEvent(examEventSettingEntity.getEyeTrackingEvent());
			setEntity.setProcessEvent(examEventSettingEntity.getProcessEvent());

			examEventSettingRepository.save(setEntity);
		}

		return ExamConstant.EXAM_PROCESS_RESULT.SUCCESS.toString();
	}
	
	  /**
	   * 시험코드를 Base64로 생성
	   * 
	   * @param str
	   * @return
	   * @throws IOException
	   */
	  public static String base64Encode(String str) throws IOException {
		  byte[] strBytes = str.getBytes("UTF-8");
		  
		  return Base64.getEncoder().encodeToString(strBytes);
	  }
}
