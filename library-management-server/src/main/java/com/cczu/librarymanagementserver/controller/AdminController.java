package com.cczu.librarymanagementserver.controller;

import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Admin;
import com.cczu.librarymanagementserver.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("admin")
@CrossOrigin(origins = "*")
@Api(tags= "管理员")
public class AdminController {
	/**
	 * 服务对象
	 */
	@Autowired
	private AdminService adminService;

	/**
	 * 管理员登录控制层实现
	 *
	 * @param loginMap
	 * @return
	 */
	@PostMapping("login")
	public RespBean login(@RequestBody Map<String,String> loginMap, HttpServletRequest request) {
		String username = loginMap.get("aname");
		String password = loginMap.get("apassword");
		String code = loginMap.get("code");
		return adminService.login(username,password,code,request);
	}

	/**
	 * 管理员注册控制层实现
	 *
	 * @param registerMap
	 * @param request
	 * @return
	 */
	@ApiOperation("管理员注册")
	@PostMapping("register")
	public RespBean register(@RequestBody Map<String,String> registerMap,HttpServletRequest request)
	{
		String aname = registerMap.get("aname");
		String ano = registerMap.get("ano");
		String apassword = registerMap.get("apassword");
		String code = registerMap.get("code");
		String address = registerMap.get("address");
		String tel = registerMap.get("tel");
		String telCode = registerMap.get("telCode");
		Admin admin = new Admin();
		admin.setAName(aname);
		admin.setAPassword(apassword);
		admin.setAddress(address);
		admin.setTel(tel);
		admin.setANo(Integer.parseInt(ano));
		return adminService.register(admin,code,telCode,request);
	}

	/**
	 * 删除管理员
	 * @param id
	 * @return
	 */
	@DeleteMapping
	@PreAuthorize("hasRole('ADMIN')")
	public RespBean deleteById(@RequestParam Integer id){
		return adminService.deleteById(id);
	}

	/**
	 * 修改管理员
	 *
	 * @param admin
	 * @return
	 */
	@PutMapping
	@PreAuthorize("hasRole('ADMIN')")
	public RespBean updateAdmin(@RequestBody @Validated Admin admin){
		return adminService.updateById(admin);
	}

}