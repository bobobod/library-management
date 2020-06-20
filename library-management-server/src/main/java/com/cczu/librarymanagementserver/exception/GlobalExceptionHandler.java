package com.cczu.librarymanagementserver.exception;

import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.common.StatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	@ExceptionHandler(NullPointerException.class)
	public RespBean handle(NullPointerException e) {
		logger.error(StatusEnum.NULLPOINTER.getDesc());
		return RespBean.error(StatusEnum.NULLPOINTER.getDesc());
	}

	@ExceptionHandler(SQLSyntaxErrorException.class)
	public RespBean handle(SQLSyntaxErrorException e) {
		logger.error("SQL语法错误");
		return RespBean.error("数据库语法错误");
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public RespBean handle(SQLIntegrityConstraintViolationException e) {
		logger.error("字段存在约束");
		return RespBean.error("该字段存在外键约束，无法删除");
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public RespBean handle(MethodArgumentNotValidException e) {
		logger.error(e.getMessage(),e);
		return RespBean.error(e.getBindingResult().getFieldError().getDefaultMessage());
	}
	@ExceptionHandler(ConstraintViolationException.class)
	public RespBean handleConstraintViolationException(ConstraintViolationException e) {
		logger.error(e.getMessage(),e);
		return RespBean.error( e.getMessage());
	}

}
