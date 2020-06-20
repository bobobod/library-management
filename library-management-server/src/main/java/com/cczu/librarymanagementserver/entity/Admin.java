package com.cczu.librarymanagementserver.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
@ApiModel("管理员实体")
@Data
@NoArgsConstructor
@SuppressWarnings("serial")
public class Admin implements Serializable, UserDetails {
	//管理员编号
	@TableId(type = IdType.AUTO)
	private Integer aId;
	//管理员工号
	@NotNull(message = "工号不能为空")
	private Integer aNo;
	//管理员姓名
	@NotBlank(message = "名字不能为空")
	private String aName;
	//管理员密码
	private String aPassword;
	//管理员电话
	@NotBlank(message = "手机号不能为空")
	@Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
	private String tel;
	//管理员住址
	@NotBlank(message = "地址不能为空")
	private String address;
	// 权限
	@TableField(exist = false)
	private List<Role> roles;

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>(roles.size());
		roles.forEach(role -> {
			SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(role.getName());
			authorities.add(simpleGrantedAuthority);
		});

		return authorities;
	}

	@JsonIgnore
	@Override
	public String getPassword() {
		return getAPassword();
	}

	@JsonIgnore
	@Override
	public String getUsername() {
		return getAName();
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
}