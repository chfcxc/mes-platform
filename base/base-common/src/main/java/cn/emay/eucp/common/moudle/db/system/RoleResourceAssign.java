package cn.emay.eucp.common.moudle.db.system;

/**
 * 角色 资源关联
 * 
 * @author Frank
 *
 */
public class RoleResourceAssign implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long roleId;// 角色ID
	private Long resourceId;// 资源ID

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
	}

}