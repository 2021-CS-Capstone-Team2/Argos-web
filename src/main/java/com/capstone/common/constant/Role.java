package com.capstone.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
	SYSTEM("ROLE_SYSTEM"), // SYSTEM 관리자
	MANAGER("ROLE_MANAGER"), // 교수, 조교
	STUDENT("ROLE_STUDENT"); // 학생

	private String value;
}
