package cn.emay.eucp.common.dto.user;

import java.io.Serializable;

/**
 * 简单用户信息dto
 *
 */
public class SimpleUserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String username;// 用户名
	private String realname;// 真实姓名
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	
}
