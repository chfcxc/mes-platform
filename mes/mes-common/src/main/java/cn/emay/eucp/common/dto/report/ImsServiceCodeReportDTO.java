package cn.emay.eucp.common.dto.report;

import java.io.Serializable;
import java.util.Date;

public class ImsServiceCodeReportDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String appId;
	private Integer sendNumber;// 发送条数
	private Date reportTime;//报表时间
	private Integer splitsNumber;//拆分条数
	private String reportTimeStr;//报表时间
	private Integer sendSuccessesNumber;// 发送成功数
	private Integer sendFailNumber;//发送失败数
	private Integer sendTimeOutNumber;// 发送超时数
	public ImsServiceCodeReportDTO() {
		super();
	}

	public ImsServiceCodeReportDTO(String appId, Integer sendNumber, Date reportTime) {
		super();
		this.appId = appId;
		this.sendNumber = sendNumber;
		this.reportTime = reportTime;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getSendNumber() {
		return sendNumber;
	}

	public void setSendNumber(Integer sendNumber) {
		this.sendNumber = sendNumber;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public Integer getSplitsNumber() {
		return splitsNumber;
	}

	public void setSplitsNumber(Integer splitsNumber) {
		this.splitsNumber = splitsNumber;
	}

	public String getReportTimeStr() {
		return reportTimeStr;
	}

	public void setReportTimeStr(String reportTimeStr) {
		this.reportTimeStr = reportTimeStr;
	}

	public Integer getSendSuccessesNumber() {
		return sendSuccessesNumber;
	}

	public void setSendSuccessesNumber(Integer sendSuccessesNumber) {
		this.sendSuccessesNumber = sendSuccessesNumber;
	}

	public Integer getSendFailNumber() {
		return sendFailNumber;
	}

	public void setSendFailNumber(Integer sendFailNumber) {
		this.sendFailNumber = sendFailNumber;
	}

	public Integer getSendTimeOutNumber() {
		return sendTimeOutNumber;
	}

	public void setSendTimeOutNumber(Integer sendTimeOutNumber) {
		this.sendTimeOutNumber = sendTimeOutNumber;
	}
	
}
