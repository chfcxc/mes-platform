package cn.emay.eucp.common.dto.fms.mt;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class SendFmsData implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static int SEND_TYPE_PAGE = 2;// 页面发送接口
	public final static int SEND_TYPE_INTERFACE = 1;// 接口发送

	private String title;// 标题

	private String templateId;// 模板id

	private String remoteIp;// 客户IP

	private String appId;// appId

	private String serviceCode;// 特服号

	private FmsIdAndMobile[] fmsIdAndMobiles; // fmsIdAndMobiles

	private Date submitTime;// 提交时间

	private String interfaceServiceNo;// 接口服务序号

	private String batchNo;// 批次号

	private int batchMobileNumber;// 批次中包含的号码总数

	private int sendType = 0;// 短信类型 1 接口发送 2：页面发送

	private Map<String, String> channelIdMap;

	private int isForceChannel;// 是否强制指定通道

	private String serialNumber;// 短信流水号，页面发送专用

	private Integer templateType;// 模板类型：1个性，0普通

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public FmsIdAndMobile[] getFmsIdAndMobiles() {
		return fmsIdAndMobiles;
	}

	public void setFmsIdAndMobiles(FmsIdAndMobile[] fmsIdAndMobiles) {
		this.fmsIdAndMobiles = fmsIdAndMobiles;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public int getBatchMobileNumber() {
		return batchMobileNumber;
	}

	public void setBatchMobileNumber(int batchMobileNumber) {
		this.batchMobileNumber = batchMobileNumber;
	}

	public Map<String, String> getChannelIdMap() {
		return channelIdMap;
	}

	public void setChannelIdMap(Map<String, String> channelIdMap) {
		this.channelIdMap = channelIdMap;
	}

	public int getIsForceChannel() {
		return isForceChannel;
	}

	public void setIsForceChannel(int isForceChannel) {
		this.isForceChannel = isForceChannel;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getInterfaceServiceNo() {
		return interfaceServiceNo;
	}

	public void setInterfaceServiceNo(String interfaceServiceNo) {
		this.interfaceServiceNo = interfaceServiceNo;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
