package com.cczu.librarymanagementserver.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cczu.librarymanagementserver.common.POIUtils;
import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Book;
import com.cczu.librarymanagementserver.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("book")
@Api(tags = "图书")
public class BookController {
	@Autowired
	private BookService bookService;

	/**
	 * 根据条件查询图书列表并分页
	 *
	 * @param page
	 * @param book
	 * @return
	 */
	@ApiOperation("根据条件查询")
	@GetMapping
	public RespBean selectBookLists(Page<Book> page, Book book) {
		return bookService.getBookLists(page, book);
	}

	/**
	 * 插入图书
	 *
	 * @param book
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public RespBean addBook(@RequestBody @Validated Book book) {
		return bookService.addRecord(book);
	}

	/**
	 * 修改图书信息
	 *
	 * @param book
	 * @return
	 */
	@PutMapping
	@PreAuthorize("hasRole('ADMIN')")
	public RespBean updateBook(@RequestBody @Validated Book book) {
		return bookService.updateRecord(book);
	}

	/**
	 * 根据id删除图书
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping
	@PreAuthorize("hasRole('ADMIN')")
	public RespBean deleteByBId(Integer id) {
		return bookService.deleteRecordById(id);
	}

	/**
	 * 根据图书卡查询图书信息
	 *
	 * @param bookId
	 * @return
	 */
	@GetMapping("{bookId}")
	public RespBean selectByBookId(@PathVariable Integer bookId) {
		return bookService.selectByBookId(bookId);
	}

	/**
	 * 导入数据
	 * @param file
	 * @return
	 */
	@PostMapping("import")
	@PreAuthorize("hasRole('ADMIN')")
	public RespBean importData(MultipartFile file){
		return bookService.importData(file);
	}

	/**
	 * 导出数据
	 *
	 * @return
	 */
	@GetMapping("export")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<byte[]> exportData() {
		Page<Book> books = (Page<Book>) bookService.getBookLists(new Page<Book>(), new Book()).getData();
		List<Book> list = books.getRecords();
	return POIUtils.book2Excel(list);

	}
}
