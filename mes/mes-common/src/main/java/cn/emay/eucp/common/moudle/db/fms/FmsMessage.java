package cn.emay.eucp.common.moudle.db.fms;

import java.io.Serializable;
import java.util.Date;

/** @author dejun
 * @version 创建时间：2019年4月25日 上午11:02:53 类说明 */
public class FmsMessage implements Serializable {

	/** @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) */
	private static final long serialVersionUID = 1L;
	public final static int STATE_SENDING = 1;// 发送中
	public final static int STATE_SENDED = 2;// 发送到运营商
	public final static int STATE_DELIVRD = 3;// 发送成功
	public final static int STATE_FAIL = 4;// 发送失败
	public final static int STATE_TIMEOUT = 5;// 发送超时

	public final static int TEMPLATE_TYPE_PERSON = 1;// 1个性
	public final static int TEMPLATE_TYPE = 0;// 普通
	private String id;
	private String title;// 标题
	private String customFmsId;// 客户自定义唯一标识
	private String templateId;// 模板id
	private String appId;// appID
	private String mobile;// 手机号
	private String content;// 全量内容
	private String sendValue;// 客户提交变量值
	private Long enterpriseId;// 企业id
	private String batchNumber;// 批次号
	private Integer state;// 状态 1发送中 2发送到运营商 3发送成功 4发送失败 5发送超时
	private Integer sendType;// 发送方式：1、接口；2、页面
	private Integer templateType;// 模板类型：1个性，0普通
	private Long userId;// 用户id
	private Date sendTime;// 发送时间
	private Long channelId;// 通道id
	private Long contentTypeId;// 内容类型id
	private String operatorCode;// 运营商编码
	private String channelReportState;// 状态报告状态
	private String channelReportDesc;// 状态报告描述
	private Date channelResponseTime;// 响应比对完成时间
	private Date channelReportTime;// 网关异步状态报告返回时间
	private String operatorId;// 运营商id
	private Date submitTime;// 提交时间
	private Long serviceCodeId;// 服务号ID

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomFmsId() {
		return customFmsId;
	}

	public void setCustomFmsId(String customFmsId) {
		this.customFmsId = customFmsId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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

	public String getSendValue() {
		return sendValue;
	}

	public void setSendValue(String sendValue) {
		this.sendValue = sendValue;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getOperatorCode() {
		return operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public String getChannelReportState() {
		return channelReportState;
	}

	public void setChannelReportState(String channelReportState) {
		this.channelReportState = channelReportState;
	}

	public String getChannelReportDesc() {
		return channelReportDesc;
	}

	public void setChannelReportDesc(String channelReportDesc) {
		this.channelReportDesc = channelReportDesc;
	}

	public Date getChannelResponseTime() {
		return channelResponseTime;
	}

	public void setChannelResponseTime(Date channelResponseTime) {
		this.channelResponseTime = channelResponseTime;
	}

	public Date getChannelReportTime() {
		return channelReportTime;
	}

	public void setChannelReportTime(Date channelReportTime) {
		this.channelReportTime = channelReportTime;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Long getContentTypeId() {
		return contentTypeId;
	}

	public void setContentTypeId(Long contentTypeId) {
		this.contentTypeId = contentTypeId;
	}

	public Long getServiceCodeId() {
		return serviceCodeId;
	}

	public void setServiceCodeId(Long serviceCodeId) {
		this.serviceCodeId = serviceCodeId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
