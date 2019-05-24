package cn.emay.eucp.common.dto.fms.page;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dejun
 * @version 创建时间：2019年4月29日 下午5:55:28 类说明
 */
public class PageSendDto implements Serializable {

	/** @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) */
	public static final Integer INTER_TYPE = 1;
	public static final Integer PAGE_TYPE = 2;
	private static final long serialVersionUID = 1L;
	private String templateId;// 模板id
	private Integer templateType;// 模板类型：1个性，0普通
	private String appId;// appID
	private Date submitTime;// 提交时间
	private Integer state;// 状态 -1 标识编辑状态 0 待解析 1 解析中 2 待发送 3 发送完成
	private String title;// 标题
	private String batchNumber;// 批次号
	private Integer sendType;// 发送方式：1、接口；2、页面
	private Long cmccChannelId;
	private Long cuccChannelId;
	private Long ctccChannelId;
	private Long contentTypeId;
	private String cmccTemplateId;
	private String cuccTemplateId;
	private String ctccTemplateId;
	private Integer cmccAuditState;
	private Integer cuccAuditState;
	private Integer ctccAuditState;

	public PageSendDto(String templateId, Integer templateType, String appId, Date submitTime, Integer state, String title, String batchNumber, Integer sendType, Long cmccChannelId,
			Long cuccChannelId, Long ctccChannelId, Long contentTypeId, String cmccTemplateId, String cuccTemplateId, String ctccTemplateId, Integer cmccAuditState, Integer cuccAuditState,
			Integer ctccAuditState) {
		super();
		this.templateId = templateId;
		this.templateType = templateType;
		this.appId = appId;
		this.submitTime = submitTime;
		this.state = state;
		this.title = title;
		this.batchNumber = batchNumber;
		this.sendType = sendType;
		this.cmccChannelId = cmccChannelId;
		this.cuccChannelId = cuccChannelId;
		this.ctccChannelId = ctccChannelId;
		this.contentTypeId = contentTypeId;
		this.cmccTemplateId = cmccTemplateId;
		this.cuccTemplateId = cuccTemplateId;
		this.ctccTemplateId = ctccTemplateId;
		this.cmccAuditState = cmccAuditState;
		this.cuccAuditState = cuccAuditState;
		this.ctccAuditState = ctccAuditState;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
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

	public String getCmccTemplateId() {
		return cmccTemplateId;
	}

	public void setCmccTemplateId(String cmccTemplateId) {
		this.cmccTemplateId = cmccTemplateId;
	}

	public String getCuccTemplateId() {
		return cuccTemplateId;
	}

	public void setCuccTemplateId(String cuccTemplateId) {
		this.cuccTemplateId = cuccTemplateId;
	}

	public String getCtccTemplateId() {
		return ctccTemplateId;
	}

	public void setCtccTemplateId(String ctccTemplateId) {
		this.ctccTemplateId = ctccTemplateId;
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

	public Long getContentTypeId() {
		return contentTypeId;
	}

	public void setContentTypeId(Long contentTypeId) {
		this.contentTypeId = contentTypeId;
	}

}
