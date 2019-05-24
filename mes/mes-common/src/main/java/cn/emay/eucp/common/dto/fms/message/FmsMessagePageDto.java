package cn.emay.eucp.common.dto.fms.message;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dejun
 * @version 创建时间：2019年4月30日 下午4:35:33 类说明
 */
public class FmsMessagePageDto implements Serializable {

	/** @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) */
	private static final long serialVersionUID = 1L;

	private Long serviceCodeId;// 服务号ID
	private String batchNumber;// 批次号
	private String title;// 标题
	private String mobile;// 手机号
	private String content;// 全量内容
	private Date sendTime;// 发送时间
	private String operatorCode;// 运营商编码
	private Long contentTypeId;// 内容类型id
	private String businessType;
	private String contentType;
	private Integer saveType;
	private Integer templateType;// 模板类型：1个性，0普通
	private Integer sendType;// 发送方式：1、接口；2、页面
	private Integer state;// 状态 1发送中 2发送到运营商 3发送成功 4发送失败 5发送超时
	private Date channelReportTime;// 网关异步状态报告返回时间
	private String channelReportDesc;// 状态报告描述
	private String channelReportStr;

	public Long getServiceCodeId() {
		return serviceCodeId;
	}

	public void setServiceCodeId(Long serviceCodeId) {
		this.serviceCodeId = serviceCodeId;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public Long getContentTypeId() {
		return contentTypeId;
	}

	public void setContentTypeId(Long contentTypeId) {
		this.contentTypeId = contentTypeId;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Integer getSaveType() {
		return saveType;
	}

	public void setSaveType(Integer saveType) {
		this.saveType = saveType;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getChannelReportTime() {
		return channelReportTime;
	}

	public void setChannelReportTime(Date channelReportTime) {
		this.channelReportTime = channelReportTime;
	}

	public String getChannelReportDesc() {
		return channelReportDesc;
	}

	public void setChannelReportDesc(String channelReportDesc) {
		this.channelReportDesc = channelReportDesc;
	}

	public String getChannelReportStr() {
		return channelReportStr;
	}

	public void setChannelReportStr(String channelReportStr) {
		this.channelReportStr = channelReportStr;
	}

}
