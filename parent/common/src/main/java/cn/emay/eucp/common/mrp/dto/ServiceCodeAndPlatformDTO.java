package cn.emay.eucp.common.mrp.dto;

import java.io.Serializable;
import java.util.List;

public class ServiceCodeAndPlatformDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8418966014889278766L;
	private List<ServiceCodeDTO> serviceCodeList;
	private List<PlatformCodeDTO> platformCodeList;
	private String type;

	public List<ServiceCodeDTO> getServiceCodeList() {
		return serviceCodeList;
	}

	public void setServiceCodeList(List<ServiceCodeDTO> serviceCodeList) {
		this.serviceCodeList = serviceCodeList;
	}

	public List<PlatformCodeDTO> getPlatformCodeList() {
		return platformCodeList;
	}

	public void setPlatformCodeList(List<PlatformCodeDTO> platformCodeList) {
		this.platformCodeList = platformCodeList;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
