package com.cczu.librarymanagementserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cczu.librarymanagementserver.common.RespBean;

/**
 * 通用接口
 *
 * @param <T>
 */
public interface  CommonService<T> {


	/**
	 * 新增新记录
	 *
	 * @param t
	 * @return
	 */
    RespBean addRecord(T t);

	/**
	 * 修改记录
	 *
	 * @param t
	 * @return
	 */
	 RespBean updateRecord(T t);

	/**
	 * 通过id删除指定记录
	 *
	 * @param id
	 * @return
	 */
	 RespBean deleteRecordById(Integer id);

}
