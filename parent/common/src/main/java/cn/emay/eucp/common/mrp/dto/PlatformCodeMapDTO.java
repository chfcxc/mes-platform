package cn.emay.eucp.common.mrp.dto;

import java.io.Serializable;
import java.util.List;

public class PlatformCodeMapDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4533378145506186720L;
	private List<PlatformCodeDTO> notBoundList;
	private List<PlatformCodeDTO> bindingList;
	private List<PlatformCodeDTO> allEffectiveList;

	public List<PlatformCodeDTO> getNotBoundList() {
		return notBoundList;
	}

	public void setNotBoundList(List<PlatformCodeDTO> notBoundList) {
		this.notBoundList = notBoundList;
	}

	public List<PlatformCodeDTO> getBindingList() {
		return bindingList;
	}

	public void setBindingList(List<PlatformCodeDTO> bindingList) {
		this.bindingList = bindingList;
	}

	public List<PlatformCodeDTO> getAllEffectiveList() {
		return allEffectiveList;
	}

	public void setAllEffectiveList(List<PlatformCodeDTO> allEffectiveList) {
		this.allEffectiveList = allEffectiveList;
	}
	
	
}
