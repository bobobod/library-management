package com.cczu.librarymanagementserver.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import springfox.documentation.annotations.ApiIgnore;

@Component
@PropertySource("classpath:smsConfig.properties")
@ConfigurationProperties(prefix = "sms")
@Data
@ApiIgnore
public class SMSEntity  {
	private String regionId;
	private String product;
	private String domain;
	private String accessKeyId;
	private String accessKeySecret;
	private String templateCode;

}
