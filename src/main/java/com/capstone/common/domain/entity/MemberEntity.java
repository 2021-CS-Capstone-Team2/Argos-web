package com.capstone.common.domain.entity;

import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "MEMBER")
public class MemberEntity extends CommonEntity implements UserDetails {

	/**
	 * email 	: 로그인 이메일
	 * password	: 패스워드
	 * name 	: 이름
	 * number	: 학번 or 교원번호
	 * type 	: 구분 (STUDENT, MANAGER)
	 * dept_code: 학과 코드
	 * status	: 사용 허가 처리
	 *  
	 */
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String email;
	
	@Column(nullable = true)
	private String password;
	
	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String number;
	
	@Column
	private String type;
	
	@Column(nullable = false)
	private String deptCode; 
	
	@Column(nullable = true)
	private String status;
	
	@Column(nullable = true)
	private String hostedDomain;
	
	@OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "memberId")
	private Set<MemberAuthEntity> auths;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}


	@Override
	public boolean isEnabled() {
		return true;
	}


	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		if(this.getStatus().equals("N")) {
			return false;
		}
		else {
			return true;
		}
	}
}
