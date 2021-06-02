package com.capstone.common.constant;

public class CommonConstant {

	/**
	 * 프로세스 결과
	 * SUCCESS: 성공, DIFFERENT: 다른 상태,
	 * EXIST: 이미 존재, EMPTY: 존재하지 않음,
	 * ENCRYPTION_FAIL: 계정 암호화 실패, LOCK: 잠김, FAIL: 실패
	 * 
	 */
	public static enum PROCESS_RESULT {
		SUCCESS,
		DIFFERENT,
		EXISTS,
		EMPTY,
		LOCK,
		ENCRYPTION_FAIL,
		FAIL;
	}
	
	/**
	 * 상태
	 * Y: 활성화, N: 비활성화
	 * 
	 */
	public static enum MEMBER_STATUS {
		Y,
		N;
	}
}