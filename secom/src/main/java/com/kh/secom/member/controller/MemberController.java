package com.kh.secom.member.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.secom.auth.service.AuthentlcationServiceImpl;
import com.kh.secom.member.model.service.MemberService;
import com.kh.secom.member.model.vo.ChangePasswordDTO;
import com.kh.secom.member.model.vo.LoginResponse;
import com.kh.secom.member.model.vo.MemberDTO;
import com.kh.secom.token.model.service.TokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="members", produces="application/json; charset=UTF-8")

@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;
	private final AuthentlcationServiceImpl authService;
	private final TokenService tokenService;

	// 새롭게 데이터를 만들어내는 요청(INSERT) == POST
	@PostMapping
	public ResponseEntity<String> save(@Valid @RequestBody MemberDTO requestMember) {
		// valid 벨리데이션쓸꺼야!
		// log.info("요청한 사용자의 데이터 : {}", requestMember);
		memberService.save(requestMember);

		return ResponseEntity.ok("회원가입에 성공했습니다.");
	}
	// 로그인 부분
	@PostMapping("login")
	public ResponseEntity<LoginResponse> login(@RequestBody MemberDTO requestMember) {

		/*
		 * 로그인에 성공했을 떄?? ==> 인증과정 : 원래 개발자가 했음 아이디 / 비밀번호(가입시 평문) 아이디 / 비밀번호(로그인시 암호문)
		 */

		Map<String, String> tokens = authService.login(requestMember);
		// 로그인에 성공 했을 때
		// AccessToken
		// RefreshToken 반환

		LoginResponse response = LoginResponse.builder().username(requestMember.getUserId()).tokens(tokens).build();
		
		log.info("이거머야? : {}", response);
		return ResponseEntity.ok(response);
	}
	
	// 비밀번호 변경 기능
	// 기존 비번 / 바꾸고 싶은 비밀번호
	@PutMapping
	public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDTO changeEntity){ // 토큰으로 인증
		
		log.info("넘어와? {}", changeEntity);
		memberService.changePassword(changeEntity);
		
		
		return ResponseEntity.ok("성공이다요");
	}
	
	// 회원 탈퇴 기능
	@DeleteMapping
	public ResponseEntity<String> deleteByByPassword(@RequestBody Map<String, String> password){
		
		memberService.deleteByPassword(password.get("password"));
		
		return ResponseEntity.ok("삭제 완료!");
	}
	// 토큰 다시받기
	@PostMapping("refresh")
	public ResponseEntity<Map> refresh(@RequestBody Map<String, String> tokens){
		String refreshToken = tokens.get("refreshToken");
		
		Map<String, String> newTokens = tokenService.refreshTokens(refreshToken);
		
		return ResponseEntity.ok(newTokens);
	}
	
	
	

}
