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
@ApiModel("读者实体")
@SuppressWarnings("serial")
public class Reader implements Serializable {
	@TableId(type = IdType.AUTO)
	//学生编号
	private Integer sId;
	//学生学号
    @NotNull(message = "学号不能为空")
	private Integer sNo;
	//学生姓名
    @NotBlank(message = "姓名不能为空")
	private String sName;
	//学生密码
	private String sPassword;
	//学生性别 0 男 1 女
    @NotNull(message = "性别不能为空")
	private Integer sex;
	//学生电话
	@NotBlank(message = "手机号不能为空")
	@Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
	private String tel;
	//读者智能卡号
    @NotBlank(message = "智能卡卡号不能为空")
	private String cardId;
	//余额
	private Double balance;
	//读者状态 0 正常 1 挂失
	private Integer status;

}