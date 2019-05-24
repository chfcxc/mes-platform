package cn.emay.eucp.common.dto.fms.template;

import java.io.Serializable;

/**
 * 模板存储在Redis中的结构
 * 
 * @author dinghaijiao
 *
 */
public class FmsTemplateRedisDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String content;
	private String variable;

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



}
