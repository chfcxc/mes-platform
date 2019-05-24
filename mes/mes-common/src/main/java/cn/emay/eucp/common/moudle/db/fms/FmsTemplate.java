package cn.emay.eucp.common.moudle.db.fms;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dejun
 * @version 创建时间：2019年4月25日 上午10:24:39 类说明
 */
public class FmsTemplate implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int REPORT_SUBMIT = 0;// 提交报备
	public static final int REPORT_DOIND = 1;// 报备中
	public static final int REPORT_OK = 2;// 报备成功
	public static final int REPORT_ERROR = 3;// 报备失败
	public static final int NOT_SUPPORT = 4;// 不支持
	public static final int REPORT_TIMEOUT = 5;// 报备超时
	public static final int NOT_SET_CHANNEL = 6;// 未配置通道
	public static final int NOT_REPORT = 7;// 未报备

	public static final int TEMPLATE_TYPE_ORDINAY = 0;// 普通
	public static final int TEMPLATE_TYPE_PERSONALITY = 1;// 个性

	private String id;// 模板id
	private String content;// 报备模板内容
	private String variable;// 报备模板变量
	private Date submitTime;// 提交时间
	private Long userId;// 用户id
	private Integer templateType;// 模板类型：1 个性 0 普通
	private Date lastUpdateTime;// 更新时间
	private Date createTime;// 创建时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

}
