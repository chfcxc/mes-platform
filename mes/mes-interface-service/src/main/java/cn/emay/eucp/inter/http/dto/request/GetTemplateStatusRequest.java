package cn.emay.eucp.inter.http.dto.request;

/**
 * 获取模板状态请求
 * 
 * @author dinghaijiao
 *
 */
public class GetTemplateStatusRequest extends FmsBaseRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String templateId;

	public GetTemplateStatusRequest(String templateId) {
		super();
		this.templateId = templateId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

}
