package cn.emay.channel.framework.dto;

import java.io.Serializable;
import java.util.Date;

public class WaitReportDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String appId;
	private Date submitTime;
	private String customFmsId;
	private Boolean isNeedReport;
	private String mobile;
	private String fmsId;
	private String operatorSmsId;// 运营商msgId
	private String batchNo;
	private int sendType;

	public WaitReportDTO() {
		super();
	}

	public WaitReportDTO(String appId, Date submitTime, String customFmsId, Boolean isNeedReport, String mobile, String fmsId, String operatorSmsId, String batchNo, int sendType) {
		super();
		this.appId = appId;
		this.submitTime = submitTime;
		this.customFmsId = customFmsId;
		this.isNeedReport = isNeedReport;
		this.mobile = mobile;
		this.fmsId = fmsId;
		this.operatorSmsId = operatorSmsId;
		this.batchNo = batchNo;
		this.sendType = sendType;
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

	public String getCustomFmsId() {
		return customFmsId;
	}

	public void setCustomFmsId(String customFmsId) {
		this.customFmsId = customFmsId;
	}

	public Boolean getIsNeedReport() {
		return isNeedReport;
	}

	public void setIsNeedReport(Boolean isNeedReport) {
		this.isNeedReport = isNeedReport;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFmsId() {
		return fmsId;
	}

	public void setFmsId(String fmsId) {
		this.fmsId = fmsId;
	}

	public String getOperatorSmsId() {
		return operatorSmsId;
	}

	public void setOperatorSmsId(String operatorSmsId) {
		this.operatorSmsId = operatorSmsId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

}
