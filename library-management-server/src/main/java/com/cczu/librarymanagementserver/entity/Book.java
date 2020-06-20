package com.cczu.librarymanagementserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Data
@NoArgsConstructor
@ApiModel("图书实体")
@SuppressWarnings("serial")
public class Book implements Serializable {
	@TableId(type = IdType.AUTO)
	//图书编号
	private Integer bId;
	//图书卡号
	@NotNull(message = "图书卡号不能为空")
	private Integer bookId;
	//图书名称
	@NotBlank(message = "图书名不能为空")
	private String bName;
	//作者
	@NotBlank(message = "作者不能为空")
	private String writer;
	//出版社名称
	@NotBlank(message = "出版社不能为空")
	private String press;
	//图书类目id
	@NotNull(message = "类别ID不能为空")
	private Integer cId;
	//图书价格
	@NotNull(message = "价格不能为空")
	private Double price;
	//出版日期
	@NotBlank(message = "出版日期不能为空")
	private String issue;
	//图书状态 0 可借 1 不可借
	private Integer status;
	// 图书所在位置 书柜号/行号/编号
	@NotBlank(message = "图书名不能为空")
	@Pattern(regexp = "[0-9]{2}/[0-9]{2}/[0-9]{2}", message = "图书位置格式为xx/xx/xx")
	private String position;

}