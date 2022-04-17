package com.cos.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.security.config.oauth.PrincipalOAuth2UserService;

// OAuth 로그인 후 후처리 필요
// 1. code(인증) 
// 2. AccessToken(권한) 
// 3. 사용자 프로필 
// 4.1 프로필 토대로 회원가입 자동 진행
// 4.2 추가 정보, 쇼핑몰 -> 집주소, 백화점 -> 등급

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 필터체인에 등록
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, preAuthorize와 postAuthorize 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private PrincipalOAuth2UserService principalOAuth2UserService;
	
	
	// 해당 메서드의 반환 객체를 IoC에 등록
	@Bean
	public BCryptPasswordEncoder encodePassword() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		
		http.authorizeRequests()
			.antMatchers("/user/**").authenticated()
			.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
			.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().permitAll()
			.and()
			.formLogin()
			.loginPage("/loginForm")
			.loginProcessingUrl("/login") // /login 주소 호출 시 시큐리티가 낚아채서 대신 로그인 진행
			.defaultSuccessUrl("/")
			.and()
			.oauth2Login()
			.loginPage("/loginForm")
			.userInfoEndpoint() // 구글 로그인 완료 후, code X, (AccessToken + 사용자 프로필 O)
			.userService(principalOAuth2UserService);
	}
}