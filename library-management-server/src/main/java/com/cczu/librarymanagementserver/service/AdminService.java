package com.cczu.librarymanagementserver.service;

import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Admin;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;


public interface AdminService extends UserDetailsService {
	Admin selectByUsername(String username);
	/**
	 * 管理员登录模块
	 *
	 * @param aName
	 * @param aPassword
	 * @return
	 */
	RespBean login(String aName, String aPassword, String code , HttpServletRequest request);

	/**
	 * 管理员注册模块
	 *
	 * @param admin
	 * @param code
	 * @param telCode
	 * @return
	 */
	RespBean register(Admin admin,String code,String telCode,HttpServletRequest request);

	/**
	 * 删除管理员
	 *
	 * @param id
	 * @return
	 */
	RespBean deleteById(Integer id);

	/**
	 * 修改管理员
	 *
	 * @param admin
	 * @return
	 */
	RespBean updateById(Admin admin);


}