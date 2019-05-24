package cn.emay.eucp.common.moudle.db.system;

/**
 * 用户角色关联
 * 
 * @author Frank
 *
 */
public class UserRoleAssign implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long userId;// 用户ID
	private Long roleId;// 角色ID
	
	public UserRoleAssign(){
		
	}
	
	public UserRoleAssign(Long userId,Long roleId){
		this.userId=userId;
		this.roleId=roleId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}