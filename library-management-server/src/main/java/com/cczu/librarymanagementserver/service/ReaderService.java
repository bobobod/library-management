package com.cczu.librarymanagementserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Reader;

import javax.servlet.http.HttpServletRequest;


public interface ReaderService extends CommonService<Reader> {


	/**
	 * 根据条件查询读者信息并翻页
	 *
	 * @param page
	 * @param reader
	 * @return
	 */
	RespBean selectByCondition(Page<Reader> page, Reader reader);


	/**
	 * 根据智能卡查用户信息
	 *
	 * @param cardId
	 * @return
	 */
	RespBean selectByCardId(String cardId);

	/**
	 * 根据用户名和密码查询用户信息
	 *
	 * @param username
	 * @param password
	 * @param code
	 * @param req
	 * @return
	 */
	RespBean selectByUsernameAndPassword(String username, String password, String code, HttpServletRequest req);

	/**
	 * 读者进行挂失
	 *
	 * @param reader
	 * @return
	 */
	RespBean readerLost(Reader reader);
}