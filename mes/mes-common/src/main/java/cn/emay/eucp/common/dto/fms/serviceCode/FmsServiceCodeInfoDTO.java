package cn.emay.eucp.common.dto.fms.serviceCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 特服号关联信息dto
 *
 */
public class FmsServiceCodeInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String serviceCode;// 服务号
	private String appId;
	private String secretKey;// 密钥
	private Integer state;// 状态 0启用1停用
	private Long enterpriseId;// 企业id
	private Long businessTypeId;// 业务类型id
	private String remark;// 备注
	private Date createTime;// 创建时间
	private Integer isNeedReport;// 是否需要状态报告 0是 1否
	private String ipConfiguration;// ip配置

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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

}
