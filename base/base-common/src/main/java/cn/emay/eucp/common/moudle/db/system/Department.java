package cn.emay.eucp.common.moudle.db.system;

/**
 * 部门
 * 
 * @author Frank
 *
 */
public class Department implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public final static Long MANAGE_ENTERPRISEID=0l;
	
	private Long id;
	private Long parentDepartmentId;// 上级部门ID
	private String departmentCode;// 部门编码
	private String departmentName;// 部门名称
	private Long enterpriseId;// 所属企业ID
	private String remark;// 备注
	private String fullPath;// 部门ID全路径
	private Boolean isDelete;// 是否删除

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentDepartmentId() {
		return parentDepartmentId;
	}

	public void setParentDepartmentId(Long parentDepartmentId) {
		this.parentDepartmentId = parentDepartmentId;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

}