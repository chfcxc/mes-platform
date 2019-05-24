package cn.emay.eucp.common.dto.report;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ImsAccountReportDTO implements Serializable {

	private static final long serialVersionUID = 1797999119159890941L;
	
	private String serviceCode;
	
	private String appId;
	
	private Date reportTime;// 报表时间
	private String reportTimeStr;// 报表时间
	
	private BigDecimal consumptionAmount;// 消费金额
	
	private BigDecimal remainingAmount;// 剩余金额

	public ImsAccountReportDTO() {
		super();
	}

	public ImsAccountReportDTO(String serviceCode, String appId, Date reportTime, BigDecimal consumptionAmount, BigDecimal remainingAmount) {
		super();
		this.serviceCode = serviceCode;
		this.appId = appId;
		this.reportTime = reportTime;
		this.consumptionAmount = consumptionAmount;
		this.remainingAmount = remainingAmount;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public BigDecimal getConsumptionAmount() {
		return consumptionAmount;
	}

	public void setConsumptionAmount(BigDecimal consumptionAmount) {
		this.consumptionAmount = consumptionAmount;
	}

	public BigDecimal getRemainingAmount() {
		return remainingAmount;
	}

	public void setRemainingAmount(BigDecimal remainingAmount) {
		this.remainingAmount = remainingAmount;
	}

	public String getReportTimeStr() {
		return reportTimeStr;
	}

	public void setReportTimeStr(String reportTimeStr) {
		this.reportTimeStr = reportTimeStr;
	}
}
