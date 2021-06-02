package com.capstone.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.capstone.common.service.CommonService;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private CommonService commonService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
		web.ignoring().antMatchers("/assets/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				// 페이지 권한 설정
				.antMatchers("/argos/**").hasRole("SYSTEM") // 과목 관리
				.antMatchers("/common/**", "/event/**", "/dashboard/**", "/classMgmt/**", "/exam/**").hasAnyRole("SYSTEM","MANAGER") // 공통영역
				.antMatchers("/", "/sign/**", "/filedownload").permitAll().and() // 로그인 설정
				// 로그인
				.formLogin().loginPage("/login").defaultSuccessUrl("/main/branch", true).permitAll().and()
				// 로그아웃 - 바로 로그인으로
				.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login").invalidateHttpSession(true).and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
				.logoutSuccessUrl("/user/logout/result")
				.invalidateHttpSession(true)
				.and()
				// 403 예외처리 핸들링 - 바로 로그인으로
				.exceptionHandling().accessDeniedPage("/denied").and().sessionManagement().maximumSessions(5)
				.expiredUrl("/login").maxSessionsPreventsLogin(false);

		http.csrf().disable();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(commonService).passwordEncoder(passwordEncoder());
	}
}
