package com.cczu.librarymanagementserver.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import springfox.documentation.annotations.ApiIgnore;

import java.io.Serializable;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ApiIgnore
public class RespBean implements Serializable {
	private Integer code;
	private String msg;
	private Object data;
	public static RespBean build(){
		return new RespBean();
	}
	public static RespBean ok(String msg){
		return new RespBean(StatusEnum.OK.getCode(),msg,null);
	}

	public static RespBean ok(String msg,Object data){
		return new RespBean(StatusEnum.OK.getCode(),msg,data);
	}
	public static RespBean error(String msg){
		return new RespBean(StatusEnum.ERROR.getCode(),msg,null);
	}

	public static RespBean error(String msg,Object data){
		return new RespBean(StatusEnum.ERROR.getCode(),msg,data);
	}

}
