package cn.emay.eucp.common.dto.user;

import java.io.Serializable;
import java.util.Date;

import cn.emay.eucp.common.moudle.db.system.Role;

public class RoleDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String roleName;// 角色名
	private Date createTime;// 创建时间
	private Boolean isDelete;// 是否删除
	private String remark;// 备注
	private String roleType;// 角色所属系统 GlobalConstants.SYSTEM_*
	
	public RoleDTO(){
		
	}
	
	public RoleDTO(Role role){
		this.id = role.getId();
		this.roleName = role.getRoleName();
		this.createTime = role.getCreateTime();
		this.remark = role.getRemark();
		this.roleType = role.getRoleType();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

}
