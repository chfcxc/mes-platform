package cn.emay.eucp.inter.http.dto.request;

import java.io.Serializable;


public class FmsBaseRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * appId
	 */
	private String appId;
	
	/**
	 * 请求时间 毫秒数
	 */
	private Long requestTime;

	/**
	 * 请求有效时间 秒
	 */
	private Integer requestValidPeriod;
	

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Long requestTime) {
		this.requestTime = requestTime;
	}

	public Integer getRequestValidPeriod() {
		return requestValidPeriod;
	}

	public void setRequestValidPeriod(Integer requestValidPeriod) {
		this.requestValidPeriod = requestValidPeriod;
	}
	
}
