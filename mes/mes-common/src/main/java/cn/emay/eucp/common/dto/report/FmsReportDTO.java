package cn.emay.eucp.common.dto.report;

import java.io.Serializable;

/**
 * 状态报告获取dto
 *
 */
public class FmsReportDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String appId;// 注册号 必填

	private String customFmsId;// 客户自定义FmsId 选填

	private String fmsId;// 唯一标识

	private String state;// 成功失败标识

	private String desc;// 状态报告描述

	private String mobile;// 手机号 必填 单个

	private String receiveTime;// 状态报告返回时间

	private String submitTime;// 信息提交时间

	public FmsReportDTO() {

	}

	public FmsReportDTO(String appId, String customFmsId, String fmsId, String state, String desc, String mobile, String receiveTime, String submitTime) {
		super();
		this.appId = appId;
		this.customFmsId = customFmsId;
		this.fmsId = fmsId;
		this.state = state;
		this.desc = desc;
		this.mobile = mobile;
		this.receiveTime = receiveTime;
		this.submitTime = submitTime;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getCustomFmsId() {
		return customFmsId;
	}

	public void setCustomFmsId(String customFmsId) {
		this.customFmsId = customFmsId;
	}

	public String getFmsId() {
		return fmsId;
	}

	public void setFmsId(String fmsId) {
		this.fmsId = fmsId;
	}

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

}
