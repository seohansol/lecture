package com.kh.secom.auth.service;

import java.util.Map;

import com.kh.secom.member.model.vo.MemberDTO;

public interface AuthentlcationService {
	
	// 토근을 만들어서 보내기 위해 Map 사용!!
	Map<String, String> login(MemberDTO requestMember);

}
