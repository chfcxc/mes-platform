package cn.emay.eucp.common.mrp.dto;

import java.io.Serializable;

public class PlatformCodeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8279962767178659236L;

	private Long id;
	private String platformCode;

	public PlatformCodeDTO(Long id, String platformCode) {

		this.id = id;
		this.platformCode = platformCode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlatformCode() {
		return platformCode;
	}

	public void setPlatformCode(String platformCode) {
		this.platformCode = platformCode;
	}


}
