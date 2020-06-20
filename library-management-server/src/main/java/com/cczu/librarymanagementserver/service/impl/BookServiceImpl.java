package com.cczu.librarymanagementserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cczu.librarymanagementserver.common.POIUtils;
import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Book;
import com.cczu.librarymanagementserver.entity.Borrow;
import com.cczu.librarymanagementserver.mapper.BookMapper;
import com.cczu.librarymanagementserver.mapper.BorrowMapper;
import com.cczu.librarymanagementserver.service.BookService;
import com.cczu.librarymanagementserver.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@Transactional
public class BookServiceImpl implements BookService {
	@Autowired
	private BookMapper bookMapper;
	@Autowired
	private CardService cardService;
	@Autowired
	private BorrowMapper borrowMapper;

	/**
	 * 根据条件查询图书并分页
	 *
	 * @param page
	 * @param book
	 * @return
	 */
	@Override
	public RespBean getBookLists(Page<Book> page, Book book) {
		QueryWrapper<Book> wrapper = new QueryWrapper<>();
		if (book != null) {
			if (!StringUtils.isEmpty(book.getBName())) wrapper.like("b_name", book.getBName());
			if (!StringUtils.isEmpty(book.getWriter())) wrapper.like("writer", book.getWriter());
			if (!StringUtils.isEmpty(book.getCId())) wrapper.eq("c_id", book.getCId());
		}
		IPage<Book> lists = bookMapper.selectPage(page, wrapper);
		if (lists.getRecords().size() == 0) return RespBean.error("查询图书信息失败", lists);
		return RespBean.ok("查询图书信息成功", lists);
	}


	/**
	 * 根据卡号查询图书
	 *
	 * @param bookId
	 * @return
	 */
	@Override
	public RespBean selectByBookId(Integer bookId) {
		QueryWrapper<Book> wrapper = new QueryWrapper<>();
		if (bookId != null) {
			wrapper.eq("book_id", bookId);
			Book book = bookMapper.selectOne(wrapper);

			if (book != null) return RespBean.ok("查询图书成功", book);
		}
		return RespBean.error("查询图书失败", null);
	}

	/**
	 * 添加图书
	 *
	 * @param book
	 * @return
	 */
	public RespBean addRecord(Book book) {
		Integer bookId = book.getBookId();
		if (cardService.write(bookId)) {
			int flag = bookMapper.insert(book);
			if (flag == 1) return RespBean.ok("添加图书成功", 1);
			return RespBean.error("添加图书失败", 0);
		}
		return RespBean.error("读写器写入图书卡失败", 0);

	}

	;

	/**
	 * 修改图书
	 *
	 * @param book
	 * @return
	 */
	public RespBean updateRecord(Book book) {
		// 修改图书前先查找是否有相应的借阅信息，如果有修改借阅信息
		List<Borrow> borrowLists = borrowMapper.selectList(new QueryWrapper<Borrow>().eq("book_id", book.getBookId()));
		if (borrowLists.size() > 0) borrowLists.stream().filter(item -> item.getStatus() == 0).forEach(item -> {
			item.setBName(book.getBName());
			item.setCId(book.getCId());
			borrowMapper.updateById(item);
		});
		int flag = bookMapper.updateById(book);
		if (flag == 1) return RespBean.ok("更新图书成功", flag);
		return RespBean.error("更新图书失败", flag);
	}

	/**
	 * 通过目录id查询图书列表
	 *
	 * @param cid
	 * @return
	 */
	public List<Book> selectByCid(Integer cid) {
		return bookMapper.selectList(new QueryWrapper<Book>().eq("c_id", cid));
	}

	/**
	 * 导入Excel文件信息
	 *
	 * @param file
	 * @return
	 */
	@Override
	public RespBean importData(MultipartFile file) {
		List<Book> books = POIUtils.excel2book(file);
		System.out.println("导入的数据");
		System.out.println(books.toString());


		if(books.size()>0){
			books.forEach(item -> {
				bookMapper.insert(item);
			});
			return RespBean.ok("导入成功");
		}
		return RespBean.error("导入失败");
	}

	/**
	 * 通过id删除指定记录
	 *
	 * @param id
	 * @return
	 */
	public RespBean deleteRecordById(Integer id) {
		Book book = bookMapper.selectById(id);
		List<Borrow> borrowList = borrowMapper.selectList(new QueryWrapper<Borrow>().eq("book_id", book.getBookId()).eq("status", 0));
		if (borrowList != null && borrowList.size() > 0) return RespBean.error("该图书在处于出借状态，不可删除", 0);
		int flag = bookMapper.deleteById(id);
		if (flag == 1) return RespBean.ok("删除图书成功", flag);
		return RespBean.error("删除图书失败", flag);
	}


}
