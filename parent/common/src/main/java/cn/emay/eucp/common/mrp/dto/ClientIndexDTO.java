package cn.emay.eucp.common.mrp.dto;

import java.io.Serializable;

public class ClientIndexDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String serviceModules;// 客户账号服务模块
	private Integer serviceCount;// 已开服务数
	private Long enterpriseId;// 企业id
	private Long count = 0l;// 发送量

	public String getServiceModules() {
		return serviceModules;
	}

	public void setServiceModules(String serviceModules) {
		this.serviceModules = serviceModules;
	}

	public Integer getServiceCount() {
		return serviceCount;
	}

	public void setServiceCount(Integer serviceCount) {
		this.serviceCount = serviceCount;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
