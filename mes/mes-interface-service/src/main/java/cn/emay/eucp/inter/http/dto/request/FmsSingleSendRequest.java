package cn.emay.eucp.inter.http.dto.request;

/**
 * 单条发送请求接口
 * 
 * @author dinghaijiao
 *
 */
public class FmsSingleSendRequest extends FmsBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String mobile;
	private String templateId;
	private String content;
	private String customFmsId;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public String getCustomFmsId() {
		return customFmsId;
	}

	public void setCustomFmsId(String customFmsId) {
		this.customFmsId = customFmsId;
	}

}
