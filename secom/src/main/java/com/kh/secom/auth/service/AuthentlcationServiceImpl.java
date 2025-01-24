package com.kh.secom.auth.service;

import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.kh.secom.auth.model.vo.CustomUserDetails;
import com.kh.secom.member.model.vo.MemberDTO;
import com.kh.secom.token.model.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthentlcationServiceImpl implements AuthentlcationService {

	private final AuthenticationManager authenticationManager;
	// private final JwtUtil jwt;
	private final TokenService tokenService;

	@Override
	public Map<String, String> login(MemberDTO requestMember) {

		// 사용자 인증
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(requestMember.getUserId(), requestMember.getUserPwd()));
		// usernamePasswordAuthenticationToken
		/*
		 * 사용자가 입력한 userName과 Password를 검증하는 용도로 사용하는 클래스 주로, SpringSecurity에서 인증을 시도할 때
		 * 사용함
		 * 
		 */

		CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

		log.info("로그인절차 성공!");
		log.info("DB에서 조회된 사용자의 정보 {}", user);
		/*
		 * String accessToken = jwt.getAccessToken(user.getUsername()); String
		 * refreshToken = jwt.getRefreshToken(user.getUsername());
		 * log.info("토큰 발급이욤 : {}", accessToken); log.info("리프레사ㅣ 토큰 : {}",
		 * refreshToken);
		 */
		Map<String, String> tokens = tokenService.generateToken(user.getUsername(), user.getUserNo());
		
		return tokens;

	}

}
