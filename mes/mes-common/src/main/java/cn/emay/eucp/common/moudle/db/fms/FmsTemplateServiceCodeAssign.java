package cn.emay.eucp.common.moudle.db.fms;

import java.io.Serializable;
import java.util.Date;

/** @author dejun
 * @version 创建时间：2019年4月25日 上午10:45:04 类说明 */
public class FmsTemplateServiceCodeAssign implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final Integer SEND_TYPE_INTERFACE = 1;
	public static final Integer SEND_TYPE_PAGE = 2;

	public static final Integer AUDIT_DOING = 0;// 审核中
	public static final Integer AUDIT_COMPLETE = 1;// 审核完成

	private Long id;
	private String templateName;// 报备模板名称
	private String appId;// appID
	private String templateId;// 报备模板id
	private Integer sendType;// 提交类型：1接口，2页面
	private Integer auditState;// 审核状态：0 未完成 1 完成
	private Long businessTypeId;
	private Integer saveType;
	private Long contentTypeId;
	private Date createTime;// 创建时间
	private Long userId;// 用户id

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getAuditState() {
		return auditState;
	}

	public void setAuditState(Integer auditState) {
		this.auditState = auditState;
	}

	public Long getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public Long getContentTypeId() {
		return contentTypeId;
	}

	public void setContentTypeId(Long contentTypeId) {
		this.contentTypeId = contentTypeId;
	}

	public Integer getSaveType() {
		return saveType;
	}

	public void setSaveType(Integer saveType) {
		this.saveType = saveType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
