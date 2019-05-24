package cn.emay.eucp.common.mrp.dto;

import java.io.Serializable;

public class ServiceCodeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3860536020533470457L;
	private Long id;
	private String serviceCode;

	public ServiceCodeDTO(Long id, String serviceCode) {
		this.id = id;
		this.serviceCode = serviceCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

}
