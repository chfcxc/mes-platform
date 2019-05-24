package cn.emay.eucp.common.dto.fms.message;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MessageAccountDetailDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String imsId;// IMSID
	private String appId;// appid
	private String content;// 内容
	private String mobile;// 手机号码
	private String country;// 国家
	private Integer state;// '状态 1发送中 2发送到运营商 3发送成功 4发送失败 5发送超时',
	private Date submitTime;// 发送时间
	private Integer sendType;// 发送类型：0 接口发送 1 页面发送
	private BigDecimal balance;// 余额
	private BigDecimal deductionAmount;// 扣费金额
	private BigDecimal beforeBuckleAmount;// 扣前金额
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getImsId() {
		return imsId;
	}
	public void setImsId(String imsId) {
		this.imsId = imsId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	public Date getSubmitTime() {
		return submitTime;
	}
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	public Integer getSendType() {
		return sendType;
	}
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getDeductionAmount() {
		return deductionAmount;
	}
	public void setDeductionAmount(BigDecimal deductionAmount) {
		this.deductionAmount = deductionAmount;
	}
	public BigDecimal getBeforeBuckleAmount() {
		return beforeBuckleAmount;
	}
	public void setBeforeBuckleAmount(BigDecimal beforeBuckleAmount) {
		this.beforeBuckleAmount = beforeBuckleAmount;
	}
	
	
}
