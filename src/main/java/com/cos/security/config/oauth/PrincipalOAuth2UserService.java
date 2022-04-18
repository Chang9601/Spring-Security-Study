package com.cos.security.config.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {
	
	// 구글로부터 받은 userRequest 데이터에 대한 후처리 진행 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration: " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
		System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue());  
		// 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code 반환(OAuth-Client 라이브러리가 받음) -> AccessToken 요청
		// userRequest 정보 -> loadUser 함수 사용 -> 회원 프로필
		System.out.println("getAttributes: " + super.loadUser(userRequest).getAttributes());
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		
		// 회원가입 강제 진행
		return super.loadUser(userRequest);
	}
}
