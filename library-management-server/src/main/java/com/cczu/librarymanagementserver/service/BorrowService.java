package com.cczu.librarymanagementserver.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Book;
import com.cczu.librarymanagementserver.entity.Borrow;


public interface BorrowService extends CommonService<Borrow>{
	/**
	 * 根据特定条件查询所有所有记录并分页
	 *
	 * @param page   获取分页对象
	 * @param borrow 获取借阅对象
	 * @param flag   标志位 0 表示 查询已借 1 表示 查询已还 2 表示 查询所有
	 * @return
	 */
	RespBean getLists(Page<Borrow> page, Borrow borrow, Integer flag);



	/**
	 * 根据图书卡和智能卡查询借阅信息
	 *
	 * @param cardId
	 * @param bookId
	 * @return
	 */
	RespBean selectBorrowInfoByCardIdAndBookId(String cardId , Integer bookId);

	/**
	 * 根据智能卡查询借阅信息列表
	 *
	 * @param cardId
	 * @return
	 */
	RespBean selectListByCardId(String cardId);


}