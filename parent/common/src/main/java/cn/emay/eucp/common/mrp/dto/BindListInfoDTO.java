package cn.emay.eucp.common.mrp.dto;

import java.io.Serializable;
import java.util.List;

public class BindListInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ServiceCodeDTO> bindingList;
	private List<PlatformCodeDTO> bindPlatformCodelist;
	private String type;
	private String code;

	public List<ServiceCodeDTO> getBindingList() {
		return bindingList;
	}

	public void setBindingList(List<ServiceCodeDTO> bindingList) {
		this.bindingList = bindingList;
	}

	public List<PlatformCodeDTO> getBindPlatformCodelist() {
		return bindPlatformCodelist;
	}

	public void setBindPlatformCodelist(List<PlatformCodeDTO> bindPlatformCodelist) {
		this.bindPlatformCodelist = bindPlatformCodelist;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
