package cn.emay.eucp.common.moudle.db.fms;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dejun
 * @version 创建时间：2019年4月25日 上午10:52:01 类说明
 */
public class FmsTemplateChannelReport implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String templateId;// 模板id
	private Long channelId;// 通道id
	private Integer state;// 报备状态：0 报备中，1通过，2拒绝，3不支持
	private String channelTemplateId;// 通道报备id
	private String operatorCode;// 运营商类型
	private Date auditTime;// 审核时间
	private Date createTime;// 创建时间

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

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getChannelTemplateId() {
		return channelTemplateId;
	}

	public void setChannelTemplateId(String channelTemplateId) {
		this.channelTemplateId = channelTemplateId;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

}
