package com.cczu.librarymanagementserver.config;

import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Admin;
import com.cczu.librarymanagementserver.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private AdminService adminService;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(adminService).passwordEncoder(bCryptPasswordEncoder());
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	@Bean
	public LoginFilter loginFilter() throws Exception {
		LoginFilter loginFilter = new LoginFilter();
		loginFilter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
				response.setContentType("application/json;charset=utf-8");
				PrintWriter out = response.getWriter();
				Admin admin = (Admin) authentication.getPrincipal();
				admin.setAPassword(null);
				RespBean ok = RespBean.ok("登录成功", admin);
				String s = new ObjectMapper().writeValueAsString(ok);
				out.write(s);
				out.flush();
				out.close();
			}
		});
		loginFilter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
				response.setContentType("application/json;charset=utf-8");
				PrintWriter out = response.getWriter();
				RespBean respBean = RespBean.error(exception.getMessage());
				if (exception instanceof LockedException) {
					respBean.setMsg("账户被锁定，请联系管理员!");
				} else if (exception instanceof CredentialsExpiredException) {
					respBean.setMsg("密码过期，请联系管理员!");
				} else if (exception instanceof AccountExpiredException) {
					respBean.setMsg("账户过期，请联系管理员!");
				} else if (exception instanceof DisabledException) {
					respBean.setMsg("账户被禁用，请联系管理员!");
				} else if (exception instanceof BadCredentialsException) {
					respBean.setMsg("用户名或者密码输入错误，请重新输入!");
				}
				out.write(new ObjectMapper().writeValueAsString(respBean));
				out.flush();
				out.close();
			}
		});
		loginFilter.setAuthenticationManager(super.authenticationManagerBean());
		loginFilter.setFilterProcessesUrl("/doLogin");
		return loginFilter;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**","/swagger-ui.html", "/js/**", "/index.html", "/img/**", "/fonts/**", "/favicon.ico", "/code","/register");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.and()
				.logout()
				.addLogoutHandler(new LogoutHandler() {
					@Override
					public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
						response.setContentType("application/json;charset=utf-8;");
						try {
							PrintWriter out = response.getWriter();
							out.write(new ObjectMapper().writeValueAsString(RespBean.ok("注销成功!")));
							out.flush();
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				})
				.permitAll()
				.and()
				.csrf()
				.disable()
				//异常没有认证时，在这里处理结果，不要重定向
				.exceptionHandling()
				.authenticationEntryPoint(new AuthenticationEntryPoint() {
					@Override
					public void commence(HttpServletRequest request, HttpServletResponse resp, AuthenticationException authException) throws IOException, ServletException {
						resp.setContentType("application/json;charset=utf-8");
						resp.setStatus(401);
						PrintWriter out = resp.getWriter();
						RespBean respBean = RespBean.error("访问失败!");
						if (authException instanceof InsufficientAuthenticationException) {
							respBean.setMsg("请求失败，请联系管理员!");
						}
						out.write(new ObjectMapper().writeValueAsString(respBean));
						out.flush();
						out.close();
					}
				})
				.accessDeniedHandler(new AccessDeniedHandler() {
					@Override
					public void handle(HttpServletRequest req, HttpServletResponse resp, AccessDeniedException accessDeniedException) throws IOException, ServletException {
						resp.setContentType("application/json;charset=utf-8");
						resp.setStatus(403);
						PrintWriter out = resp.getWriter();
						RespBean respBean = RespBean.error("没有权限访问");
						out.write(new ObjectMapper().writeValueAsString(respBean));
						out.flush();
						out.close();
					}
				})
				.and()
				.addFilterAt(loginFilter(), UsernamePasswordAuthenticationFilter.class)
		;
	}
}
