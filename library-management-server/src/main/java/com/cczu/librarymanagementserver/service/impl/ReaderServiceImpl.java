package com.cczu.librarymanagementserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Borrow;
import com.cczu.librarymanagementserver.entity.Reader;
import com.cczu.librarymanagementserver.mapper.BorrowMapper;
import com.cczu.librarymanagementserver.mapper.ReaderMapper;
import com.cczu.librarymanagementserver.service.CardService;
import com.cczu.librarymanagementserver.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Transactional
@Service("readerService")
public class ReaderServiceImpl implements ReaderService {
	@Autowired
	private ReaderMapper readerMapper;

	@Autowired
	private CardService cardService;
	@Autowired
	private BorrowMapper borrowMapper;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	/**
	 * 新增读者
	 *
	 * @param reader
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public RespBean addRecord(Reader reader) {

		if (cardService.write(reader.getCardId())) {
			List<Reader> lists = readerMapper.selectList(new QueryWrapper<Reader>().eq("s_name", reader.getSName()));
			if (lists.size() == 0) {
				reader.setSPassword(bCryptPasswordEncoder.encode(reader.getSPassword()));
				int flag = readerMapper.insert(reader);
				if (flag == 1) return RespBean.ok("新增读者成功", flag);
			} else return RespBean.error("数据库有存在该用户名", 0);
		}
		return RespBean.error("新增读者失败", 0);

	}

	/**
	 * 更新读者信息
	 *
	 * @param reader
	 * @return
	 */
	@Override
	public RespBean updateRecord(Reader reader) {
		// 修改用户信息时同时修改借阅记录信息
		List<Borrow> borrowLists = borrowMapper.selectList(new QueryWrapper<Borrow>()
				.eq("card_id", reader.getCardId()));
		if (borrowLists.size() > 0)
			// 只对读者正在借阅的借阅记录进行修改
			borrowLists.stream().filter(item -> item.getStatus() == 0)
					.forEach(item -> {
				item.setSNo(reader.getSNo());
				item.setSName(reader.getSName());
				borrowMapper.updateById(item);
			});
		// 判断是否填入密码，如果没有填入，则密码部分不变
		if (StringUtils.isEmpty(reader.getSPassword())) {
			// 密码为空的情况下
			int flag = readerMapper.updatePartial(reader);
			if (flag >= 1) return RespBean.ok("更新读者成功", flag);
			return RespBean.error("更新读者失败", 0);
		} else {
			// 密码不为空的情况下
			reader.setSPassword(bCryptPasswordEncoder.encode(reader.getSPassword()));
			int flag = readerMapper.updateById(reader);
			if (flag == 1) return RespBean.ok("更新读者成功", flag);
			return RespBean.error("更新读者失败", 0);
		}
	}

	/**
	 * 根据条件查询读者信息并翻页
	 *
	 * @param page
	 * @param reader
	 * @return
	 */
	@PreAuthorize("hasRole('ADMIN')")
	@Override
	public RespBean selectByCondition(Page<Reader> page, Reader reader) {
		QueryWrapper<Reader> readerQueryWrapper = new QueryWrapper<>();
		if (reader != null) {
			if (!StringUtils.isEmpty(reader.getSNo()))
				readerQueryWrapper.eq("s_no", reader.getSNo());
			if (!StringUtils.isEmpty(reader.getSName()))
				readerQueryWrapper.like("s_name", reader.getSName());
		}
		IPage<Reader> lists = readerMapper.selectPage(page, readerQueryWrapper);
		if (lists.getRecords().size() == 0) return RespBean.error("读者信息为空", lists);
		lists.getRecords().stream().forEach(item -> item.setSPassword(null));
		return RespBean.ok("查询读者信息成功", lists);
	}

	/**
	 * 通过id删除读者信息
	 *
	 * @param id
	 * @return
	 */
	@Override
	public RespBean deleteRecordById(Integer id) {
		Reader reader = readerMapper.selectById(id);
		List<Borrow> borrowLists = borrowMapper.selectList(new QueryWrapper<Borrow>().eq("card_id", reader.getCardId()).eq("status",0));
		// 存在出借记录无法删除
		if (borrowLists.size() > 0) return RespBean.error("存在出借记录无法删除",0);
		int flag = readerMapper.deleteById(id);
		if (flag == 1) return RespBean.ok("删除读者成功", flag);
		return RespBean.error("删除读者失败", 0);
	}

	/**
	 * 根据智能卡查用户信息
	 *
	 * @param cardId
	 * @return
	 */
	@Override
	public RespBean selectByCardId(String cardId) {
		QueryWrapper<Reader> queryWrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(cardId)) {
			queryWrapper.eq("card_id", cardId);
		}
		Reader reader = readerMapper.selectOne(queryWrapper);
		if (reader == null) return RespBean.error("无该读者", null);
		reader.setSPassword(null);
		return RespBean.ok("查询读者成功", reader);
	}

	/**
	 * 根据用户名和密码查询用户
	 *
	 * @param username
	 * @param password
	 * @param code
	 * @param req
	 * @return
	 */
	@Override
	public RespBean selectByUsernameAndPassword(String username, String password, String code, HttpServletRequest req) {
		String realCode = (String) req.getSession().getAttribute("code");
		if (!realCode.equalsIgnoreCase(code)){
			return RespBean.error("验证码不匹配");
		}
		QueryWrapper<Reader> wrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
			wrapper.eq("s_name", username);
			Reader reader = readerMapper.selectOne(wrapper);
			if (reader == null) return RespBean.error("不存在该用户");
			else {
				if (matchPassword(password, reader.getSPassword())) {
					reader.setSPassword(null);
					return RespBean.ok("查询成功", reader);

				} else return RespBean.error("密码不匹配");
			}
		}
		return RespBean.error("用户名或密码不能为空");
	}

	/**
	 * 读者自行挂失
	 *
	 * @param reader
	 * @return
	 */
	@Override
	public RespBean readerLost(Reader reader) {
		if (reader != null) {
			reader.setStatus(1);

			int flag = readerMapper.updateById(reader);
			if (flag == 1) return RespBean.ok("挂失成功");
			return RespBean.error("挂失失败");
		}
		return RespBean.error("读者信息为空");
	}

	public Boolean matchPassword(String password, String encodePassword) {
		return bCryptPasswordEncoder.matches(password, encodePassword);
	}
}
