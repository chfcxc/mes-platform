package cn.emay.eucp.common.dto.report;

import java.io.Serializable;
import java.util.Date;

public class ImsServiceCodeCountryReportDTO  implements Serializable {
	
	private static final long serialVersionUID = 1980899318736744811L;

	private String  chineseName;

	private String  appId;
	
	private int  sendNumber;
	
	private Date reportTime;//报表时间 、
	private String reportTimeStr;//报表时间 、
	
	private String clientName;
	
	private Long  enterpriseId;
	private String countryCode;

	private Integer splitsNumber;//拆分条数
	
	private Integer sendSuccessesNumber;// 发送成功数
	private Integer sendFailNumber;//发送失败数
	private Integer sendTimeOutNumber;// 发送超时数
	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public int getSendNumber() {
		return sendNumber;
	}

	public void setSendNumber(int sendNumber) {
		this.sendNumber = sendNumber;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
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
