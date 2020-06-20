package com.cczu.librarymanagementserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel("类别实体")
@SuppressWarnings("serial")
public class Category implements Serializable {
	@TableId(type = IdType.AUTO)
	//类目ID
	private Integer cId;
	//父类目ID -1 为根节点
	private Integer parentId;
	//类目名称
    @NotBlank(message = "类目名称不能为空")
	private String cName;
	//该类目是否为父类目，1为true，0为false
	private Integer isParent;
	// 子节点
	@TableField(exist = false)
	private List<Category> children;

}