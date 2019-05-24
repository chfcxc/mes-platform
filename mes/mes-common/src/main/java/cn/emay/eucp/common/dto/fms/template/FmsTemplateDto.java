package cn.emay.eucp.common.dto.fms.template;

import java.io.Serializable;
import java.util.Date;

public class FmsTemplateDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String appId;
	private Long serviceCodeId;
	private String templateName;
	private String templateId;
	private String content;
	private String cmccChannelName;
	private String cuccChannelName;
	private String ctccChannelName;
	private Long cmccChannelId;
	private Long cuccChannelId;
	private Long ctccChannelId;
	private Long businessTypeId;
	private String businessType;
	private Integer saveType;
	private Long contentTypeId;
	private String contentType;
	private Integer templateType;
	private Integer sendType;
	private String cmccTemplateId;
	private String cuccTemplateId;
	private String ctccTemplateId;
	private Date submitTime;
	private Integer cmccReportType;
	private Integer cuccReportType;
	private Integer ctccReportType;
	private Integer cmccAuditState;
	private Integer cuccAuditState;
	private Integer ctccAuditState;
	private String cmccAuditStateDesc;
	private String cuccAuditStateDesc;
	private String ctccAuditStateDesc;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getCmccChannelName() {
		return cmccChannelName;
	}

	public void setCmccChannelName(String cmccChannelName) {
		this.cmccChannelName = cmccChannelName;
	}

	public String getCuccChannelName() {
		return cuccChannelName;
	}

	public void setCuccChannelName(String cuccChannelName) {
		this.cuccChannelName = cuccChannelName;
	}

	public String getCtccChannelName() {
		return ctccChannelName;
	}

	public void setCtccChannelName(String ctccChannelName) {
		this.ctccChannelName = ctccChannelName;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getCmccTemplateId() {
		return cmccTemplateId;
	}

	public void setCmccTemplateId(String cmccTemplateId) {
		this.cmccTemplateId = cmccTemplateId;
	}

	public String getCuccTemplateId() {
		return cuccTemplateId;
	}

	public void setCuccTemplateId(String cuccTemplateId) {
		this.cuccTemplateId = cuccTemplateId;
	}

	public String getCtccTemplateId() {
		return ctccTemplateId;
	}

	public void setCtccTemplateId(String ctccTemplateId) {
		this.ctccTemplateId = ctccTemplateId;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Integer getCmccReportType() {
		return cmccReportType;
	}

	public void setCmccReportType(Integer cmccReportType) {
		this.cmccReportType = cmccReportType;
	}

	public Integer getCuccReportType() {
		return cuccReportType;
	}

	public void setCuccReportType(Integer cuccReportType) {
		this.cuccReportType = cuccReportType;
	}

	public Integer getCtccReportType() {
		return ctccReportType;
	}

	public void setCtccReportType(Integer ctccReportType) {
		this.ctccReportType = ctccReportType;
	}

	public Long getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public Long getContentTypeId() {
		return contentTypeId;
	}

	public void setContentTypeId(Long contentTypeId) {
		this.contentTypeId = contentTypeId;
	}

	public Integer getCmccAuditState() {
		return cmccAuditState;
	}

	public void setCmccAuditState(Integer cmccAuditState) {
		this.cmccAuditState = cmccAuditState;
	}

	public Integer getCuccAuditState() {
		return cuccAuditState;
	}

	public void setCuccAuditState(Integer cuccAuditState) {
		this.cuccAuditState = cuccAuditState;
	}

	public Integer getCtccAuditState() {
		return ctccAuditState;
	}

	public void setCtccAuditState(Integer ctccAuditState) {
		this.ctccAuditState = ctccAuditState;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public Long getCmccChannelId() {
		return cmccChannelId;
	}

	public void setCmccChannelId(Long cmccChannelId) {
		this.cmccChannelId = cmccChannelId;
	}

	public Long getCuccChannelId() {
		return cuccChannelId;
	}

	public void setCuccChannelId(Long cuccChannelId) {
		this.cuccChannelId = cuccChannelId;
	}

	public Long getCtccChannelId() {
		return ctccChannelId;
	}

	public void setCtccChannelId(Long ctccChannelId) {
		this.ctccChannelId = ctccChannelId;
	}

	public Integer getSaveType() {
		return saveType;
	}

	public void setSaveType(Integer saveType) {
		this.saveType = saveType;
	}

	public String getCmccAuditStateDesc() {
		return cmccAuditStateDesc;
	}

	public void setCmccAuditStateDesc(String cmccAuditStateDesc) {
		this.cmccAuditStateDesc = cmccAuditStateDesc;
	}

	public String getCuccAuditStateDesc() {
		return cuccAuditStateDesc;
	}

	public void setCuccAuditStateDesc(String cuccAuditStateDesc) {
		this.cuccAuditStateDesc = cuccAuditStateDesc;
	}

	public String getCtccAuditStateDesc() {
		return ctccAuditStateDesc;
	}

	public void setCtccAuditStateDesc(String ctccAuditStateDesc) {
		this.ctccAuditStateDesc = ctccAuditStateDesc;
	}

	public Long getServiceCodeId() {
		return serviceCodeId;
	}

	public void setServiceCodeId(Long serviceCodeId) {
		this.serviceCodeId = serviceCodeId;
	}

}
