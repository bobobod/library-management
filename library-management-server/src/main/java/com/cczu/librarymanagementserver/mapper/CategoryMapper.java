package com.cczu.librarymanagementserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cczu.librarymanagementserver.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
	/**
	 * 自定义通过pid查询分类列表 pid 为-1
	 *
	 * @param pid
	 * @return
	 */
	List<Category> getCatByPid(Integer pid);

}
