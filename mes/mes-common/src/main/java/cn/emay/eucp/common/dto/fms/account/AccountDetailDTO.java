package cn.emay.eucp.common.dto.fms.account;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AccountDetailDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String appId;// APPID
	private BigDecimal changeNumber;// 变动金额
	private Long serviceCodeId;
	private BigDecimal remainingNumber;// 剩余金额

	private Integer operationType;// 操作类型

	private String remark;// 备注
	private Long enterpriseId;
	private Long userId;
	private Date operationTime;// 操作时间
	private Long businessTypeId;
	private String serviceCode;// 服务号
	private String userName;
	private String nameCn;// 企业中文名称（客户名称）
	private String clientNumber;// 客户编号
	private Integer saveType;
	private String contentType;
	private String businessType;

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

	public BigDecimal getChangeNumber() {
		return changeNumber;
	}

	public void setChangeNumber(BigDecimal changeNumber) {
		this.changeNumber = changeNumber;
	}

	public BigDecimal getRemainingNumber() {
		return remainingNumber;
	}

	public void setRemainingNumber(BigDecimal remainingNumber) {
		this.remainingNumber = remainingNumber;
	}

	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getOperationTime() {
		return operationTime;
	}

	public void setOperationTime(Date operationTime) {
		this.operationTime = operationTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNameCn() {
		return nameCn;
	}

	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}

	public String getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
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

	public Long getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getServiceCodeId() {
		return serviceCodeId;
	}

	public void setServiceCodeId(Long serviceCodeId) {
		this.serviceCodeId = serviceCodeId;
	}

}
