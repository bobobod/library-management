package com.cczu.librarymanagementserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiIgnore
public class Role implements Serializable {
	@TableId(type = IdType.AUTO)
	private Integer id;
	private String name;
	private Integer rid;
}
