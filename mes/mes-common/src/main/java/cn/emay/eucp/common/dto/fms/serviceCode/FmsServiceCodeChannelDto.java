package cn.emay.eucp.common.dto.fms.serviceCode;

import java.io.Serializable;
import java.util.Date;

public class FmsServiceCodeChannelDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String appId;// appId
	private Long channelId;// 通道id
	private String channelName;// 通道名
	private String operatorCode;// 运营商编码
	private Date createTime;// 创建时间
	private Integer templateType;// 审核类型：1个性，0普通
	private Integer reportType;// 报备类型

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	public Integer getReportType() {
		return reportType;
	}

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}

}
