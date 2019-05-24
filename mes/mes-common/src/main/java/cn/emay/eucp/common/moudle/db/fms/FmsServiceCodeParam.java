package cn.emay.eucp.common.moudle.db.fms;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dejun
 * @version 创建时间：2019年4月25日 上午11:57:58 类说明
 */
public class FmsServiceCodeParam implements Serializable {
	public static final int GET_REPORT_TYPE = 2;
	public static final int NOT_GET_REPORT_TYPE = 1;
	private static final long serialVersionUID = 1L;
	private Long id;
	private String appId;
	private String remark;// 备注
	private Integer getReportType;// 是否需要状态报告 1不获取 2主动获取
	private String ipConfiguration;// ip配置
	private Date createTime;// 创建时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getGetReportType() {
		return getReportType;
	}

	public void setGetReportType(Integer getReportType) {
		this.getReportType = getReportType;
	}

	public String getIpConfiguration() {
		return ipConfiguration;
	}

	public void setIpConfiguration(String ipConfiguration) {
		this.ipConfiguration = ipConfiguration;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
