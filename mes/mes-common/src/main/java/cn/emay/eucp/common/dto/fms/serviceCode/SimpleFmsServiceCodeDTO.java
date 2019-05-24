package cn.emay.eucp.common.dto.fms.serviceCode;

import java.io.Serializable;

import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;

public class SimpleFmsServiceCodeDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String serviceCode;// 服务号
	private String agentAbbr = "EMY"; // EMY
	private Long enterpriseId;
	private String snType = "EUCP"; // EUCP
	private String version; // [TEST|0000]
	private String customerName;// 客户名称
	private String customerNumber;// 客户编号
	private Long userId;
	private Long businessTypeId;
	private String remark;

	private FmsServiceCode fmsServiceCode = new FmsServiceCode();

	public SimpleFmsServiceCodeDTO(String serviceCode, Long enterpriseId, String agentAbbr) {
		this.serviceCode = serviceCode;
		this.enterpriseId = enterpriseId;
		this.agentAbbr = agentAbbr;
	}

	public FmsServiceCode getfmsServiceCode() {
		return fmsServiceCode;
	}

	public void setFmsServiceCode(FmsServiceCode fmsServiceCode) {
		this.fmsServiceCode = fmsServiceCode;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getAgentAbbr() {
		return agentAbbr;
	}

	public void setAgentAbbr(String agentAbbr) {
		this.agentAbbr = agentAbbr;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getSnType() {
		return snType;
	}

	public void setSnType(String snType) {
		this.snType = snType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerNumber() {
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
