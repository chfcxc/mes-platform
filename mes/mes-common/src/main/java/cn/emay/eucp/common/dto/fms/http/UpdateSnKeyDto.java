package cn.emay.eucp.common.dto.fms.http;

import java.io.Serializable;

public class UpdateSnKeyDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String appId;
	private String key;
	private String subTime;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSubTime() {
		return subTime;
	}

	public void setSubTime(String subTime) {
		this.subTime = subTime;
	}

}
