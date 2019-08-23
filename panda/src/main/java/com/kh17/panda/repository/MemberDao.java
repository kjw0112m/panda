package com.kh17.panda.repository;

import com.kh17.panda.entity.MemberDto;

public interface MemberDao {
	boolean regist(MemberDto memberDto);
	MemberDto idCheck(String id);
	MemberDto login(MemberDto memberDto);
	MemberDto get(String id);
	void lastlogin(String id);
	void delete(String id);
	void change(MemberDto memberDto);
	boolean findPassword(MemberDto memberDto);
	void changePw(MemberDto memberDto);
	MemberDto findId(MemberDto memberDto);
	void lastchangepw(String id);
	
}