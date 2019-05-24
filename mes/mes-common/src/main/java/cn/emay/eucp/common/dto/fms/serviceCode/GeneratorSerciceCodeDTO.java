package cn.emay.eucp.common.dto.fms.serviceCode;

import java.io.Serializable;

/**
 * @author Shmily
 *
 *         生成特服号实体对象
 */
public class GeneratorSerciceCodeDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String sn;
	private String serviceCode;
	private String secretKey;
	private Long enterpriseId;
	private String agentAbbr;
	private String message;
	private boolean success;
	private String registryCode;
	private Integer state;
	private Long businessTypeId;
	private String remark;

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getAgentAbbr() {
		return agentAbbr;
	}

	public void setAgentAbbr(String agentAbbr) {
		this.agentAbbr = agentAbbr;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getRegistryCode() {
		return registryCode;
	}

	public void setRegistryCode(String registryCode) {
		this.registryCode = registryCode;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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
