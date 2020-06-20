package com.cczu.librarymanagementserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cczu.librarymanagementserver.entity.Book;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BookMapper extends BaseMapper<Book> {
}
