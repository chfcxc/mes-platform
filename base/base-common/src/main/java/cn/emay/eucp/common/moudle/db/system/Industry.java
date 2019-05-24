package cn.emay.eucp.common.moudle.db.system;

import java.io.Serializable;
import java.util.Date;

/**
 * 	行业
 * 
 */
public class Industry implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String industry;// 行业
	private Boolean isDelete;// 是否删除
	private Date createTime;//创建时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


}
