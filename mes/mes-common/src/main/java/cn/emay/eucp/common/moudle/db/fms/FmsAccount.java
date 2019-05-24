package cn.emay.eucp.common.moudle.db.fms;

import java.io.Serializable;

/**
 * @author dejun
 * @version 创建时间：2019年4月25日 下午2:56:13 类说明
 */
public class FmsAccount implements Serializable {

	/** @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) */
	private static final long serialVersionUID = 1L;
	private Long id;

	private Long enterpriseId;// 客户ID

	private Long serviceCodeId;// 服务id

	private String appId;// APPID

	private Long balance;// 余量
	private String remark;// 备注

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getServiceCodeId() {
		return serviceCodeId;
	}

	public void setServiceCodeId(Long serviceCodeId) {
		this.serviceCodeId = serviceCodeId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Long getBalance() {
		return balance;
	}

	public void setBalance(Long balance) {
		this.balance = balance;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
