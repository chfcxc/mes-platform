package cn.emay.eucp.common.dto.fms.message;

import java.io.Serializable;
import java.util.Date;

/** @author dejun
 * @version 创建时间：2019年4月30日 上午11:58:08 类说明 */
public class FmsBatchPageDto implements Serializable {

	/** @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) */
	private static final long serialVersionUID = 1L;
	private Long serviceCodeId;// 服务号ID
	private String batchNumber;// 批次号
	private String title;// 标题
	private Integer sendTotal;// 发送总数
	private Integer successTotal;// 成功总数
	private Integer errorTextNumber;// 内容错误数量
	private Integer unknownTotal;// 未识别
	private Integer repeatTotal;// 重复总数
	private Integer cmccNumber;// 移动发送总数
	private Integer cuccNumber;// 联通发送总数
	private Integer ctccNumber;// 电信发送总数
	private String content;// 报备模板内容
	private Integer sendType;// 发送方式：1、接口；2、页面
	private Date submitTime;// 提交时间
	private Long contentTypeId;// 内容类型id
	private String businessType;// 业务类型名称
	private String contentType;// 内容类型名称
	private Integer saveType;// 保存类型1 保存 2 不保存
	private Integer templateType;// 模板类型：1个性，0普通
	private Integer state;// 状态-1 标识编辑状态 0 待解析 1 解析中 2 待发送 3 发送完成 4 提交数据错误

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSendType() {
		return sendType;
	}

	public void setSendType(Integer sendType) {
		this.sendType = sendType;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getSendTotal() {
		return sendTotal;
	}

	public void setSendTotal(Integer sendTotal) {
		this.sendTotal = sendTotal;
	}

	public Integer getRepeatTotal() {
		return repeatTotal;
	}

	public void setRepeatTotal(Integer repeatTotal) {
		this.repeatTotal = repeatTotal;
	}

	public Integer getErrorTextNumber() {
		return errorTextNumber;
	}

	public void setErrorTextNumber(Integer errorTextNumber) {
		this.errorTextNumber = errorTextNumber;
	}

}
