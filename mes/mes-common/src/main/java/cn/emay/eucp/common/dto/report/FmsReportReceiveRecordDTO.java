package cn.emay.eucp.common.dto.report;

import java.io.Serializable;
import java.util.Date;

/**
 * 状态报告获取记录dto
 *
 */
public class FmsReportReceiveRecordDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String appId;//appId
	private String remoteIp;//客户ip
	private Date createTime;//记录时间
	private String reportJson;//状态报告json串

	public FmsReportReceiveRecordDTO() {
		
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getReportJson() {
		return reportJson;
	}

	public void setReportJson(String reportJson) {
		this.reportJson = reportJson;
	}

}
