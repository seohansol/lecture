package com.kh.secom.member.model.service;



import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kh.secom.auth.model.vo.CustomUserDetails;
import com.kh.secom.exception.DuplicateUserException;
import com.kh.secom.exception.InvalidParameterException;
import com.kh.secom.exception.MissmatchPasswordException;
import com.kh.secom.member.model.mapper.MemberMapper;
import com.kh.secom.member.model.vo.ChangePasswordDTO;
import com.kh.secom.member.model.vo.Member;
import com.kh.secom.member.model.vo.MemberDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService { 

	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	
	
	@Override
	public void save(MemberDTO requestMember) { // 일반 사용자용 가입 메소드
		
		// 빈 문자열 유효성 검사
		if("".equals(requestMember.getUserId()) ||
		   "".equals(requestMember.getUserPwd())){
			throw new InvalidParameterException("유효하지 않은 값입니다.");
		}
		
		
		// DB에 이미 사용자가 입력한 사용자가 존재해서는 안됨
		Member searched = memberMapper.findByUserId(requestMember.getUserId());
		
		if(searched != null) {
			throw new DuplicateUserException("이미 존재하는 아이디 입니다.");
		}
		
		//비밀번호 평문이라 그냥 들어가면 안됨
		// + ROLE -- USER라고 저장할 예정
		
		Member member = Member.builder()
							  .userId(requestMember.getUserId())
							  .userPwd(passwordEncoder.encode(requestMember.getUserPwd()))
							  .role("ROLE_USER") // ROLE 이라는 접두사를 꼭 사용해야함 나중에 자동으로 부착하기에 미리 안하면 오류터짐
							  .build();

		memberMapper.save(member);
		log.info("회원 가입 성공!");
		
	}

	@Override
	public void changePassword(@Valid ChangePasswordDTO changeEntity) {
		
		// 비밀번호 바꿔주세요!
		// 제 현재 비밀번호는 currentPassword구요
		// 요게 맞다면 newPassword로 바꾸고싶어욤
		
		// 비번 검증해줄게!
		Long userNo = passwordMatches(changeEntity.getCurrentPassword());
		
		/*
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		// 유저 정보 (
		CustomUserDetails user =(CustomUserDetails)auth.getPrincipal();
		
		if(!(passwordEncoder.matches(changeEntity.getCurrentPassword(), user.getPassword()))) {
			throw new MissmatchPasswordException("비번 틀림");
		}
		*/
		
		String encodedPassword = passwordEncoder.encode(changeEntity.getNewPassword());
		
		Map<String, String> changeRequest = new HashMap();
		changeRequest.put("userNo", String.valueOf(userNo));
		changeRequest.put("password", encodedPassword);
		
		memberMapper.changePassword(changeRequest);
		
		
	}
	
	// 탈퇴다요
	@Override
	public void deleteByPassword(String password) {
		
		// 사용자가 입력한 비밀번호와 DB에 저장되어있는 비밀번호가 둘이 서로 같은지? 검증
		Long userNo = passwordMatches(password);
		
		memberMapper.deleteByPassword(userNo);
		
	}
	
	// 비번 내가 검증해줄게~
	private Long passwordMatches(String password) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDatails = (CustomUserDetails)auth.getPrincipal();
		if(!passwordEncoder.matches(password, userDatails.getPassword())) {
			throw new MissmatchPasswordException("비밀번호 달라요~");
		}
		return userDatails.getUserNo();
	}
	



	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
