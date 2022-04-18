package com.cos.security.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.cos.security.model.User;

import lombok.Data;

// 시큐리티가 /login 주소 요청이 오면 가로채서 로그인 진행
// 로그인 진행 완료 후 시큐리티 세션을 생성(SecurityContextHolder)
// 객체 타입 -> Authentication 타입
// Authentication 안에 User 정보 존재
// User 객체 타입 -> UserDetails 타입

// Security Session -> Authentication -> UserDetails(PrincipalDetails)
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

	private User user; // composition
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	// 해당 사용자의 권한 반환
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collection = new ArrayList<GrantedAuthority>();
		collection.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return user.getRole();
			}
		});
		return collection;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		// 1년 회원이 로그인을 안하면 휴면 계정
		// 현재 시간 - 로그인 시간 > 1년 -> false
		return true;
	}

	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}
}