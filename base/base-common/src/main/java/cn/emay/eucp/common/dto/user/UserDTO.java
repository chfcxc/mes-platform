package cn.emay.eucp.common.dto.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.emay.eucp.common.moudle.db.system.Department;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.moudle.db.system.UserRoleAssign;

/**
 * 用户信息
 * 
 * @author lijunjian
 *
 */
public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final int USER_IDENTITY_MANAGE = 1;
	public static final int USER_IDENTITY_ORDINARY = 2;

	private Long id;
	private String username;// 用户名
	private String realname;// 真实姓名
	private String mobile;// 手机号
	private String email;// 邮箱
	private Date createTime;// 创建时间
	private Integer state;// 状态[0-删除，1-停用，2-启用]
	private String remark;// 说明
	private Long operatorId;// 创建者
	private Date lastChangePasswordTime;// 最后一次修改密码时间[用来判断登陆修改密码]
	private String rolename;// 角色名
	private String department;// 部门名
	private String parentDepartment;// 上级部门名
	private Long departmentId;// 部门id
	private String serviceModules;// 客户账号服务模块
	private Integer identity;// 账号类型[1-管理账号,2-普通账号]
	private String clientName;// 客户名称
	private String clientNumber;// 客户编号
	private String userType;
	private String type;//企业类型
	
	/**
	 * 角色列表
	 */
	private List<Long> roles = new ArrayList<Long>();

	public UserDTO() {

	}

	public UserDTO(Long id, String username, String realname, String mobile, String email, Date createTime, Integer state, 
			String rolename, String department,String userType,Integer identity) {
		this.id = id;
		this.username = username;
		this.realname = realname;
		this.mobile = mobile;
		this.email = email;
		this.createTime = createTime;
		this.state = state;
		this.rolename = rolename;
		this.department = department;
		this.identity = identity;
		this.userType = userType;
	}

	public UserDTO(User user, List<UserRoleAssign> userroles,Integer identity) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.realname = user.getRealname();
		this.mobile = user.getMobile();
		this.email = user.getEmail();
		this.createTime = user.getCreateTime();
		this.state = user.getState();
		this.remark = user.getRemark();
		this.operatorId = user.getOperatorId();
		this.userType = user.getUserType();
		this.lastChangePasswordTime = user.getLastChangePasswordTime();
		for (UserRoleAssign ur : userroles) {
			roles.add(ur.getRoleId());
		}
		this.identity=identity;
	}

	public UserDTO(Long userId, String username, Integer state, Integer identity, String serviceModules,String userType) {
		this.id = userId;
		this.username = username;
		this.state = state;
		this.identity = identity;
		this.serviceModules = serviceModules;
		this.userType = userType;
	}

	public UserDTO(User user, Department department, Department parmentDepmant) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.realname = user.getRealname();
		this.mobile = user.getMobile();
		this.email = user.getEmail();
		this.createTime = user.getCreateTime();
		this.state = user.getState();
		this.remark = user.getRemark();
		this.operatorId = user.getOperatorId();
		this.lastChangePasswordTime = user.getLastChangePasswordTime();
		this.department = department.getDepartmentName();
		if (parmentDepmant != null) {
			this.parentDepartment = parmentDepmant.getDepartmentName();
		} else {
			this.parentDepartment = "";
		}
		this.departmentId = department.getId();
		this.userType = user.getUserType();
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Date getLastChangePasswordTime() {
		return lastChangePasswordTime;
	}

	public void setLastChangePasswordTime(Date lastChangePasswordTime) {
		this.lastChangePasswordTime = lastChangePasswordTime;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getParentDepartment() {
		return parentDepartment;
	}

	public void setParentDepartment(String parentDepartment) {
		this.parentDepartment = parentDepartment;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getServiceModules() {
		return serviceModules;
	}

	public void setServiceModules(String serviceModules) {
		this.serviceModules = serviceModules;
	}

	public Integer getIdentity() {
		return identity;
	}

	public void setIdentity(Integer identity) {
		this.identity = identity;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
