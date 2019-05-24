package cn.emay.eucp.common.dto.fms.template;

import java.io.Serializable;

public class ReportFmsTemplateDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String templateId;

	private Integer templateType;

	private String content;

	private String variable;

	public ReportFmsTemplateDto() {

	}

	public ReportFmsTemplateDto(String templateId, Integer templateType, String content, String variable) {
		super();
		this.templateId = templateId;
		this.setTemplateType(templateType);
		this.content = content;
		this.variable = variable;
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

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}
}
