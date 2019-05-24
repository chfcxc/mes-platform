package cn.emay.eucp.common.moudle.db.system;

import java.util.Date;

/**
 *   联系人组
 * 
 */
public class ContactGroup implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public final static int GROUP_TYPE_PERSONAL = 0;// 组类型-个人组
	public final static int GROUP_TYPE_SHARE = 1;// 组类型-共享组

	private Long id;
	private String groupName;// 组名
	private Integer groupType;// 组类型[0-个人组,1-共享组]
	private Long userId;// 用户id
	private Long enterpriseId;// 企业id
	private Date createTime;// 创建时间
	private Boolean isDelete;// 是否删除
	
	//参数
	private Boolean isShow;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getGroupType() {
		return groupType;
	}

	public void setGroupType(Integer groupType) {
		this.groupType = groupType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
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

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}


}
