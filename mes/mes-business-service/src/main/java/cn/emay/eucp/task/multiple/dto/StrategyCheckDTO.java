package cn.emay.eucp.task.multiple.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.emay.eucp.common.moudle.db.fms.FmsMessage;

/**
 * 策略dto
 * 
 */
public class StrategyCheckDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long serviceCodeId;
	private String appId;// 注册号 必填
	private Long enterpriseId;// 企业id
	private String state;// 状态
	private List<FmsMessage> allMessageList;
	private Map<String, FmsMessage> rightMmsMessageMap;
	private List<FmsMessage> failMessageList;

	public Long getServiceCodeId() {
		return serviceCodeId;
	}

	public void setServiceCodeId(Long serviceCodeId) {
		this.serviceCodeId = serviceCodeId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<FmsMessage> getAllMessageList() {
		return allMessageList;
	}

	public void setAllMessageList(List<FmsMessage> allMessageList) {
		this.allMessageList = allMessageList;
	}

	public Map<String, FmsMessage> getRightMmsMessageMap() {
		return rightMmsMessageMap;
	}

	public void setRightMmsMessageMap(Map<String, FmsMessage> rightMmsMessageMap) {
		this.rightMmsMessageMap = rightMmsMessageMap;
	}

	public List<FmsMessage> getFailMessageList() {
		return failMessageList;
	}

	public void setFailMessageList(List<FmsMessage> failMessageList) {
		this.failMessageList = failMessageList;
	}

}
