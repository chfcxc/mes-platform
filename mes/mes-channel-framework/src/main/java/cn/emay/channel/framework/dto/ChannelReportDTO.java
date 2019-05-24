package cn.emay.channel.framework.dto;

import java.io.Serializable;

public class ChannelReportDTO implements Serializable {

	private static final long serialVersionUID = -810411052372136120L;

	private String channelReportTime;// 通道（网关异步）状态报告返回时间
	private String mobile;// 手机号
	private String operatorFmsId;// 运营商msgid
	private String state;// 状态报告
	private String stateDesc;// 状态报告描述
	private Long compareTime = 0L;

	public ChannelReportDTO(String channelReportTime, String mobile, String operatorFmsId, String state, String stateDesc) {
		super();
		this.channelReportTime = channelReportTime;
		this.mobile = mobile;
		this.operatorFmsId = operatorFmsId;
		this.state = state;
		this.stateDesc = stateDesc;
	}

	public String getChannelReportTime() {
		return channelReportTime;
	}

	public void setChannelReportTime(String channelReportTime) {
		this.channelReportTime = channelReportTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOperatorFmsId() {
		return operatorFmsId;
	}

	public void setOperatorFmsId(String operatorFmsId) {
		this.operatorFmsId = operatorFmsId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateDesc() {
		return stateDesc;
	}

	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}

	public Long getCompareTime() {
		return compareTime;
	}

	public void setCompareTime(Long compareTime) {
		this.compareTime = compareTime;
	}

}
