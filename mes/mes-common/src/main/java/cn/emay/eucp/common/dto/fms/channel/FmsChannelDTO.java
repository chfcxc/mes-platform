/**
 * 
 */
package cn.emay.eucp.common.dto.fms.channel;

import java.io.Serializable;

/**
 * 通道DTO
 * 
 * @author Administrator
 */
public class FmsChannelDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;// ID
	private String channelName;// 通道名
	private String channelNumber;// 通道号
	private Integer state;// '状态 1 启用 2 停用 3异常
	private String providers;
	private String templateType;
	private Integer saveType;
	private String contentType;
	private String businessType;
	private Long businessTypeId;

	public FmsChannelDTO() {

	}

	public FmsChannelDTO(Long id, String channelName) {
		this.id = id;
		this.channelName = channelName;
	}

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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getChannelNumber() {
		return channelNumber;
	}

	public void setChannelNumber(String channelNumber) {
		this.channelNumber = channelNumber;
	}

	public String getProviders() {
		return providers;
	}

	public void setProviders(String providers) {
		this.providers = providers;
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

	public Long getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}

}
