package com.cczu.librarymanagementserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cczu.librarymanagementserver.common.RespBean;
import com.cczu.librarymanagementserver.entity.Admin;
import com.cczu.librarymanagementserver.entity.Role;
import com.cczu.librarymanagementserver.mapper.AdminMapper;
import com.cczu.librarymanagementserver.mapper.RoleMapper;
import com.cczu.librarymanagementserver.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Transactional
@Service("adminService")
public class AdminServiceImpl implements AdminService {
	@Autowired
	private AdminMapper adminMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private BCryptPasswordEncoder encoder;

	/**
	 * 通过用户名返回用户信息
	 *
	 * @param username
	 * @return
	 */
	@Override
	public Admin selectByUsername(String username) {
		return adminMapper.selectOne(new QueryWrapper<Admin>().eq("a_name", username));
	}

	/**
	 * 管理员登录服务层实现
	 *
	 * @param aName
	 * @param aPassword
	 * @return
	 */
	@Override
	public RespBean login(String aName, String aPassword, String code, HttpServletRequest request) {
		if (StringUtils.isEmpty(aName) || StringUtils.isEmpty(aPassword) || StringUtils.isEmpty(code))
			return RespBean.error("用户或密码或验证码不能为空");
		String realCode = (String) request.getSession().getAttribute("code");
		if (!realCode.equalsIgnoreCase(code)) return RespBean.error("验证码错误");
		QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("a_name", aName).eq("a_password", aPassword);
		Admin admin = adminMapper.selectOne(queryWrapper);
		if (admin == null) return RespBean.error("不存在该用户");
		return RespBean.ok("管理员登录成功", admin);
	}

	/**
	 * 管理员注册服务层实现
	 *
	 * @param admin
	 * @return
	 */
	@Override
	public RespBean register(Admin admin, String code, String telCode,
							 HttpServletRequest request) {
		if (!StringUtils.isEmpty(admin.getTel())) {
			// 获取缓存的手机验证码和图形验证码
			String realTelCode = (String) request.getSession().
					getAttribute(admin.getTel().toString());
			String realCode = (String) request.getSession().
					getAttribute("code");
			// 比对验证码是否匹配
			if (!realCode.equalsIgnoreCase(code))
				return RespBean.error("图形验证码输入错误");
			if (!realTelCode.equalsIgnoreCase(telCode))
				return RespBean.error("手机验证码输入错误");
			// 加密密码存储
			admin.setAPassword(encoder.encode(admin.getAPassword()));
			int flag = adminMapper.insert(admin);
			if (flag == 1) {
				Role role = new Role();
				role.setName("ROLE_ADMIN");
				role.setRid(admin.getAId());
				roleMapper.insert(role);
				return RespBean.ok("注册成功", flag);
			}
			return RespBean.ok("注册失败", flag);
		}
		return RespBean.error("手机号不能为空");
	}

	/**
	 * 删除管理员
	 *
	 * @param id
	 * @return
	 */
	@Override
	public RespBean deleteById(Integer id) {
		int flag = adminMapper.deleteById(id);
		if (flag == 1) return RespBean.ok("删除成功", flag);
		return RespBean.error("删除失败", flag);
	}

	/**
	 * 修改管理员
	 *
	 * @param admin
	 * @return
	 */
	@Override
	public RespBean updateById(Admin admin) {
		if (StringUtils.isEmpty(admin.getAPassword())) {
			int flag = adminMapper.updatePartial(admin);
			if (flag >= 1) return RespBean.ok("修改成功", flag);
			return RespBean.error("修改失败", flag);
		} else {

			admin.setAPassword(encoder.encode(admin.getAPassword()));
			int flag = adminMapper.updateById(admin);
			if (flag == 1) return RespBean.ok("修改成功", flag);
			return RespBean.error("修改失败", flag);
		}
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// 根据用户名查询数据库
		Admin admin = selectByUsername(username);
		if (admin == null) {
			throw new RuntimeException("用户不存在");
		}
		// 获取该用户的角色信息
		QueryWrapper<Role> wrapper = new QueryWrapper<>();
		wrapper.eq("rid", admin.getAId());
		admin.setRoles(roleMapper.selectList(wrapper));
		return admin;
	}
}