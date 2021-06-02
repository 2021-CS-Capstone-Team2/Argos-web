package com.capstone.dashboard.dto;

import java.time.LocalDateTime;

/**
 * 
 * 대시보드 DTO Interface
 * 
 * @author swkim
 *
 */
public interface IDashboardDto {

	/**
	 * 이름
	 * 학번
	 * 디바이스이벤트 수
	 * 프로세스이벤트 수
	 * 이벤트
	 * 이벤트 타입
	 * 이벤트 메시지
	 * 이벤트 발생일
	 * 
	 */
	String getName();
	String getNumber();
	Integer getDeviceEvent();
	Integer getDeviceCheating();
	Integer getDeviceWarning();
	Integer getProcessEvent();
	String getEventTime();
	String getEventKo();
	String getEventEn();
	String getType();
	String getMessage();
	LocalDateTime getCreateDate();
	
}
