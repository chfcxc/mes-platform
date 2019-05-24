package cn.emay.eucp.common.dto.user;

import java.io.Serializable;

/**
 * 用户信息
 * 
 * @author xingyuwei
 *
 */
public class TurnOperatorUserDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;// 用户名
	private String realname;// 真实姓名
	private Integer state;// 状态[0-删除，1-停用，2-启用,3-锁定]
	private String mobile;
	private String email;

	public TurnOperatorUserDTO() {
		super();
	}

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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	

}
