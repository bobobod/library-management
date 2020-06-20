package com.cczu.librarymanagementserver.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Borrow;
import com.cczu.librarymanagementserver.service.BorrowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class BorrowServiceImplTest {
	@Autowired
	private BorrowService borrowService;
	@Test
	void getLists() {
		RespBean lists = borrowService.getLists(new Page<Borrow>(), new Borrow(), 0);
		System.out.println(lists.toString());
	}

	@Test
	void selectBorrowInfoByCardIdAndBookId() {
		RespBean respBean = borrowService.selectBorrowInfoByCardIdAndBookId("13432", 1343);
		System.out.println(respBean.toString());
	}

	@Test
	void selectListByCardId() {
	}

	@Test
	void addRecord() {
	}

	@Test
	void updateRecord() {
	}

	@Test
	void deleteRecordById() {
	}
}