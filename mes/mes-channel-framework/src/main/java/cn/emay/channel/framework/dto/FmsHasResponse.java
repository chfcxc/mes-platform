package cn.emay.channel.framework.dto;

import java.io.Serializable;

public class FmsHasResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String operatorFmsId;// 运营商msgId

	private String customFmsId;// 客户自定义FmsId 选填

	private String mobile;

	private String fmsId;

	private String appId;// 注册号 必填

	private String submitTime;// 客户提交短信时间

	private String channelServiceReceiveResponseTime;// 通道服务接收到响应时间

	private boolean needReport;

	private String batchNo;

	private String channelResponseTime;

	public FmsHasResponse() {

	}

	public FmsHasResponse(String operatorFmsId, String mobile, String fmsId, String appId, String customFmsId, String submitTime, String channelServiceReceiveResponseTime, boolean needReport,
			String batchNo) {
		super();
		this.operatorFmsId = operatorFmsId;
		this.mobile = mobile;
		this.fmsId = fmsId;
		this.appId = appId;
		this.submitTime = submitTime;
		this.channelServiceReceiveResponseTime = channelServiceReceiveResponseTime;
		this.needReport = needReport;
		this.batchNo = batchNo;

	}

	public String getChannelResponseTime() {
		return channelResponseTime;
	}

	public void setChannelResponseTime(String channelResponseTime) {
		this.channelResponseTime = channelResponseTime;
	}

	public String getOperatorFmsId() {
		return operatorFmsId;
	}

	public void setOperatorFmsId(String operatorFmsId) {
		this.operatorFmsId = operatorFmsId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getChannelServiceReceiveResponseTime() {
		return channelServiceReceiveResponseTime;
	}

	public void setChannelServiceReceiveResponseTime(String channelServiceReceiveResponseTime) {
		this.channelServiceReceiveResponseTime = channelServiceReceiveResponseTime;
	}

	public boolean isNeedReport() {
		return needReport;
	}

	public void setNeedReport(boolean needReport) {
		this.needReport = needReport;
	}

	public String getFmsId() {
		return fmsId;
	}

	public void setFmsId(String fmsId) {
		this.fmsId = fmsId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getCustomFmsId() {
		return customFmsId;
	}

	public void setCustomFmsId(String customFmsId) {
		this.customFmsId = customFmsId;
	}

}
