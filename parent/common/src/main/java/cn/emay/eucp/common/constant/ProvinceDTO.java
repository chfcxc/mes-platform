package cn.emay.eucp.common.constant;

import java.io.Serializable;

public class ProvinceDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 编码
	 */
	private String code;
	
	public ProvinceDTO() {
		
	}
	
	public ProvinceDTO(String name,String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
