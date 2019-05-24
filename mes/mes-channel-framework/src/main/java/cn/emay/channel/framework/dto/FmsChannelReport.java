package cn.emay.channel.framework.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 表名称: 状态报告表
 * 
 * 
 */
public class FmsChannelReport implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;// id,

	private String operatorMessageId;// 运营商消息id,

	private String mobile;// 手机号码,

	private String state;// 状态,

	private String errorCode;// 错误码,

	private String stateDesc;// 错误描述,

	private String channelNo;// 通道号,

	private String channelReportTime;// 状态报告返回时间,

	private Date createTime;// 记录时间,
	
	private Long compareTime = 0L;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOperatorMessageId() {
		return operatorMessageId;
	}

	public void setOperatorMessageId(String operatorMessageId) {
		this.operatorMessageId = operatorMessageId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getStateDesc() {
		return stateDesc;
	}

	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getChannelReportTime() {
		return channelReportTime;
	}

	public void setChannelReportTime(String channelReportTime) {
		this.channelReportTime = channelReportTime;
	}

	public Long getCompareTime() {
		return compareTime;
	}

	public void setCompareTime(Long compareTime) {
		this.compareTime = compareTime;
	}

	
}