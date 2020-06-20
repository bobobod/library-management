package com.cczu.librarymanagementserver.controller;

import com.cczu.librarymanagementserver.common.VerifyCode;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@CrossOrigin("*")
@RequestMapping("code")
@Api(tags = "验证码")
public class VerifyCodeController {
	@GetMapping
	public void code(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		VerifyCode vc = new VerifyCode();
		BufferedImage image = vc.getImage();
		String text = vc.getText();
		HttpSession session = req.getSession();
		session.setAttribute("code", text);
		VerifyCode.output(image, resp.getOutputStream());
	}
}
