package cn.emay.eucp.common.moudle.db.system;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业与销售人员关系实体
 * 
 * @author ChenXiyao
 * 
 */
public class EnterpriseBindingSale implements Serializable {

	private static final long serialVersionUID = 2548545117921689371L;

	private Long id;
	private Long systemEnterpriseId;// 企业ID
	private Long systemUserId;// 人员ID
	private String lastUpdateMan;
	private Date createTime;
	private String createMan;

	private String systemEnterpriseName;// 客户名称
	private String systemEnterpriseCode;// 客户编码
	private String systemUsername;// 销售用户名
	private String systemRealname;// 销售姓名
	private String parentDepartment;// 上级部门

	/************************ 销售系统 ************************/
	private String linkman;// 联系人
	private String mobile;// 手机号
	private Integer serviceCodeCount;// 服务号数量
	private String type;// 客户类型：0：普通客户；1：代理商
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSystemEnterpriseId() {
		return systemEnterpriseId;
	}

	public void setSystemEnterpriseId(Long systemEnterpriseId) {
		this.systemEnterpriseId = systemEnterpriseId;
	}

	public Long getSystemUserId() {
		return systemUserId;
	}

	public void setSystemUserId(Long systemUserId) {
		this.systemUserId = systemUserId;
	}

	public String getLastUpdateMan() {
		return lastUpdateMan;
	}

	public void setLastUpdateMan(String lastUpdateMan) {
		this.lastUpdateMan = lastUpdateMan;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateMan() {
		return createMan;
	}

	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}

	public String getSystemEnterpriseName() {
		return systemEnterpriseName;
	}

	public void setSystemEnterpriseName(String systemEnterpriseName) {
		this.systemEnterpriseName = systemEnterpriseName;
	}

	public String getSystemEnterpriseCode() {
		return systemEnterpriseCode;
	}

	public void setSystemEnterpriseCode(String systemEnterpriseCode) {
		this.systemEnterpriseCode = systemEnterpriseCode;
	}

	public String getSystemUsername() {
		return systemUsername;
	}

	public void setSystemUsername(String systemUsername) {
		this.systemUsername = systemUsername;
	}

	public String getSystemRealname() {
		return systemRealname;
	}

	public void setSystemRealname(String systemRealname) {
		this.systemRealname = systemRealname;
	}

	public String getParentDepartment() {
		return parentDepartment;
	}

	public void setParentDepartment(String parentDepartment) {
		this.parentDepartment = parentDepartment;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getServiceCodeCount() {
		return serviceCodeCount;
	}

	public void setServiceCodeCount(Integer serviceCodeCount) {
		this.serviceCodeCount = serviceCodeCount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
