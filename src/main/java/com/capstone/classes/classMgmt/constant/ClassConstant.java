package com.capstone.classes.classMgmt.constant;

public class ClassConstant {

	/**
	 * 과목 관리 프로세스 결과
	 * SUCCESS: 성공, DIFFERENT: 다른 상태,
	 * EXIST: 이미 존재, EMPTY: 존재하지 않음,
	 * DELETED: 삭제됨,
	 * FAIL: 실패
	 * 
	 * @author swkim
	 * 
	 */
	public static enum CLASS_PROCESS_RESULT {
		SUCCESS,
		DIFFERENT,
		EXISTS,
		EMPTY,
		DELETED,
		FAIL;
	}
	
	/**
	 * 과목 구성원 타입
	 * MANAGER: 과목 담당자
	 * STUDENT: 학생
	 * 
	 * @author swkim
	 *
	 */
	public static enum CLASS_MEMBER_TYPE {
		MANAGER,
		STUDENT;
	}
}