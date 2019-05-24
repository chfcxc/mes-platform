package cn.emay.eucp.common.dto.fms.serviceCode;

import java.io.Serializable;

public class FmsServiceCodeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String serviceCode;// 服务号
	private String appId;// APPID
	private String secretKey;// 密钥
	private Long enterpriseId;// 客户id
	private Integer state;// '状态[0-激活，1-停用]
	private Integer saveType;
	private String contentType;
	private String businessType;
	private String enterpriseName;
	private String enterpriseNumber;
	private Integer isNeedReport;// 是否需要状态报告 1不获取 2主动获取
	private String ipConfiguration;// ip配置
	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getSaveType() {
		return saveType;
	}

	public void setSaveType(Integer saveType) {
		this.saveType = saveType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public String getEnterpriseNumber() {
		return enterpriseNumber;
	}

	public void setEnterpriseNumber(String enterpriseNumber) {
		this.enterpriseNumber = enterpriseNumber;
	}

	public Integer getIsNeedReport() {
		return isNeedReport;
	}

	public void setIsNeedReport(Integer isNeedReport) {
		this.isNeedReport = isNeedReport;
	}

	public String getIpConfiguration() {
		return ipConfiguration;
	}

	public void setIpConfiguration(String ipConfiguration) {
		this.ipConfiguration = ipConfiguration;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
