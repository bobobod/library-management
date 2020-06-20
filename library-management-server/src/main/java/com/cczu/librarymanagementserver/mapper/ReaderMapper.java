package com.cczu.librarymanagementserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cczu.librarymanagementserver.entity.Reader;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReaderMapper extends BaseMapper<Reader> {
	/**
	 * 修改部分片段，不包括密码部分和智能卡部分
	 *
	 * @param reader
	 * @return
	 */
	int updatePartial(Reader reader);
}
