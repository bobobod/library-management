package com.cczu.librarymanagementserver.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum  StatusEnum {
	OK(200,"成功"),ERROR(500,"服务器出错"),NULLPOINTER(500,"空指针异常");
	private Integer code;
	private String desc;
}
