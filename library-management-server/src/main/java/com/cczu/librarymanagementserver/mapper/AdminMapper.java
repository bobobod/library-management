package com.cczu.librarymanagementserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cczu.librarymanagementserver.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
	/**
	 * 如果密码为空表示不更新密码
	 *
	 * @param admin
	 * @return
	 */
	int updatePartial(Admin admin);
}
