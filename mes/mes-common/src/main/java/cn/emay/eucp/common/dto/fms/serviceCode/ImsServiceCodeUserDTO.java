package cn.emay.eucp.common.dto.fms.serviceCode;

import java.io.Serializable;

/**
 * 用户关联服务号dto
 * 
 */
public class ImsServiceCodeUserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String serviceCode;
	private String appId;
	private String note;//服务号备注
	private Long userId;//用户id
	
	public ImsServiceCodeUserDTO(){
		
	}
	
	public ImsServiceCodeUserDTO(String serviceCode,String note){
		this.serviceCode=serviceCode;
		this.note=note;
	}
	
	public ImsServiceCodeUserDTO(String serviceCode,String appId,String note){
		this.serviceCode=serviceCode;
		this.appId=appId;
		this.note=note;
	}

	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
}
