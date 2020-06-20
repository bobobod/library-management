package com.cczu.librarymanagementserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Borrow;
import com.cczu.librarymanagementserver.service.BorrowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("borrow")
@Api(tags = "借阅")
public class BorrowController {
	@Autowired
	private BorrowService borrowService;
	/**
	 * 根据特定条件查询所有所有记录并分页
	 *
	 * @param page   获取分页对象
	 * @param borrow 获取借阅对象
	 * @param flag   标志位 0 表示 查询已借 1 表示 查询已还 2 表示 查询所有
	 * @return
	 */
	@ApiOperation("获得借阅信息")
	@GetMapping
	public RespBean getLists(Page<Borrow> page, Borrow borrow, Integer flag){

		return borrowService.getLists(page, borrow, flag);

	};
	/**
	 * 根据借阅id删除记录
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping
	@PreAuthorize("hasRole('ADMIN')")
	public RespBean deleteRecordById(Integer id){
		return borrowService.deleteRecordById(id);

	}

	/**
	 * 插入借阅记录
	 *
	 * @param borrow
	 * @return
	 */
	@PostMapping
	public RespBean addRecord(@RequestBody Borrow borrow){
		return borrowService.addRecord(borrow);

	}

	/**
	 * 更新借阅记录
	 *
	 * @param borrow
	 * @return
	 */
	@PutMapping
	public RespBean updateRecord(@RequestBody Borrow borrow){
		return borrowService.updateRecord(borrow);

	}

	/**
	 * 根据图书卡和借阅卡查询借阅信息
	 *
	 * @param rid
	 * @param bid
	 * @return
	 */
	@GetMapping("info")
	public RespBean selectByCondition(@RequestParam String rid,@RequestParam Integer bid){
		return borrowService.selectBorrowInfoByCardIdAndBookId(rid, bid);
	}

	/**
	 * 通过智能卡查询用户借阅信息
	 *
	 * @param cardId
	 * @return
	 */
	@GetMapping("{cardId}")
	public RespBean selectListsByCardId(@PathVariable String cardId){
		return borrowService.selectListByCardId(cardId);
	}
}
