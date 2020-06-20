package com.cczu.librarymanagementserver.service;

import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Category;


public interface CategoryService extends CommonService<Category>{
	/**
	 * 自定义XML通过pid查询分类列表 pid 为-1
	 *
	 * @param pid
	 * @return
	 */
	RespBean getCatListsByPid(Integer pid);

	/**
	 * 通过pid查询父节点信息
	 *
	 * @param pid
	 * @return
	 */
	RespBean getCatByPid(Integer pid);



	/**
	 * 获取所有叶子节点
	 *
	 * @return
	 */
	RespBean getLeafCats();

	/**
	 * 添加根节点
	 *
	 * @return
	 */
	RespBean addRootCat(Category category);
}
