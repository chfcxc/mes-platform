package cn.emay.eucp.common.mrp.dto;

import java.io.Serializable;
import java.util.List;

public class ServiceCodeMapDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4616254265602794767L;
	private List<ServiceCodeDTO> allEffectiveList;
	private List<ServiceCodeDTO> bindingList;
	private List<ServiceCodeDTO> notBoundList;

	public List<ServiceCodeDTO> getAllEffectiveList() {
		return allEffectiveList;
	}

	public void setAllEffectiveList(List<ServiceCodeDTO> allEffectiveList) {
		this.allEffectiveList = allEffectiveList;
	}

	public List<ServiceCodeDTO> getBindingList() {
		return bindingList;
	}

	public void setBindingList(List<ServiceCodeDTO> bindingList) {
		this.bindingList = bindingList;
	}

	public List<ServiceCodeDTO> getNotBoundList() {
		return notBoundList;
	}

	public void setNotBoundList(List<ServiceCodeDTO> notBoundList) {
		this.notBoundList = notBoundList;
	}
	
	
}
