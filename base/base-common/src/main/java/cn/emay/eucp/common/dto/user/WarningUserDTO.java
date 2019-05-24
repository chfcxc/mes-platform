package cn.emay.eucp.common.dto.user;

import java.io.Serializable;

/**
 * 预警用户信息
 * 
 * @author xingyuwei
 *
 */
public class WarningUserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long serviceCodeId;// 特服号id
	private String username;// 用户名
	private String realname;// 真实姓名
	private String mobile;// 手机号
	private String email;// 邮箱
	private Integer state;// 状态[0-删除，1-停用，2-启用]

	public WarningUserDTO() {
	}

	public WarningUserDTO(Long id, String username, String realname, String mobile, String email) {
		this.id = id;
		this.username = username;
		this.realname = realname;
		this.mobile = mobile;
		this.email = email;
	}

	public WarningUserDTO(String username, String mobile, String email, Integer state) {
		this.username = username;
		this.mobile = mobile;
		this.email = email;
		this.state = state;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getServiceCodeId() {
		return serviceCodeId;
	}

	public void setServiceCodeId(Long serviceCodeId) {
		this.serviceCodeId = serviceCodeId;
	}

}
