package com.cczu.librarymanagementserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Reader;
import com.cczu.librarymanagementserver.service.ReaderService;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("reader")
@CrossOrigin(origins = "*")
@Api( tags = "读者")
public class ReaderController {
	@Resource
	private ReaderService readerService;

	/**
	 * 根据条件查询并且分页
	 *
	 * @param page
	 * @param reader
	 * @return
	 */
	@GetMapping
	public RespBean selectByCondition(Page<Reader> page, Reader reader) {
		return readerService.selectByCondition(page, reader);

	}

	/**
	 * 新增读者
	 *
	 * @param reader
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public RespBean insert(@RequestBody @Validated Reader reader) {
		return readerService.addRecord(reader);
	}

	/**
	 * 修改读者信息
	 *
	 * @param reader
	 * @return
	 */
	@PutMapping
	public RespBean update(@RequestBody @Validated Reader reader) {
		return readerService.updateRecord(reader);

	}

	/**
	 * 删除读者信息
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public RespBean deleteById(@RequestParam("id") Integer id) {
		return readerService.deleteRecordById(id);

	}

	/**
	 * 根据智能卡查用户信息
	 *
	 * @param cardId
	 * @return
	 */
	@GetMapping("{cardId}")
	public RespBean selectByCardId(@PathVariable String cardId) {
		return readerService.selectByCardId(cardId);

	}

	/**
	 * 通过username和password查询用户信息
	 *
	 * @return
	 * @Param reader
	 */
	@PostMapping("login")
	public RespBean selectByUsernameAndPassword(@RequestBody Map<String, String> map, HttpServletRequest req) {
		return readerService.selectByUsernameAndPassword(map.get("username"), map.get("password"),map.get("code"),req);
	}

	/**
	 * 读者自行挂失
	 *
	 * @param reader
	 * @return
	 */
	@PutMapping("lost")
	public RespBean readerLost(@RequestBody Reader reader) {
		return readerService.readerLost(reader);
	}
}
