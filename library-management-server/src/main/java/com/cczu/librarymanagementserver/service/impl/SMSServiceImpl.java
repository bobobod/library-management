package com.cczu.librarymanagementserver.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.SMSEntity;
import com.cczu.librarymanagementserver.service.SMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class SMSServiceImpl implements SMSService {

	@Autowired
	private SMSEntity smsEntity;
	@Override
	public RespBean sendSMS(String tel, HttpServletRequest req)  {

		if (req.getSession().getAttribute(tel)!=null) return RespBean.ok("已有该验证码的缓存");
		//初始化ascClient需要的几个参数
		final String product = smsEntity.getProduct();//短信API产品名称（短信产品名固定，无需修改）
		final String domain = smsEntity.getDomain();//短信API产品域名（接口地址固定，无需修改）
//替换成你的AK
		final String accessKeyId = smsEntity.getAccessKeyId();//你的accessKeyId,参考本文档步骤2
		final String accessKeySecret = smsEntity.getAccessKeySecret();//你的accessKeySecret，参考本文档步骤2
//初始化ascClient,暂时不支持多region（请勿修改）
		IClientProfile profile = DefaultProfile.getProfile(smsEntity.getRegionId(), accessKeyId,
				accessKeySecret);
		try {
			DefaultProfile.addEndpoint(smsEntity.getRegionId(), smsEntity.getRegionId(), product, domain);
		} catch (ClientException e) {
			e.printStackTrace();
		}
		IAcsClient acsClient = new DefaultAcsClient(profile);
		//组装请求对象
		SendSmsRequest request = new SendSmsRequest();
		//使用post提交
		request.setMethod(MethodType.POST);
		//必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
		request.setPhoneNumbers(tel);
		//必填:短信签名-可在短信控制台中找到
		request.setSignName("麦浪技术支持");
		//必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
		request.setTemplateCode(smsEntity.getTemplateCode());
		//可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
		//友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
		Long code = Math.round((Math.random() + 1) * 1000);
		Map<String,Long> map = new HashMap<>();
		map.put("code",code);
		request.setTemplateParam(JSONObject.toJSONString(map));


		//请求失败这里会抛ClientException异常
		SendSmsResponse sendSmsResponse = null;
		try {
			sendSmsResponse = acsClient.getAcsResponse(request);
			System.out.println(sendSmsResponse);
		} catch (ClientException e) {
			e.printStackTrace();
		}

		if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
			req.getSession().setAttribute(tel,code.toString());
			return RespBean.ok("发送手机验证码成功");
//请求成功
		}
		return RespBean.error("发送手机验证码失败");
	}
}
