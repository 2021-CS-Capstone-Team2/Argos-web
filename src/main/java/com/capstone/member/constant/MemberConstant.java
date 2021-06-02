package com.capstone.member.constant;

public class MemberConstant {

	/**
	 * 사용자 관리 프로세스 결과
	 * SUCCESS: 성공, DIFFERENT: 다른 상태,
	 * EXIST: 이미 존재, EMPTY: 존재하지 않음,
	 * DELETED: 삭제됨,
	 * FAIL: 실패
	 * 
	 */
	public static enum MEMBER_MGMT_RESULT {
		SUCCESS,
		DIFFERENT,
		EXISTS,
		EMPTY,
		DELETED,
		FAIL;
	}
}