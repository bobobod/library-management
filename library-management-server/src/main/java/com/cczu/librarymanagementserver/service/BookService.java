package com.cczu.librarymanagementserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Book;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookService extends CommonService<Book>{
	/**
	 * 根据条件查询图书并分页
	 *
	 * @param page
	 * @param book
	 * @return
	 */
	RespBean getBookLists(Page<Book> page, Book book);


	/**
	 * 通过卡号查询图书信息
	 *
	 * @param bookId
	 */
	RespBean selectByBookId(Integer bookId);

	/**
	 * 通过目录id查询图书列表
	 *
	 * @param cid
	 * @return
	 */
	List<Book> selectByCid(Integer cid);

	/**
	 * 导入文件信息
	 * 
	 * @param file
	 * @return
	 */
	RespBean importData(MultipartFile file);
}
