package cn.emay.eucp.common.dto.user;

import java.io.Serializable;

public class UserRoleDTO implements Serializable{
	private static final long serialVersionUID=1l;
	private Long id;
	private String username;// 用户名
	private String realname;// 真实姓名
	private String mobile;// 手机号
	private String email;// 邮箱
	private String roleName;// 角色名
	private Long roleid;
	private String rid;
	private String remark;
	

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public UserRoleDTO() {
		super();
	}
	
	
	public UserRoleDTO(Long id, String username, String realname, String mobile, String email, String roleName, Long roleid,String rid,String remark) {
		super();
		this.id = id;
		this.username = username;
		this.realname = realname;
		this.mobile = mobile;
		this.email = email;
		this.roleName = roleName;
		this.roleid = roleid;
		this.rid = rid;
		this.remark = remark;
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
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public Long getRoleid() {
		return roleid;
	}
	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}
