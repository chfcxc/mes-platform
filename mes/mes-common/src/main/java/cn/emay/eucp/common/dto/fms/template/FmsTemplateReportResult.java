package cn.emay.eucp.common.dto.fms.template;

import java.io.Serializable;

public class FmsTemplateReportResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer state;// 报备状态：0 报备中，1通过，2拒绝，3不支持
	private String channelTemplateId;// 通道报备id

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getChannelTemplateId() {
		return channelTemplateId;
	}

	public void setChannelTemplateId(String channelTemplateId) {
		this.channelTemplateId = channelTemplateId;
	}

}
