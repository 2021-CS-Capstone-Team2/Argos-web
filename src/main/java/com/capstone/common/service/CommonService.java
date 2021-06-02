package com.capstone.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.capstone.common.constant.CommonConstant;
import com.capstone.common.constant.Role;
import com.capstone.common.domain.entity.DepartmentEntity;
import com.capstone.common.domain.entity.MemberAuthEntity;
import com.capstone.common.domain.entity.MemberEntity;
import com.capstone.common.domain.repository.DepartmentRepository;
import com.capstone.common.domain.repository.MemberAuthsRepository;
import com.capstone.common.domain.repository.MemberRepository;

import lombok.AllArgsConstructor;


/**
 * 공통 서비스 (권한, 로그인, 패스워드)
 * 
 * @author swkim
 *
 */
@AllArgsConstructor
@Service
@Transactional
public class CommonService implements UserDetailsService {

	private MemberRepository memberRepository;
	private MemberAuthsRepository memberAuthsRepository;
	private DepartmentRepository departmentRepository;

	
	/**
	 * 회원가입 프로세스 (멤버 및 권한 설정)
	 * 
	 * @param entity
	 * @param authSet
	 * @return
	 */
	public String setMemberAndAuthSet(MemberEntity entity, Set<MemberAuthEntity> authSet) {
		Optional<MemberEntity> checkId = memberRepository.findByEmailOrNumber(entity.getEmail(), entity.getNumber());
		
		// 이미 이메일이 존재
		if(checkId.isPresent()) {
			return CommonConstant.PROCESS_RESULT.EXISTS.toString();
		} else {
			// 비밀번호 암호화
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			entity.setPassword(passwordEncoder.encode(entity.getPassword()));

			memberRepository.save(entity);

			for (MemberAuthEntity memberAuths : authSet) {
				memberAuths.setMember(entity);
				memberAuthsRepository.save(memberAuths);
			}
			
			return CommonConstant.PROCESS_RESULT.SUCCESS.toString();
		}
	}

	@Override
	public UserDetails loadUserByUsername(String userKey) throws UsernameNotFoundException, LockedException {
		Optional<MemberEntity> userEntity = memberRepository.findByEmail(userKey);
		MemberEntity memberEntity = userEntity.get();

		if (!(memberEntity.isAccountNonLocked())) {
			throw new DisabledException(memberEntity.getEmail());
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		for (MemberAuthEntity auth : memberEntity.getAuths()) {
			if (auth.getAuthCode().equals("SYSTEM"))
				authorities.add(new SimpleGrantedAuthority(Role.SYSTEM.getValue()));
			if (auth.getAuthCode().equals("MANAGER"))
				authorities.add(new SimpleGrantedAuthority(Role.MANAGER.getValue()));
			if (auth.getAuthCode().equals("STUDENT"))
				authorities.add(new SimpleGrantedAuthority(Role.STUDENT.getValue()));
		}

		return new User(memberEntity.getEmail(), memberEntity.getPassword(), authorities);
	}

	public MemberEntity getUserInfo(String email) {
		Optional<MemberEntity> userEntity = memberRepository.findByEmail(email);
		return userEntity.get();
	}

	// 계정 존재 여부
	public String existMemberEmail(MemberEntity member) {
		String message = "";
		Optional<MemberEntity> checkDb = memberRepository.findByEmail(member.getEmail());
		// 존재하지 않는 이메일
		if (checkDb.isEmpty()) {
			message = CommonConstant.PROCESS_RESULT.EMPTY.toString();
			return message;
		} else {
			message = CommonConstant.PROCESS_RESULT.SUCCESS.toString();
		}
		return message;
	}

	// 계정 일치 여부
	public String checkMemberIdAndPw(MemberEntity member) {
		String message = "";
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		Optional<MemberEntity> checkDb = memberRepository.findByEmail(member.getEmail());
		// 존재하지 않는 이메일
		if (checkDb.isEmpty()) {
			message = CommonConstant.PROCESS_RESULT.EMPTY.toString();
			return message;
		} else {
			MemberEntity checkEntity = checkDb.get();

			if (passwordEncoder.matches(member.getPassword(), checkEntity.getPassword())) {

				if (checkEntity.getStatus().equals(CommonConstant.MEMBER_STATUS.Y.toString())) {
					message = CommonConstant.PROCESS_RESULT.SUCCESS.toString();
				} else {
					// 계정이 잠긴 상태
					message = CommonConstant.PROCESS_RESULT.LOCK.toString();
				}

			} else { // 올바르지 않은 비밀번호
				message = CommonConstant.PROCESS_RESULT.FAIL.toString();
			}
		}
		return message;
	}
	
	/**
	 * 전체 학과 조회
	 * 
	 * @return
	 */
	public List<DepartmentEntity> getAllDepartment() {
		return departmentRepository.findAll();
	}

}
