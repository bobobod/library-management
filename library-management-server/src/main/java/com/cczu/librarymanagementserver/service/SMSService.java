package com.cczu.librarymanagementserver.service;

import com.cczu.librarymanagementserver.common.RespBean;

import javax.servlet.http.HttpServletRequest;

public interface SMSService {
	RespBean sendSMS(String tel, HttpServletRequest request);
}
