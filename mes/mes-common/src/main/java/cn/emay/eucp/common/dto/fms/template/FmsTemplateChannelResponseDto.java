package cn.emay.eucp.common.dto.fms.template;

import java.io.Serializable;
import java.util.Date;

/** @author dejun
 * @version 创建时间：2019年5月6日 下午2:35:19 类说明 */
public class FmsTemplateChannelResponseDto implements Serializable {

	/** @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) */
	private static final long serialVersionUID = 1L;

	private String templateId;// 模板id
	private Long channelId;// 通道id
	private Integer state;// 报备状态：0 报备中，1通过，2拒绝，3不支持
	private Integer cmccState; // -1 报备中，1通过，2拒绝，3不支持
	private Integer cuccState;
	private Integer ctccState;
	private String cmccTemplateId;
	private String cuccTemplateId;
	private String ctccTemplateId;
	private Date auditTime;// 审核时间

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

	public Integer getCmccState() {
		return cmccState;
	}

	public void setCmccState(Integer cmccState) {
		this.cmccState = cmccState;
	}

	public Integer getCuccState() {
		return cuccState;
	}

	public void setCuccState(Integer cuccState) {
		this.cuccState = cuccState;
	}

	public Integer getCtccState() {
		return ctccState;
	}

	public void setCtccState(Integer ctccState) {
		this.ctccState = ctccState;
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

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

}
