package cn.emay.eucp.common.moudle.db.system;

/**
 * 用户所属部门
 * 
 * @author Frank
 *
 */
public class UserDepartmentAssign implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public final static int IDENTITY_LEADER = 1;// 领导
	public final static int IDENTITY_EMPLOYEE = 2;// 职员

	private Long id;
	private Long departmentId;// 部门ID
	private Long userId;// 用户ID
	private Integer identity;// 身份[1-领导,2-职员]

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getIdentity() {
		return identity;
	}

	public void setIdentity(Integer identity) {
		this.identity = identity;
	}

}
