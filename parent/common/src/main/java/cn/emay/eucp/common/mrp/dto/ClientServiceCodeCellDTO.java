package cn.emay.eucp.common.mrp.dto;

import java.io.Serializable;

public class ClientServiceCodeCellDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1506599706037658338L;
	private String name;
	private String key;
	private Object value;

	public ClientServiceCodeCellDTO(String name, String key, Object value) {
		this.name = name;
		this.key = key;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
