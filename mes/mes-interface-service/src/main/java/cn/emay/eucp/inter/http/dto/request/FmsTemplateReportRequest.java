package cn.emay.eucp.inter.http.dto.request;


/**
 * 报备发送参数
 * 
 * @author dinghaijiao
 *
 */
public class FmsTemplateReportRequest extends FmsBaseRequest {

	private static final long serialVersionUID = 1L;

	/**
	 * 报备内容
	 */
	private String templateInfo;

	public String getTemplateInfo() {
		return templateInfo;
	}

	public void setTemplateInfo(String templateInfo) {
		this.templateInfo = templateInfo;
	}




}
