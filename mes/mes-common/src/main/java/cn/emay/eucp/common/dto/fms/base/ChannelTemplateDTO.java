package cn.emay.eucp.common.dto.fms.base;

import java.io.Serializable;

public class ChannelTemplateDTO implements Serializable {

	/**
	 * 通道模板
	 */

	private static final long serialVersionUID = 1L;
	private String countryNumber;// 国家编码
	private String countryCode;// 国家代码
	private String chineseName;// 中文
	private Long channelId;// 通道Id

	public String getCountryNumber() {
		return countryNumber;
	}

	public void setCountryNumber(String countryNumber) {
		this.countryNumber = countryNumber;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getChineseName() {
		return chineseName;
	}

	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}
}
