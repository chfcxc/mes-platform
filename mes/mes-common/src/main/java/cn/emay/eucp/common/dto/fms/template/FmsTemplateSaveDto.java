package cn.emay.eucp.common.dto.fms.template;

import java.io.Serializable;
import java.util.Date;

/**
 * 模板保存dto
 * 
 * @author dinghaijiao
 *
 */
public class FmsTemplateSaveDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String templateName;
	private String appId;
	private String templateId;// 模板id
	private String content;// 报备模板内容
	private String variable;// 报备模板变量
	private Integer sendType;// 提交类型0接口，1页面
	private Date submitTime;// 提交时间
	private Long userId;// 用户id
	private Integer templateType;// 模板类型：1 个性 0 普通
	private Date lastUpdateTime;// 更新时间
	private Date createTime;// 创建时间
	private Boolean save;// 模板主数据是否已入库

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public Boolean getSave() {
		return save;
	}

	public void setSave(Boolean save) {
		this.save = save;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

}
