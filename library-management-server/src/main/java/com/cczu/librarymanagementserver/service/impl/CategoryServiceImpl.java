package com.cczu.librarymanagementserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Book;
import com.cczu.librarymanagementserver.entity.Category;
import com.cczu.librarymanagementserver.mapper.CategoryMapper;
import com.cczu.librarymanagementserver.service.BookService;
import com.cczu.librarymanagementserver.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	private CategoryMapper categoryMapper;
	@Autowired
	private BookService bookService;
	/**
	 * 自定义XML通过pid查询分类列表 pid 为-1
	 *
	 * @param pid
	 * @return
	 */
	@Override
	public RespBean getCatListsByPid(Integer pid) {

		List<Category> lists = categoryMapper.getCatByPid(pid);
		if (lists.size() == 0) return RespBean.error("分类信息为空", null);
		return RespBean.ok("查询分类信息成功", lists);
	}

	/**
	 * 通过pid查询父节点信息
	 *
	 * @param pid
	 * @return
	 */
	@Override
	public RespBean getCatByPid(Integer pid) {
		Category category = categoryMapper.selectById(pid);

		if (category == null) return RespBean.error("节点信息为空", null);
		return RespBean.ok("查询节点信息成功", category);

	}

	/**
	 * 添加分类
	 *
	 * @param category
	 * @return
	 */
	@Override
	public RespBean addRecord(Category category) {
		// 1.判断该分类的父节点是否是叶子节点
		Category parentCategory = categoryMapper.selectById(category.getParentId());
		if (parentCategory != null) {
			// 为叶子节点
			if (parentCategory.getIsParent() == 0) {
				parentCategory.setIsParent(1);
				categoryMapper.updateById(parentCategory);
			}
			// 为父节点
			int flag = categoryMapper.insert(category);
			if (flag > 0) return RespBean.ok("添加分类成功", category.getCId());
		}
		return RespBean.error("添加分类失败", 0);
	}

	/**
	 * 更新分类信息
	 *
	 * @param category
	 * @return
	 */
	@Override
	public RespBean updateRecord(Category category) {
		int flag = categoryMapper.updateById(category);
		if (flag == 1) return RespBean.ok("更新分类成功", flag);
		return RespBean.error("更新分类失败", 0);
	}

	/**
	 * 删除分类通过id,只能删除叶子节点
	 *
	 * @param id
	 * @return
	 */
	@Override
	public RespBean deleteRecordById(Integer id) {
		List<Book> books = bookService.selectByCid(id);
		if (books.size() > 0) return RespBean.error("无法删除类目，有相应图书使用了该类目",0);
		int flag = categoryMapper.deleteById(id);
		if (flag == 1) return RespBean.ok("删除分类成功", flag);
		return RespBean.error("删除分类失败", 0);
	}

	/**
	 * 获取所有叶子节点
	 *
	 * @return
	 */
	@Override
	public RespBean getLeafCats() {
		QueryWrapper<Category> wrapper = new QueryWrapper<>();
		wrapper.eq("is_parent", 0);
		List<Category> lists = categoryMapper.selectList(wrapper);
		if (lists.size() == 0) return RespBean.error("无叶子节点", lists);
		return RespBean.ok("查询叶子节点成功", lists);
	}

	/**
	 * 添加根目录
	 *
	 * @return
	 */
	@Override
	public RespBean addRootCat(Category category) {
		if (category != null) {
			if (StringUtils.isEmpty(category.getCName())) return RespBean.error("名称不能为空", 0);
			category.setParentId(-1);
			category.setIsParent(0);
			int flag = categoryMapper.insert(category);
			if (flag == 1) return RespBean.ok("添加根节点成功", flag);
		}
		return RespBean.error("添加根节点失败", 0);
	}
}
