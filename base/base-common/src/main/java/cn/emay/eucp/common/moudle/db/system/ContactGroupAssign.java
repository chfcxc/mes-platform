package cn.emay.eucp.common.moudle.db.system;

import java.util.Date;

/**
 *   联系人组关联表
 * 
 */
public class ContactGroupAssign implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long groupId;// 组id
	private Long contactId;// 联系人id
	private Date createTime;// 创建时间
	private Boolean isDelete;// 是否删除

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
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

}
