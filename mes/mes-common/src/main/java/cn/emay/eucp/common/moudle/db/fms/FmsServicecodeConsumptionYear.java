package cn.emay.eucp.common.moudle.db.fms;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dejun
 * @version 创建时间：2019年4月25日 下午3:12:03 类说明
 */
public class FmsServicecodeConsumptionYear implements Serializable {

	/** @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) */
	private static final long serialVersionUID = 1L;
	private Long id;
	private Long servicecodeId;// 服务号ID
	private Long enterpriseId;// 企业id
	private String appId;
	private Integer totalNumber;// 提交总数
	private Integer cmccNumber;// 移动总数
	private Integer cuccNumber;// 联通总数
	private Integer ctccNumber;// 电信总数
	private Integer successNumber;// 成功总数
	private Integer cmccSuccessNumber;// 移动成功总数
	private Integer cuccSuccessNumber;// 联通成功总数
	private Integer ctccSuccessNumber;// 电信成功总数
	private Integer failNumber;// 失败总数
	private Integer cmccFailNumber;// 移动失败总数
	private Integer cuccFailNumber;// 联通失败总数
	private Integer ctccFailNumber;// 电信失败总数
	private Integer unknowNumber;// 未知总数
	private Integer timeoutNumber;// 超时总数
	private Integer cmccTimeoutNumber;// 移动超时总数
	private Integer cuccTimeoutNumber;// 联通超时总数
	private Integer ctccTimeoutNumber;// 电信超时总数
	private Date reportTime;// 统计时间
	private Date createTime;// 创建时间
	private String reportTimeStr;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getServicecodeId() {
		return servicecodeId;
	}

	public void setServicecodeId(Long servicecodeId) {
		this.servicecodeId = servicecodeId;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
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

	public Integer getSuccessNumber() {
		return successNumber;
	}

	public void setSuccessNumber(Integer successNumber) {
		this.successNumber = successNumber;
	}

	public Integer getCmccSuccessNumber() {
		return cmccSuccessNumber;
	}

	public void setCmccSuccessNumber(Integer cmccSuccessNumber) {
		this.cmccSuccessNumber = cmccSuccessNumber;
	}

	public Integer getCuccSuccessNumber() {
		return cuccSuccessNumber;
	}

	public void setCuccSuccessNumber(Integer cuccSuccessNumber) {
		this.cuccSuccessNumber = cuccSuccessNumber;
	}

	public Integer getCtccSuccessNumber() {
		return ctccSuccessNumber;
	}

	public void setCtccSuccessNumber(Integer ctccSuccessNumber) {
		this.ctccSuccessNumber = ctccSuccessNumber;
	}

	public Integer getFailNumber() {
		return failNumber;
	}

	public void setFailNumber(Integer failNumber) {
		this.failNumber = failNumber;
	}

	public Integer getCmccFailNumber() {
		return cmccFailNumber;
	}

	public void setCmccFailNumber(Integer cmccFailNumber) {
		this.cmccFailNumber = cmccFailNumber;
	}

	public Integer getCuccFailNumber() {
		return cuccFailNumber;
	}

	public void setCuccFailNumber(Integer cuccFailNumber) {
		this.cuccFailNumber = cuccFailNumber;
	}

	public Integer getCtccFailNumber() {
		return ctccFailNumber;
	}

	public void setCtccFailNumber(Integer ctccFailNumber) {
		this.ctccFailNumber = ctccFailNumber;
	}

	public Integer getUnknowNumber() {
		return unknowNumber;
	}

	public void setUnknowNumber(Integer unknowNumber) {
		this.unknowNumber = unknowNumber;
	}

	public Integer getTimeoutNumber() {
		return timeoutNumber;
	}

	public void setTimeoutNumber(Integer timeoutNumber) {
		this.timeoutNumber = timeoutNumber;
	}

	public Integer getCmccTimeoutNumber() {
		return cmccTimeoutNumber;
	}

	public void setCmccTimeoutNumber(Integer cmccTimeoutNumber) {
		this.cmccTimeoutNumber = cmccTimeoutNumber;
	}

	public Integer getCuccTimeoutNumber() {
		return cuccTimeoutNumber;
	}

	public void setCuccTimeoutNumber(Integer cuccTimeoutNumber) {
		this.cuccTimeoutNumber = cuccTimeoutNumber;
	}

	public Integer getCtccTimeoutNumber() {
		return ctccTimeoutNumber;
	}

	public void setCtccTimeoutNumber(Integer ctccTimeoutNumber) {
		this.ctccTimeoutNumber = ctccTimeoutNumber;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getReportTimeStr() {
		return reportTimeStr;
	}

	public void setReportTimeStr(String reportTimeStr) {
		this.reportTimeStr = reportTimeStr;
	}

}
