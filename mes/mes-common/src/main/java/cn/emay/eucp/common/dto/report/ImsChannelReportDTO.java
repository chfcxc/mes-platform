package cn.emay.eucp.common.dto.report;

import java.io.Serializable;
import java.util.Date;

public class ImsChannelReportDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String channelName;
	private String channelNumber;
	private String chineseName;
	private Integer submitNumber;
	private Integer sendSuccessesNumber;// '发送成功数',
	private Integer sendFailNumber;// '发送失败数',
	private Integer sendTimeOutNumber;// 发送超时数',
	private String  successRate;
	private String  failRate;
	private String  timeOutRate;
	private Integer splitsNumber;//拆分条数
	private Date reportTime;// 报表时间
	private String reportTimeStr;//报表展示时间
	private String countryCode;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelNumber() {
		return channelNumber;
	}

	public void setChannelNumber(String channelNumber) {
		this.channelNumber = channelNumber;
	}

	
	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public Integer getSubmitNumber() {
		return submitNumber;
	}

	public void setSubmitNumber(Integer submitNumber) {
		this.submitNumber = submitNumber;
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

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public String getSuccessRate() {
		return successRate;
	}

	public void setSuccessRate(String successRate) {
		this.successRate = successRate;
	}

	public String getFailRate() {
		return failRate;
	}

	public void setFailRate(String failRate) {
		this.failRate = failRate;
	}

	public String getTimeOutRate() {
		return timeOutRate;
	}

	public void setTimeOutRate(String timeOutRate) {
		this.timeOutRate = timeOutRate;
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

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	
}
