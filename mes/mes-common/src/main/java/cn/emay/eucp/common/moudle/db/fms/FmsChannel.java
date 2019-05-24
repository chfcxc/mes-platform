package cn.emay.eucp.common.moudle.db.fms;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dejun
 * @version 创建时间：2019年4月25日 下午2:15:37 类说明
 */
public class FmsChannel implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static int CHANNEL_STATE_ON = 1;// 通道开启状态
	public final static int CHANNEL_STATE_OFF = 0;// 通道关闭状态
	public final static int IS_NEED_REPORT = 1;// 需要报备
	public final static int IS_NOT_NEED_REPORT = 0;// 不需要
	private Long id;
	private String channelName;// 通道名称
	private String channelNumber;// 通道号
	private Integer sendSpeed;// 发送速度
	private String templateType;// 模板类型：1个性，0普通
	private Long businessTypeId;// 业务类型id
	private String providers;// 允许发送的运营商 1.移动 2.电信 3.联通 例如1#2#3
	private Integer cmccLimit;// 移动通道字数限制
	private Integer cuccLimit;// 联通通道字数限制
	private Integer ctccLimit;// 电信通道字数限制
	private Integer isNeedReport;// 是否需要报备 0 不需要1需要报备
	private Integer reportType;// 报备类型 0线上报备线上回模板ID，1线上报备线下回模板ID，2线下报备线上回模板ID，3线下报备线下回模板ID
	private Integer state;// 状态 0 停用1启用
	private Date createTime;// 生成日期
	private Long userId;// 用户id
	private Integer saveType;
	private String contentType;
	private String businessType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Integer getSendSpeed() {
		return sendSpeed;
	}

	public void setSendSpeed(Integer sendSpeed) {
		this.sendSpeed = sendSpeed;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

	public Long getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getProviders() {
		return providers;
	}

	public void setProviders(String providers) {
		this.providers = providers;
	}

	public Integer getCmccLimit() {
		return cmccLimit;
	}

	public void setCmccLimit(Integer cmccLimit) {
		this.cmccLimit = cmccLimit;
	}

	public Integer getCuccLimit() {
		return cuccLimit;
	}

	public void setCuccLimit(Integer cuccLimit) {
		this.cuccLimit = cuccLimit;
	}

	public Integer getCtccLimit() {
		return ctccLimit;
	}

	public void setCtccLimit(Integer ctccLimit) {
		this.ctccLimit = ctccLimit;
	}

	public Integer getIsNeedReport() {
		return isNeedReport;
	}

	public void setIsNeedReport(Integer isNeedReport) {
		this.isNeedReport = isNeedReport;
	}

	public Integer getReportType() {
		return reportType;
	}

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getSaveType() {
		return saveType;
	}

	public void setSaveType(Integer saveType) {
		this.saveType = saveType;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

}
