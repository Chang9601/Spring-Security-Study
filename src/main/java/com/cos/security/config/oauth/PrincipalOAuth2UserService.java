package com.cos.security.config.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security.config.auth.PrincipalDetails;
import com.cos.security.model.User;
import com.cos.security.repository.UserRepository;

@Service
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {
	
	//private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final UserRepository userRepository;
	
	@Autowired
	public PrincipalOAuth2UserService(/*BCryptPasswordEncoder bCryptPasswordEncoder, */UserRepository userRepository) {
		//this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userRepository = userRepository;
	}
	
	// 구글로부터 받은 userRequest 데이터에 대한 후처리 진행 함수
	// 함수 종료 시 @AuthenticationPrincipal 어노테이션 생성
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("getClientRegistration: " + userRequest.getClientRegistration()); // registrationId로 어떤 OAuth로 로그인 했는지 확인 가능
		System.out.println("getAccessToken: " + userRequest.getAccessToken().getTokenValue());  

		OAuth2User oAuth2User = super.loadUser(userRequest);
		// 구글 로그인 버튼 클릭 -> 구글 로그인 창 -> 로그인 완료 -> code 반환(OAuth-Client 라이브러리가 받음) -> AccessToken 요청
		// userRequest 정보 -> loadUser 함수 사용 -> 회원 프로필
		System.out.println("getAttributes: " + oAuth2User.getAttributes());

		// 회원가입 강제 진행
		String provider = userRequest.getClientRegistration().getClientId(); // google
		String providerId = oAuth2User.getAttribute("sub"); 
		String username = provider + "_" + providerId;  // google_...
		String password = "겟인데어"; // bCryptPasswordEncoder.encode("겟인데어");
		String email = oAuth2User.getAttribute("email");
		String role = "ROLE_USER";
		
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			userEntity = User.builder()
				.username(username)
				.password(password)
				.email(email)
				.role(role)
				.provider(provider)
				.providerId(providerId)
				.build();
			userRepository.save(userEntity);
		} 
			
		return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
	}
}