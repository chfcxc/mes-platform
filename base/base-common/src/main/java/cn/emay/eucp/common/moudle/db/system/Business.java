package cn.emay.eucp.common.moudle.db.system;

import java.util.Date;

/**
 * 系统业务表
 * 
 * @author dinghaijiao
 *
 */
public class Business implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5628358626416324735L;
	private Long id;
	private String businessName;
	private String businessNameSimple;
	private String businessCode;
	private Integer businessIndex;
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getBusinessNameSimple() {
		return businessNameSimple;
	}

	public void setBusinessNameSimple(String businessNameSimple) {
		this.businessNameSimple = businessNameSimple;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public Integer getBusinessIndex() {
		return businessIndex;
	}

	public void setBusinessIndex(Integer businessIndex) {
		this.businessIndex = businessIndex;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
