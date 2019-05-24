package cn.emay.eucp.common.moudle.db.system;

import java.io.Serializable;

/**
 * 基础号段
 * 
 * @author Frank
 *
 */
public class BaseSectionNumber implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String number;// 号段
	private String operatorCode;// 运营商CODE[CMCC-移动，CUCC-联通，CTCC-电信]，cn.emay.eucp.common.constant.Operator
	private Boolean isDelete;// 是否删除

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
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

}
