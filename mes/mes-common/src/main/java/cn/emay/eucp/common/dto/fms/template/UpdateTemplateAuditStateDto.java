package cn.emay.eucp.common.dto.fms.template;

import java.io.Serializable;

public class UpdateTemplateAuditStateDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String templateId;

	private String channelTemplateId;

	private Long channelId;

	private Integer auditState;

	private String operatorCode;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getChannelTemplateId() {
		return channelTemplateId;
	}

	public void setChannelTemplateId(String channelTemplateId) {
		this.channelTemplateId = channelTemplateId;
	}

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
}
