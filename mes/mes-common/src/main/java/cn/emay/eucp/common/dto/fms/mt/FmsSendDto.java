package cn.emay.eucp.common.dto.fms.mt;

import java.io.Serializable;

public class FmsSendDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String appId;//

	private String submitTime;

	private String mobile;

	private String content;
	private String paramContent;// 变量参数
	private String allContent;// 全内容

	private Boolean isNeedReport;

	private String fmsId;

	private String customFmsId;

	private String serialNumber;

	private String batchNo;

	private String templateId;

	private String channelTemplateId;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getIsNeedReport() {
		return isNeedReport;
	}

	public void setIsNeedReport(Boolean isNeedReport) {
		this.isNeedReport = isNeedReport;
	}

	public String getFmsId() {
		return fmsId;
	}

	public void setFmsId(String fmsId) {
		this.fmsId = fmsId;
	}

	public String getCustomFmsId() {
		return customFmsId;
	}

	public void setCustomFmsId(String customFmsId) {
		this.customFmsId = customFmsId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getParamContent() {
		return paramContent;
	}

	public void setParamContent(String paramContent) {
		this.paramContent = paramContent;
	}

	public String getAllContent() {
		return allContent;
	}

	public void setAllContent(String allContent) {
		this.allContent = allContent;
	}

	public String getChannelTemplateId() {
		return channelTemplateId;
	}

	public void setChannelTemplateId(String channelTemplateId) {
		this.channelTemplateId = channelTemplateId;
	}

}
