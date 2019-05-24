package cn.emay.eucp.inter.framework.dto;

import java.io.Serializable;

/**
 * 客户查询模板报备状态dto
 * 
 * @author dinghaijiao
 *
 */
public class TemplateResultDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String templateId;
	private TemplateSatus status;

	public TemplateResultDto() {

	}

	public TemplateResultDto(String templateId, TemplateSatus status) {
		this.templateId = templateId;
		this.status = status;

	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public TemplateSatus getStatus() {
		return status;
	}

	public void setStatus(TemplateSatus status) {
		this.status = status;
	}
}
