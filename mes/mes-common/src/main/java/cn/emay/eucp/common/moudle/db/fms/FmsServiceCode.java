package cn.emay.eucp.common.moudle.db.fms;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dejun
 * @version 创建时间：2019年4月25日 上午11:44:19 类说明
 */
public class FmsServiceCode implements Serializable {

	/** @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) */
	private static final long serialVersionUID = 1L;
	public final static int STATE_ENABLE = 0;// 激活
	public final static int STATE_DISABLE = 1;// 停用
	private Long id;
	private String serviceCode;// 服务号
	private String appId;
	private String secretKey;// 密钥
	private Integer state;// 状态 0启用1停用
	private Long enterpriseId;// 企业id
	private Long businessTypeId;// 业务类型id
	private String remark;// 备注
	private Date createTime;// 创建时间

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

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(Long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public Long getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Long businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
