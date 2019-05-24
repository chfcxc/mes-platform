package cn.emay.eucp.common.dto.fms.channel;

import java.io.Serializable;

public class ChannelSimpleDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long channelId;// ID

	private String channelName;// 通道名

	private Integer auditType;

	public ChannelSimpleDTO() {

	}

	public ChannelSimpleDTO(Long channelId, String channelName, Integer auditType) {
		super();
		this.channelId = channelId;
		this.channelName = channelName;
		this.auditType = auditType;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Integer getAuditType() {
		return auditType;
	}

	public void setAuditType(Integer auditType) {
		this.auditType = auditType;
	}

}
