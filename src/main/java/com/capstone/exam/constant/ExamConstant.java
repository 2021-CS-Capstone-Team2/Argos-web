package com.capstone.exam.constant;

/**
 * 시험 관리 Constant
 * 
 * @author swkim
 *
 */
public class ExamConstant {

	/**
	 * 과목 관리 프로세스 결과
	 * 
	 * SUCCESS: 성공, DIFFERENT: 다른 상태,
	 * EXIST: 이미 존재, EMPTY: 존재하지 않음,
	 * DELETED: 삭제됨,
	 * FAIL: 실패
	 * 
	 */
	public static enum EXAM_PROCESS_RESULT {
		SUCCESS,
		DIFFERENT,
		EXISTS,
		EMPTY,
		DELETED,
		FAIL;
	}
	
	/**
	 * 시험 유형
	 * 
	 * QUIZ: 퀴즈
	 * MIDTERM: 중간고사
	 * FINAL: 기말고사
	 * 
	 */
	public static enum EXAM_TYPE {
		QUIZ,
		MIDTERM,
		FINAL;
	}
	
	/**
	 * 시험 이벤트 셋팅
	 * 
	 *  Y, N
	 *
	 */
	public static enum EXAM_EVENT_SETTING {
		Y,
		N;
	}
	
	/**
	 * 시험 이력- 상태
	 * 
	 * FINISH: 종료
	 * PROCEED: 시험중
	 * 
	 */
	public static enum EXAM_LOG_STATUS {
		FINISH,
		PROCEED;
	}
}