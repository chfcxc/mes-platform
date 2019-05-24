package cn.emay.eucp.common.dto.fms.channel;

public class FmsChannelBusinessDTO {

	private Integer saveType;
	private String saveDesc;// 保存描述
	private String contentType;
	private String businessType;
	private Integer reportType;// 报备类型 0线上报备线上回模板ID，1线上报备线下回模板ID，2线下报备线上回模板ID，3线下报备线下回模板ID
	private String reportTypeDesc;// 报备描述

	public Integer getSaveType() {
		return saveType;
	}

	public void setSaveType(Integer saveType) {
		this.saveType = saveType;
	}

	public String getSaveDesc() {
		return saveDesc;
	}

	public void setSaveDesc(String saveDesc) {
		this.saveDesc = saveDesc;
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

	public Integer getReportType() {
		return reportType;
	}

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}

	public String getReportTypeDesc() {
		return reportTypeDesc;
	}

	public void setReportTypeDesc(String reportTypeDesc) {
		this.reportTypeDesc = reportTypeDesc;
	}

}
