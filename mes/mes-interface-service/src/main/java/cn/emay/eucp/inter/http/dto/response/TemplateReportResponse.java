package cn.emay.eucp.inter.http.dto.response;

import java.io.Serializable;

/**
 * 报备返回参数
 * 
 * @author dinghaijiao
 *
 */
public class TemplateReportResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String templateId;
	private String variable;

	public TemplateReportResponse(String templateId, String variable) {
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
