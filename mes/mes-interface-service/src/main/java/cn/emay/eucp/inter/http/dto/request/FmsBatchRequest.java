package cn.emay.eucp.inter.http.dto.request;

import cn.emay.eucp.inter.framework.dto.CustomFmsIdAndMobile;

/**
 * 批量短信发送参数
 * 
 * @author Frank
 *
 */
public class FmsBatchRequest extends FmsBaseRequest {

	private static final long serialVersionUID = 1L;

	private String templateId;
	/**
	 * 手机号与自定义SmsId
	 */
	private CustomFmsIdAndMobile[] fmses;

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public CustomFmsIdAndMobile[] getFmses() {
		return fmses;
	}

	public void setFmses(CustomFmsIdAndMobile[] fmses) {
		this.fmses = fmses;
	}

}
