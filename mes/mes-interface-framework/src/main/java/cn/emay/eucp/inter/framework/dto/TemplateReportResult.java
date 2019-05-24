package cn.emay.eucp.inter.framework.dto;

public class TemplateReportResult {

	private String templateId;
	private String variable;

	public TemplateReportResult(String templateId, String variable) {
		this.templateId = templateId;
		this.variable = variable;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}


}
