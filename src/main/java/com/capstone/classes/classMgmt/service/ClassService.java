package com.capstone.classes.classMgmt.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.capstone.classes.classMgmt.constant.ClassConstant;
import com.capstone.classes.classMgmt.domain.dto.IClassDto;
import com.capstone.classes.classMgmt.domain.entity.ClassEntity;
import com.capstone.classes.classMgmt.domain.entity.ClassMemberEntity;
import com.capstone.classes.classMgmt.domain.repository.ClassMemberRepository;
import com.capstone.classes.classMgmt.domain.repository.ClassRepository;
import com.capstone.common.domain.entity.MemberEntity;
import com.capstone.common.domain.repository.MemberRepository;

import lombok.AllArgsConstructor;

/**
 * 과목 관리 서비스
 * 
 * 시스템 관리자와 과목의 담당자가 사용하는 서비스
 * 
 * @author swkim
 *
 */
@AllArgsConstructor
@Service
@Transactional
public class ClassService {

	/**
	 * Repository
	 */
	private ClassRepository classRepository;
	private ClassMemberRepository classMemberRepository;
	private MemberRepository memberRepository;

	/**
	 * 전체 과목 조회
	 * 
	 * @return
	 */
	public List<ClassEntity> getAllClasses() {
		return classRepository.findAll();
	}

	/**
	 * 선택한 과목 가져오기
	 * 
	 * @param classEntity
	 * @return
	 */
	public ClassEntity getSelectClass(ClassEntity classEntity) {
		Optional<ClassEntity> oC = classRepository.findById(classEntity.getId());

		if (oC.isPresent()) {
			// 과목의 담당자인지 체크
			Integer checkManager = classMemberRepository.countByClassCodeAndSemesterAndType(oC.get().getCode(), oC.get().getSemester(),
					ClassConstant.CLASS_MEMBER_TYPE.MANAGER.toString());
			if (checkManager > 0 || "[ROLE_SYSTEM]".equals(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString())) {
				return oC.get();
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 나의 과목 조회
	 * 
	 * @return
	 */
	@SuppressWarnings("null")
	public ArrayList<ClassEntity> getMyClasses() {
		// 로그인 유저 이메일
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginEmail = auth.getName();
		// 나의 관리 과목 가져옴
		List<ClassMemberEntity> classMember = classMemberRepository.findAllByMemberEmailAndTypeOrderBySemesterDesc(loginEmail, ClassConstant.CLASS_MEMBER_TYPE.MANAGER.toString());

		// 과목 리스트 생성
		ArrayList<ClassEntity> classEntityList = new ArrayList<ClassEntity>();

		// 과목 리스트 등록
		for (ClassMemberEntity cM : classMember) {
			Optional<ClassEntity> oC = classRepository.findByCodeAndSemester(cM.getClassCode(), cM.getSemester());
			if (oC.isPresent()) {
				classEntityList.add(oC.get());
			}
		}

		return classEntityList;
	}

	/**
	 * 새 과목 등록
	 *
	 * @param classEntity
	 * @return
	 */
	public String createClass(ClassEntity classEntity) {
		// 로그인 유저 이메일
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginEmail = auth.getName();
		// 로그인 풀린 상태
		if (loginEmail.equals("anonymousUser")) {
			return ClassConstant.CLASS_PROCESS_RESULT.DIFFERENT.toString();
		}
		ClassEntity cEntity = new ClassEntity();
		// 과목이 존재하는지 체크
		Optional<ClassEntity> oC = classRepository.findByCodeAndSemester(classEntity.getCode(), classEntity.getSemester());
		// 존재하지 않다면 생성
		if (oC.isEmpty()) {
			cEntity.setCreateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));
			cEntity.setCreateMemberId(memberRepository.findByEmail(loginEmail).get().getId().toString());
			cEntity.setCode(classEntity.getCode());
			cEntity.setNameKo(classEntity.getNameKo());
			cEntity.setNameEn(classEntity.getNameEn());
			cEntity.setSemester(classEntity.getSemester());

			classRepository.save(cEntity);

		} else {
			// 이미 존재하는 과목
			return ClassConstant.CLASS_PROCESS_RESULT.EXISTS.toString();
		}

		return ClassConstant.CLASS_PROCESS_RESULT.SUCCESS.toString();
	}

	/**
	 * 과목 삭제
	 * 
	 * @param classEntity
	 * @return
	 */
	public String deleteClass(ClassEntity classEntity) {
		ClassEntity cEntity = new ClassEntity();
		// 회사에서 지원중인 서비스인지 체크
		Optional<ClassEntity> oC = classRepository.findByCodeAndSemester(classEntity.getCode(), classEntity.getSemester());

		if (oC.isEmpty()) {
			// 이미 삭제된 과목 (존재 X)
			return ClassConstant.CLASS_PROCESS_RESULT.EMPTY.toString();
		} else {
			// 과목에 등록된 사람부터 삭제
			List<ClassMemberEntity> listCM = classMemberRepository.findAllByClassCodeAndSemester(classEntity.getCode(), classEntity.getSemester());
			for (ClassMemberEntity cmEntity : listCM) {
				classMemberRepository.delete(cmEntity);
			}

			cEntity = oC.get();
			classRepository.delete(cEntity);
		}

		return ClassConstant.CLASS_PROCESS_RESULT.SUCCESS.toString();
	}

	/**
	 * 과목 수정
	 * 
	 * @param classEntity
	 * @return
	 */
	public String editClass(ClassEntity classEntity) {
		// 로그인 유저 이메일
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginEmail = auth.getName();
		// 로그인 풀린 상태
		if (loginEmail.equals("anonymousUser")) {
			return ClassConstant.CLASS_PROCESS_RESULT.DIFFERENT.toString();
		}
		String userId = memberRepository.findByEmail(loginEmail).get().getId().toString();

		// 과목이 존재하는지 체크
		Optional<ClassEntity> oC = classRepository.findByCodeAndSemester(classEntity.getCode(), classEntity.getSemester());
		// 존재하지 않다면 수정
		if (oC.isEmpty()) {
			Optional<ClassEntity> temp = classRepository.findById(classEntity.getId());

			if (temp.isEmpty()) {
				// 과목이 존재하지 않습니다. (삭제)
				return ClassConstant.CLASS_PROCESS_RESULT.EMPTY.toString();
			} else {
				ClassEntity cE = temp.get();

				cE.setUpdateMemberId(userId);
				cE.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));
				cE.setNameKo(classEntity.getNameKo());
				cE.setNameEn(classEntity.getNameEn());
				cE.setCode(classEntity.getCode());
				if (classEntity.getMidtermExam() != null || classEntity.getFinalExam() != null) {
					cE.setMidtermExam(classEntity.getMidtermExam());
					cE.setFinalExam(classEntity.getFinalExam());
				}

				classRepository.save(cE);

			}
		} else {
			// 존재하는 과목
			ClassEntity cE = oC.get();

			// 원래의 과목
			if (classEntity.getId().equals(cE.getId())) {
				cE.setUpdateMemberId(userId);
				cE.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));
				cE.setNameKo(classEntity.getNameKo());
				cE.setNameEn(classEntity.getNameEn());
				if (classEntity.getMidtermExam() != null || classEntity.getFinalExam() != null) {
					cE.setMidtermExam(classEntity.getMidtermExam());
					cE.setFinalExam(classEntity.getFinalExam());
				}

				classRepository.save(cE);
			} else {
				// 이미 존재하는 과목코드입니다.
				return ClassConstant.CLASS_PROCESS_RESULT.EXISTS.toString();
			}
		}

		return ClassConstant.CLASS_PROCESS_RESULT.SUCCESS.toString();
	}

	/**
	 * 과목 구성원 리스트
	 * 
	 * @param classEntity
	 * @return
	 */
	public List<IClassDto> getClassMemberList(ClassEntity classEntity) {
		return classMemberRepository.getClassMemberList(classEntity.getCode(), classEntity.getSemester());
	}

	/**
	 * 과목 구성원 등록
	 * 
	 * @param memberList
	 * @return
	 */
	public String regiClassMember(String[] memberList, Long classId, String cmType) {
		// 로그인 유저 이메일
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginEmail = auth.getName();
		// 로그인 풀린 상태
		if (loginEmail.equals("anonymousUser")) {
			return ClassConstant.CLASS_PROCESS_RESULT.DIFFERENT.toString();
		}
		// 과목 존재유무 판단
		Optional<ClassEntity> oC = classRepository.findById(classId);
		if (oC.isEmpty()) {
			// 과목이 없습니다. (삭제된 경우)
			return ClassConstant.CLASS_PROCESS_RESULT.EMPTY.toString();
		} else {
			ClassEntity classEntity = oC.get();

			for (String email : memberList) {
				Optional<MemberEntity> memberEntity = memberRepository.findByEmail(email);
				Optional<ClassMemberEntity> oCM = classMemberRepository.findByMemberEmailAndClassCodeAndSemester(email, classEntity.getCode(), classEntity.getSemester());
				if (memberEntity.isEmpty()) {
					// 이미 삭제된 사용자
					return ClassConstant.CLASS_PROCESS_RESULT.DELETED.toString();
				} else if (oCM.isEmpty()) {
					// 등록되지 않은 사용자만 등록
					MemberEntity member = memberEntity.get();
					ClassMemberEntity cmEntity = new ClassMemberEntity();

					cmEntity.setCreateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));
					cmEntity.setCreateMemberId(memberRepository.findByEmail(loginEmail).get().getId().toString());
					cmEntity.setClassCode(classEntity.getCode());
					cmEntity.setMemberEmail(member.getEmail());
					cmEntity.setSemester(classEntity.getSemester());
					cmEntity.setType(cmType);

					classMemberRepository.save(cmEntity);

				} else {
					return ClassConstant.CLASS_PROCESS_RESULT.EXISTS.toString();
				}
			}
		}
		return ClassConstant.CLASS_PROCESS_RESULT.SUCCESS.toString();
	}

	/**
	 * 과목 구성원 삭제
	 * 
	 * @param cmEntity
	 * @return
	 */
	public String deleteClassMember(ClassMemberEntity cmEntity) {
		Optional<ClassMemberEntity> oC = classMemberRepository.findById(cmEntity.getId());

		if (oC.isEmpty()) {
			return ClassConstant.CLASS_PROCESS_RESULT.EMPTY.toString();
		} else {
			classMemberRepository.deleteById(oC.get().getId());
			return ClassConstant.CLASS_PROCESS_RESULT.SUCCESS.toString();
		}
	}
}
