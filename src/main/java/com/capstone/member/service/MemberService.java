package com.capstone.member.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.capstone.classes.classMgmt.constant.ClassConstant;
import com.capstone.classes.classMgmt.domain.entity.ClassEntity;
import com.capstone.classes.classMgmt.domain.entity.ClassMemberEntity;
import com.capstone.classes.classMgmt.domain.repository.ClassMemberRepository;
import com.capstone.classes.classMgmt.domain.repository.ClassRepository;
import com.capstone.common.domain.entity.DepartmentEntity;
import com.capstone.common.domain.entity.MemberEntity;
import com.capstone.common.domain.repository.DepartmentRepository;
import com.capstone.common.domain.repository.MemberAuthsRepository;
import com.capstone.common.domain.repository.MemberRepository;
import com.capstone.member.constant.MemberConstant;
import com.capstone.member.dto.IMemberDto;
import com.capstone.member.dto.MemberDto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class MemberService {

	/**
	 * Repository
	 */
	private MemberRepository memberRepository;
	private ClassMemberRepository classMemberRepository;
	private ClassRepository classRepository;
	private DepartmentRepository departmentRepository;
	private MemberAuthsRepository memberAuthsRepository;
	
	/**
	 * 전체 이용자 가져오기 - 과목 구성원 등록 페이지
	 * 
	 * @return
	 */
	public List<IMemberDto> getClassMemberList(){
		return memberRepository.getClassMemberList();
	}
	
	/**
	 * 전체 이용자 가져오기 - 과목 구성원 등록 페이지
	 * 
	 * @return
	 */
	public List<DepartmentEntity> getDeptList(){
		return departmentRepository.findAll();
	}	

	// 사용자 리스트 가져오기
	public List<MemberDto> getMemberList(Locale locale) {
		List<MemberDto> memberDto = new ArrayList<MemberDto>();
		// 사용자 정보
		List<IMemberDto> iMemberDto = memberRepository.getMemberList();
		// 사용자의 수강 정보
		List<ClassMemberEntity> memberClassList = classMemberRepository.findAll();
		
		for (IMemberDto memberInfo : iMemberDto) {
			// 사용자 + 수강 정보 담는 객체
			MemberDto dto = new MemberDto();
			
			// 사용자에 따른 수강 과목
			ArrayList<String> classList = new ArrayList<>();
			
			// 과목 리스트
			for (ClassMemberEntity cmEntity : memberClassList) {
				if (memberInfo.getEmail().equals(cmEntity.getMemberEmail())) {
					Optional<ClassEntity> oC = classRepository.findByCodeAndSemester(cmEntity.getClassCode(), cmEntity.getSemester());

					if(oC.isPresent()) {
						if(locale.getLanguage().equals("ko")) {
							classList.add(oC.get().getNameKo()+" ("+oC.get().getSemester()+")");
						} else {
							classList.add(oC.get().getNameEn()+" ("+oC.get().getSemester()+")");
						}
					} else {
						continue;
					}					
				}
			}
			dto.setClassList(classList);
			dto.setIMemberDto(memberInfo);
			memberDto.add(dto);
		}

		return memberDto;
	}
	
	/**
	 * 사용자 관리 -정보 수정
	 * 
	 * @param memberDto
	 * @return
	 */
	public String editMemberInfo(MemberDto memberDto) {
		// 로그인 유저 이메일
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loginEmail = auth.getName();
		// 로그인 풀린 상태
		if (loginEmail.equals("anonymousUser")) {
			return ClassConstant.CLASS_PROCESS_RESULT.DIFFERENT.toString();
		}
		
		MemberEntity member = memberRepository.findByEmail(memberDto.getMemberEntity().getEmail()).get();
		Long loginMemberId = memberRepository.findByEmail(loginEmail).get().getId();
		member.setName(memberDto.getMemberEntity().getName());
		member.setStatus(memberDto.getMemberEntity().getStatus());

		member.setUpdateMemberId(loginMemberId.toString());
		member.setUpdateDate(LocalDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.MILLIS));

		// session에 있는 사용자id로 modify
		memberRepository.save(member);

		return MemberConstant.MEMBER_MGMT_RESULT.SUCCESS.toString();
	}
	
	/**
	 * 사용자 삭제
	 * 
	 * @param memberList
	 * @return
	 */
	public String deleteMember(String[] memberList) {
		for (String email : memberList) {
			Optional<MemberEntity> memberEntity = memberRepository.findByEmail(email);
			if (memberEntity.isEmpty()) {
				// 이미 삭제된 사용자
				return MemberConstant.MEMBER_MGMT_RESULT.DELETED.toString();
			} else {
				MemberEntity member = memberEntity.get();
				// 사용자 권한 삭제
				memberAuthsRepository.deleteByMemberId(member.getId());
			}
		}
		try {
			// 사용자 과목 정보 삭제
			classMemberRepository.deleteAllByEmailIn(memberList);
			// 사용자 삭제
			memberRepository.deleteAllByEmailIn(memberList);
		} catch (Exception e) {
			// 이미 삭제된 사용자
			return MemberConstant.MEMBER_MGMT_RESULT.DELETED.toString();
		}
		return MemberConstant.MEMBER_MGMT_RESULT.SUCCESS.toString();
	}
	
}
