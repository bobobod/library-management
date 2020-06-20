package com.cczu.librarymanagementserver.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {
	public Authentication attemptAuthentication(HttpServletRequest request,
												HttpServletResponse response) throws AuthenticationException {
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException(
					"Authentication method not supported: " + request.getMethod());
		}
		if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE) || request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)) {
			Map<String, String> loginData = new HashMap<>();
			try {
				loginData = new ObjectMapper().readValue(request.getInputStream(), Map.class);
			} catch (IOException e) {
				String code = loginData.get("code");
				String verify_code = (String) request.getSession().getAttribute("code");
				checkCode(response, code, verify_code);
				e.printStackTrace();
			}
			String username = loginData.get(getUsernameParameter());
			String password = loginData.get(getPasswordParameter());
			if (username == null) {
				username = "";
			}
			if (password == null) {
				password = "";
			}
			username = username.trim();

			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
					username, password);
			setDetails(request, authRequest);
			return this.getAuthenticationManager().authenticate(authRequest);
		} else {
			return super.attemptAuthentication(request, response);
		}
	}

	private void checkCode(HttpServletResponse response, String code, String verify_code) {
		if (code == null || verify_code == null || "".equals(code) || !verify_code.toLowerCase().equals(code.toLowerCase())) {
			//验证码不正确
			throw new AuthenticationServiceException("验证码不正确");
		}
	}
}
