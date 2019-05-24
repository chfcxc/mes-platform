package cn.emay.eucp.common.moudle.db.system;

import java.io.Serializable;
import java.util.Date;

/**
 * 携号转网
 * 
 * @author cbb
 * 
 */
public class PortableNumber implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String mobile;// 手机号码
	private String operatorCode;// 运营商CODE[CMCC-移动，CUCC-联通，CTCC-电信]，cn.emay.eucp.common.constant.Operator
	private Boolean isDelete;// 是否删除
	private Date lastUpdateTime;//最后更改时间
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
