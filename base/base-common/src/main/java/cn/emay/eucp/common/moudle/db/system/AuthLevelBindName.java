package cn.emay.eucp.common.moudle.db.system;

import java.io.Serializable;
import java.util.Date;

/**
 * 客户权限名称与授权等级关系表
 * system_name_binding_level
 * @author gh
 */
public class AuthLevelBindName implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String authName;// 板块名称
	private int authLevel;//授权等级
	private Date  createTime;//创建时间
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public int getAuthLevel() {
		return authLevel;
	}

	public void setAuthLevel(int authLevel) {
		this.authLevel = authLevel;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}