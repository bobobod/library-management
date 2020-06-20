package com.cczu.librarymanagementserver.controller;

import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.service.SMSService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@Api(tags = "短信发送")
public class SMSController {
	@Autowired
	private SMSService smsService;
	@ApiOperation("发送短信")
	@GetMapping("/sendSMS")
	public RespBean sendSMS(@RequestParam String tel, HttpServletRequest request){
		return smsService.sendSMS(tel,request);
	}
}
