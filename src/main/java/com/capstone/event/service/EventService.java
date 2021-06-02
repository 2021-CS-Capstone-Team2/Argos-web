package com.capstone.event.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.capstone.event.domain.dto.IDeviceEventDto;
import com.capstone.event.domain.dto.IProcessEventDto;
import com.capstone.event.domain.repository.DeviceEventRepository;
import com.capstone.event.domain.repository.ProcessEventRepository;

import lombok.AllArgsConstructor;

/**
 * 이벤트 서비스
 * 
 * @author swkim
 * @since 2021.04.08
 *
 */
@AllArgsConstructor
@Service
@Transactional
public class EventService {

	/**
	 * Repository
	 */
	private DeviceEventRepository deviceEventRepository;
	private ProcessEventRepository processEventRepository;

	/**
	 * 디바이스 이벤트가 일어난 과목 리스트
	 * 
	 * @param examList
	 * @return
	 */
	public List<IDeviceEventDto> getDeviceEventClass(List<String> examList) {
		return deviceEventRepository.getDeviceEventClass(examList);
	}

	/**
	 * 디바이스 이벤트
	 * 
	 * @param examList
	 * @return
	 */
	public List<IDeviceEventDto> getDeviceEventLog(List<String> examList) {
		return deviceEventRepository.getDeviceEventList(examList);
	}

	/**
	 * 프로세스 이벤트가 일어난 과목 리스트
	 * 
	 * @param examList
	 * @return
	 */
	public List<IProcessEventDto> getProcessEventClass(List<String> examList) {
		return processEventRepository.getProcessEventClass(examList);
	}

	/**
	 * 프로세스 이벤트
	 * 
	 * @param examList
	 * @return
	 */
	public List<IProcessEventDto> getProcessEventLog(List<String> examList) {
		return processEventRepository.getProcessEventList(examList);
	}

}
