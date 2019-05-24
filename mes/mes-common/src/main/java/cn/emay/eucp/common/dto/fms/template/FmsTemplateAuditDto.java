package cn.emay.eucp.common.dto.fms.template;

import java.io.Serializable;

/** @author dejun
 * @version 创建时间：2019年4月29日 下午3:10:06 类说明 */
public class FmsTemplateAuditDto implements Serializable {

	/** @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) */
	private static final long serialVersionUID = 1L;

	private String templateName;// 模板名称
	private String templateId;// 模板id
	private Long serviceCodeId;// 服务号id
	private String serviceCode;// 服务号
	private String appId;// appId
	private Integer cmccAuditState;
	private Integer cuccAuditState;
	private Integer ctccAuditState;
	private String businessType;
	private String contentType;
	private Integer saveType;
	private Long contentTypeId;
	private Long cmccChannelId;
	private Long cuccChannelId;
	private Long ctccChannelId;
	private String content;

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Long getServiceCodeId() {
		return serviceCodeId;
	}

	public void setServiceCodeId(Long serviceCodeId) {
		this.serviceCodeId = serviceCodeId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getCmccAuditState() {
		return cmccAuditState;
	}

	public void setCmccAuditState(Integer cmccAuditState) {
		this.cmccAuditState = cmccAuditState;
	}

	public Integer getCuccAuditState() {
		return cuccAuditState;
	}

	public void setCuccAuditState(Integer cuccAuditState) {
		this.cuccAuditState = cuccAuditState;
	}

	public Integer getCtccAuditState() {
		return ctccAuditState;
	}

	public void setCtccAuditState(Integer ctccAuditState) {
		this.ctccAuditState = ctccAuditState;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Integer getSaveType() {
		return saveType;
	}

	public void setSaveType(Integer saveType) {
		this.saveType = saveType;
	}

	public Long getContentTypeId() {
		return contentTypeId;
	}

	public void setContentTypeId(Long contentTypeId) {
		this.contentTypeId = contentTypeId;
	}

	public Long getCmccChannelId() {
		return cmccChannelId;
	}

	public void setCmccChannelId(Long cmccChannelId) {
		this.cmccChannelId = cmccChannelId;
	}

	public Long getCuccChannelId() {
		return cuccChannelId;
	}

	public void setCuccChannelId(Long cuccChannelId) {
		this.cuccChannelId = cuccChannelId;
	}

	public Long getCtccChannelId() {
		return ctccChannelId;
	}

	public void setCtccChannelId(Long ctccChannelId) {
		this.ctccChannelId = ctccChannelId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
