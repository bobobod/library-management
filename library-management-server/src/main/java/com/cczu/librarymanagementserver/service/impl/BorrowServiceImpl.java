package com.cczu.librarymanagementserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Book;
import com.cczu.librarymanagementserver.entity.Borrow;
import com.cczu.librarymanagementserver.entity.Reader;
import com.cczu.librarymanagementserver.mapper.BorrowMapper;
import com.cczu.librarymanagementserver.service.BookService;
import com.cczu.librarymanagementserver.service.BorrowService;
import com.cczu.librarymanagementserver.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {
	@Autowired
	private BorrowMapper borrowMapper;
	@Autowired
	private BookService bookService;
	@Autowired
	private ReaderService readerService;

	/**
	 * 根据特定条件查询所有所有记录并分页
	 *
	 * @param page   获取分页对象
	 * @param borrow 获取借阅对象
	 * @param flag   标志位 3 表示 查询已借 1 表示 查询已还 2 表示 查询所有
	 * @return
	 */
	@Override
	public RespBean getLists(Page<Borrow> page, Borrow borrow, Integer flag) {
		QueryWrapper<Borrow> wrapper = new QueryWrapper<>();
		// 查询匹配的学号和姓名的借阅信息
		if (!(borrow == null)) {
			if (!StringUtils.isEmpty(borrow.getSNo())) wrapper.eq("s_no", borrow.getSNo());
			if (!StringUtils.isEmpty(borrow.getSName())) wrapper.like("s_name", borrow.getSName());
		}
		IPage<Borrow> lists = null;
		// 根据不同的条件查询相应的借阅信息
		if (flag == 3) {
			wrapper.eq("status", 0);
			lists = borrowMapper.selectPage(page, wrapper);
		} else if (flag == 1) {
			wrapper.eq("status", flag);
			lists = borrowMapper.selectPage(page, wrapper);
		} else lists = borrowMapper.selectPage(page, wrapper);

		if (lists.getRecords().size() == 0) return RespBean.error("借阅信息为空", lists);
		return RespBean.ok("查询借阅信息成功", lists);
	}


	/**
	 * 根据图书卡和借阅卡查询信息
	 *
	 * @param cardId
	 * @param bookId
	 * @return
	 */
	@Override
	public RespBean selectBorrowInfoByCardIdAndBookId(String cardId, Integer bookId) {
		QueryWrapper<Borrow> wrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(cardId) && bookId != null) {
			wrapper.eq("card_id", cardId).eq("book_id", bookId).eq("status", 0);
			Borrow borrow = borrowMapper.selectOne(wrapper);
			if (borrow == null) return RespBean.error("无该信息", null);
			else if (borrow.getStatus() == 1) return RespBean.error("该图书已还", null);
			return RespBean.ok("查询借阅信息成功", borrow);
		}
		return RespBean.error("查询借阅信息失败", null);
	}

	/**
	 * 通过智能卡查询读者借阅记录
	 *
	 * @param cardId
	 * @return
	 */
	@Override
	public RespBean selectListByCardId(String cardId) {
		if (!StringUtils.isEmpty(cardId)) {
			QueryWrapper<Borrow> wrapper = new QueryWrapper<>();
			wrapper.eq("card_id", cardId);
			List<Borrow> lists = borrowMapper.selectList(wrapper);
			if (lists.size() == 0) return RespBean.error("该用户没有借阅记录");
			return RespBean.ok("查询用户借阅记录成功", lists);
		}
		return RespBean.error("智能卡为空");
	}

	/**
	 * 添加借阅记录
	 *
	 * @param borrow
	 * @return
	 */
	@Override
	public RespBean addRecord(Borrow borrow) {
		Reader reader = (Reader) readerService.selectByCardId(borrow.getCardId()).getData();
		// 判断读者卡的状态和余额
		if (reader.getStatus() == 0 && reader.getBalance() >= 0) {
			Book book = (Book) bookService.selectByBookId(borrow.getBookId()).getData();
			if (book != null) {
				if (book.getStatus() == 1) return RespBean.error("图书正在出借无法借阅", 0);
				book.setStatus(1);
				System.out.println(book);
				Integer flag = (Integer) bookService.updateRecord(book).getData();
				if (flag == 1) {
					int flag2 = borrowMapper.insert(borrow);
					if (flag2 == 1) return RespBean.ok("添加借阅记录成功", flag2);
				}
			}
		} else if (reader.getStatus() == 1) {
			return RespBean.error("读者处于挂失状态无法借阅", 0);
		} else if (reader.getBalance() < 0) return RespBean.error("读者账户余额不住无法扣费", 0);
		return RespBean.error("添加借阅记录失败", 0);
	}

	/**
	 * 修改借阅记录
	 *
	 * @param borrow
	 * @return
	 */
	@Override
	public RespBean updateRecord(Borrow borrow) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Integer flag1 = 1; // 默认为1 标注readerService更新成功与否
		Integer flag2 = 1; // 默认为1 标注bookservice是否更新成功
		try {
			String expire = borrow.getExpire();
			String returnDate = borrow.getReturnDate();
			Date d1 = df.parse(returnDate);
			Date d2 = df.parse(expire);
			long diff = d1.getTime() - d2.getTime();
			System.out.println(diff);
			borrow.setOverdue(0);
			if (diff > 0) {
				// 逾期更新读者状态
				Integer overdue = (int) (diff / (1000 * 60 * 60 * 24));
				borrow.setOverdue(overdue);
				Reader reader = (Reader) readerService.selectByCardId(borrow.getCardId()).getData();
				// 逾期进行扣费，每超过一天扣一元
				reader.setBalance(reader.getBalance() - overdue);
				flag1 = (Integer) readerService.updateRecord(reader).getData();
			}
		} catch (ParseException e) {
			System.out.println("转换异常");
			e.printStackTrace();
		}
		// 更新图书状态
		if (flag1 == 1) {
			Book book = (Book) bookService.selectByBookId(borrow.getBookId()).getData();
			book.setStatus(0);
			RespBean respBean = bookService.updateRecord(book);
			flag2 = (Integer) respBean.getData();
		}
		if (flag2 == 1) {
			int flag3 = borrowMapper.updateById(borrow);
			if (flag3 == 1) return RespBean.ok("更新借阅信息成功", flag3);
		}
		return RespBean.error("更新借阅信息失败", 0);
	}

	/**
	 * 根据id删除借阅记录
	 *
	 * @param id
	 * @return
	 */
	@Override
	public RespBean deleteRecordById(Integer id) {

		int flag = borrowMapper.deleteById(id);
		if (flag == 1) return RespBean.ok("删除借阅记录成功", flag);
		return RespBean.error("删除借阅记录失败", flag);
	}

}

