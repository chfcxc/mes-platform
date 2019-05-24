package cn.emay.eucp.common.dto.report;

import java.io.Serializable;

public class UpdateFmsDTO implements Serializable {

	private static final long serialVersionUID = -1L;

	private String fmsId;

	private String operatorFmsId;

	private String batchNo;

	private String channelResponseTime;

	private String reportTime;

	private int state;

	private String channelReportState;// 状态报告状态

	private String channelReportDesc;// 状态报告描述

	public UpdateFmsDTO() {

	}

	public UpdateFmsDTO(String fmsId, String channelResponseTime, String reportTime, int state, String channelReportState, String channelReportDesc, String operatorFmsId, String batchNo) {
		super();
		this.setFmsId(fmsId);
		this.setChannelResponseTime(channelResponseTime);
		this.reportTime = reportTime;
		this.state = state;
		this.channelReportState = channelReportState;
		this.channelReportDesc = channelReportDesc;
		this.setOperatorFmsId(operatorFmsId);
		this.batchNo = batchNo;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getChannelReportState() {
		return channelReportState;
	}

	public void setChannelReportState(String channelReportState) {
		this.channelReportState = channelReportState;
	}

	public String getChannelReportDesc() {
		return channelReportDesc;
	}

	public void setChannelReportDesc(String channelReportDesc) {
		this.channelReportDesc = channelReportDesc;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getChannelResponseTime() {
		return channelResponseTime;
	}

	public void setChannelResponseTime(String channelResponseTime) {
		this.channelResponseTime = channelResponseTime;
	}

	public String getFmsId() {
		return fmsId;
	}

	public void setFmsId(String fmsId) {
		this.fmsId = fmsId;
	}

	public String getOperatorFmsId() {
		return operatorFmsId;
	}

	public void setOperatorFmsId(String operatorFmsId) {
		this.operatorFmsId = operatorFmsId;
	}

}
