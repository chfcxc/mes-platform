package cn.emay.eucp.common.mrp.dto;

import java.io.Serializable;

public class ServiceCodeAndPlatformMapDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4139270669692914639L;
	private ServiceCodeMapDTO userServiceCodeAssignMap;
	private PlatformCodeMapDTO userPlatformCodeAssignMap;
	private String type;

	public ServiceCodeMapDTO getUserServiceCodeAssignMap() {
		return userServiceCodeAssignMap;
	}

	public void setUserServiceCodeAssignMap(ServiceCodeMapDTO userServiceCodeAssignMap) {
		this.userServiceCodeAssignMap = userServiceCodeAssignMap;
	}

	public PlatformCodeMapDTO getUserPlatformCodeAssignMap() {
		return userPlatformCodeAssignMap;
	}

	public void setUserPlatformCodeAssignMap(PlatformCodeMapDTO userPlatformCodeAssignMap) {
		this.userPlatformCodeAssignMap = userPlatformCodeAssignMap;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
