package com.kh.secom.member.model.service;

import com.kh.secom.member.model.vo.ChangePasswordDTO;
import com.kh.secom.member.model.vo.MemberDTO;

public interface MemberService {

	void save(MemberDTO requestmember);

	void changePassword(ChangePasswordDTO changeEntity);
}
