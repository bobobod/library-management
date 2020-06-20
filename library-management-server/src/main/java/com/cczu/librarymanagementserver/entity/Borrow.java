package com.cczu.librarymanagementserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ApiModel("借阅")
@SuppressWarnings("serial")
public class Borrow implements Serializable {
	@TableId(type = IdType.AUTO)
	//借阅编号
	private Integer id;
	//图书卡号
	private Integer bookId;
	//图书名称
	private String bName;
	//图书类目id
	private Integer cId;
	//借阅日期
	private String borrowDate;
	//应还日期
	private String expire;
	//超期时间 默认为0
	private Integer overdue;
	//学生学号
	private Integer sNo;
	//学生姓名
	private String sName;
	//学生卡号
	private String cardId;
	//归还日期
	private String returnDate;
	//状态 0 借出 1 已还
	private Integer status;

}