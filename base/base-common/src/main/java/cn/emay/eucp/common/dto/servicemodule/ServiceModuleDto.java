package cn.emay.eucp.common.dto.servicemodule;

import java.io.Serializable;

/**
 * 系统开通服务dto
 * @author lijunjian
 *
 */
public class ServiceModuleDto implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String serviceModuleName;
	private Integer state;//0开通，1关闭
	
	public ServiceModuleDto(){
		
	}
	
	public ServiceModuleDto(String id,String serviceModuleName,Integer state){
		this.id=id;
		this.serviceModuleName=serviceModuleName;
		this.state=state;
	}
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getServiceModuleName() {
		return serviceModuleName;
	}
	public void setServiceModuleName(String serviceModuleName) {
		this.serviceModuleName = serviceModuleName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	
	

}
