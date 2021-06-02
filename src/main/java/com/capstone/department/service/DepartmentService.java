package com.capstone.department.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.capstone.classes.classMgmt.constant.ClassConstant;
import com.capstone.common.domain.entity.DepartmentEntity;
import com.capstone.common.domain.repository.DepartmentRepository;
import com.capstone.common.domain.repository.MemberRepository;

import lombok.AllArgsConstructor;

/**
 * 학과 관리 서비스
 * 
 * 시스템 관리자만이 담당
 * 
 * @author swkim
 *
 */
@AllArgsConstructor
@Service
@Transactional
public class DepartmentService {

	/**
	 * Repository
	 */
	private DepartmentRepository departmentRepository;
	private MemberRepository memberRepository;

	/**
	 * 전체 학과 조회
	 * 
	 * @return
	 */
	public List<DepartmentEntity> getAllDept() {
		return departmentRepository.findAll();
	}

	/**
	 * 새 학과 등록
	 *
	 * @param departmentEntity
	 * @return
	 */
	public String createDepartment(DepartmentEntity departmentEntity) {
		// 로그인 유저 이메일
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginEmail = auth.getName();
		// 로그인 풀린 상태
		if (loginEmail.equals("anonymousUser")) {
			return ClassConstant.CLASS_PROCESS_RESULT.DIFFERENT.toString();
		}
		DepartmentEntity dEntity = new DepartmentEntity();
		// 학과가 존재하는지 체크
		Optional<DepartmentEntity> oD = departmentRepository.findByCode(departmentEntity.getCode());
		// 존재하지 않다면 생성
		if (oD.isEmpty()) {
			dEntity.setCreateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));
			dEntity.setCreateMemberId(memberRepository.findByEmail(loginEmail).get().getId().toString());
			dEntity.setCode(departmentEntity.getCode());
			dEntity.setNameKo(departmentEntity.getNameKo());
			dEntity.setNameEn(departmentEntity.getNameEn());

			departmentRepository.save(dEntity);

		} else {
			// 이미 존재하는 학과
			return ClassConstant.CLASS_PROCESS_RESULT.EXISTS.toString();
		}

		return ClassConstant.CLASS_PROCESS_RESULT.SUCCESS.toString();
	}

	/**
	 * 학과 수정
	 * 
	 * @param departmentEntity
	 * @return
	 */
	public String editDepartment(DepartmentEntity departmentEntity) {
		// 로그인 유저 이메일
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginEmail = auth.getName();
		// 로그인 풀린 상태
		if (loginEmail.equals("anonymousUser")) {
			return ClassConstant.CLASS_PROCESS_RESULT.DIFFERENT.toString();
		}
		String userId = memberRepository.findByEmail(loginEmail).get().getId().toString();

		// 과목이 존재하는지 체크
		Optional<DepartmentEntity> oD = departmentRepository.findByCode(departmentEntity.getCode());
		// 존재하지 않다면 수정
		if (oD.isEmpty()) {
			Optional<DepartmentEntity> temp = departmentRepository.findById(departmentEntity.getId());

			if (temp.isEmpty()) {
				// 과목이 존재하지 않습니다. (삭제)
				return ClassConstant.CLASS_PROCESS_RESULT.EMPTY.toString();
			} else {
				DepartmentEntity dE = temp.get();

				dE.setUpdateMemberId(userId);
				dE.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));
				dE.setNameKo(departmentEntity.getNameKo());
				dE.setNameEn(departmentEntity.getNameEn());
				dE.setCode(departmentEntity.getCode());

				departmentRepository.save(dE);

			}
		} else {
			// 존재하는 과목
			DepartmentEntity dE = oD.get();

			// 원래의 과목
			if (departmentEntity.getId().equals(dE.getId())) {
				dE.setUpdateMemberId(userId);
				dE.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));
				dE.setNameKo(departmentEntity.getNameKo());
				dE.setNameEn(departmentEntity.getNameEn());

				departmentRepository.save(dE);
			} else {
				// 이미 존재하는 과목코드입니다.
				return ClassConstant.CLASS_PROCESS_RESULT.EXISTS.toString();
			}
		}

		return ClassConstant.CLASS_PROCESS_RESULT.SUCCESS.toString();
	}


}
