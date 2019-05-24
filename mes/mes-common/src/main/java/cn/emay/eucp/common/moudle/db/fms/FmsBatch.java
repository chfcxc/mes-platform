package cn.emay.eucp.common.moudle.db.fms;

import java.io.Serializable;
import java.util.Date;

/** @author dejun
 * @version 创建时间：2019年4月25日 上午11:34:11 类说明 */
public class FmsBatch implements Serializable {

	/** @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String title;// 标题
	private String batchNumber;// 批次号
	private String templateId;// 模板id
	private Integer templateType;// 模板类型：1个性，0普通
	private String content;// 模板内容
	private String appId;// appID
	private Date submitTime;// 提交时间
	private Integer state;// 状态 0 待解析 1 解析中 2 待发送 3 发送完成 4 提交数据错误
	private Integer cmccNumber;// 移动发送总数
	private Integer cuccNumber;// 联通发送总数
	private Integer ctccNumber;// 电信发送总数
	private Integer sendTotal;// 发送总数
	private Integer successTotal;// 成功总数
	private Integer errorTextNumber;// 内容错误数量
	private Integer unknownTotal;// 未识别
	private Integer repeatTotal;// 重复
	private Long userId;// 用户id
	private Long serviceCodeId;// 服务号ID
	private Long contentTypeId;
	/** 普通模板类型 */
	public static final int COMMON_TEMPLATE = 0;
	/** 个性模板类型 */
	public static final int PERSONAL_TEMPLATE = 1;
	public static final int WATING_PARSE = 0;
	public static final int PARSING = 1;
	public static final int SEND_FINISH = 3;
	public static final int DATA_ERROR = 4;// 提交数据错误

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getCmccNumber() {
		return cmccNumber;
	}

	public void setCmccNumber(Integer cmccNumber) {
		this.cmccNumber = cmccNumber;
	}

	public Integer getCuccNumber() {
		return cuccNumber;
	}

	public void setCuccNumber(Integer cuccNumber) {
		this.cuccNumber = cuccNumber;
	}

	public Integer getCtccNumber() {
		return ctccNumber;
	}

	public void setCtccNumber(Integer ctccNumber) {
		this.ctccNumber = ctccNumber;
	}

	public Integer getSendTotal() {
		return sendTotal;
	}

	public void setSendTotal(Integer sendTotal) {
		this.sendTotal = sendTotal;
	}

	public Integer getSuccessTotal() {
		return successTotal;
	}

	public void setSuccessTotal(Integer successTotal) {
		this.successTotal = successTotal;
	}

	public Integer getUnknownTotal() {
		return unknownTotal;
	}

	public void setUnknownTotal(Integer unknownTotal) {
		this.unknownTotal = unknownTotal;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getServiceCodeId() {
		return serviceCodeId;
	}

	public void setServiceCodeId(Long serviceCodeId) {
		this.serviceCodeId = serviceCodeId;
	}

	public Long getContentTypeId() {
		return contentTypeId;
	}

	public void setContentTypeId(Long contentTypeId) {
		this.contentTypeId = contentTypeId;
	}

	public Integer getRepeatTotal() {
		return repeatTotal;
	}

	public void setRepeatTotal(Integer repeatTotal) {
		this.repeatTotal = repeatTotal;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getErrorTextNumber() {
		return errorTextNumber;
	}

	public void setErrorTextNumber(Integer errorTextNumber) {
		this.errorTextNumber = errorTextNumber;
	}

}
