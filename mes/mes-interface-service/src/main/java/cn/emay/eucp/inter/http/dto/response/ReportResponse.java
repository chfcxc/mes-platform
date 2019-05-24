package cn.emay.eucp.inter.http.dto.response;

import java.io.Serializable;

/**
 * 状态报告数据
 * 
 */
public class ReportResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fmsId;// 短信唯一标识

	private String customFmsId;// 客户自定义SmsId

	private String state;// 成功失败标识

	private String desc;// 状态报告描述

	private String mobile;// 手机号

	private String receiveTime;// 状态报告返回时间

	private String submitTime;// 信息提交时间


	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}

	public String getFmsId() {
		return fmsId;
	}

	public void setFmsId(String fmsId) {
		this.fmsId = fmsId;
	}

	public String getCustomFmsId() {
		return customFmsId;
	}

	public void setCustomFmsId(String customFmsId) {
		this.customFmsId = customFmsId;
	}
	
}
