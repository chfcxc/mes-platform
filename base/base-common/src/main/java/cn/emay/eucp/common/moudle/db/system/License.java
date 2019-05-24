package cn.emay.eucp.common.moudle.db.system;

import java.util.Date;

public class License implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public final static String SMS_SERVICE_TYPE = "1";// 短信服务
	public final static String MMS_SERVICE_TYPE = "2";// 彩信服务
	
	private Long id;
	private Long systemEnterpriseId;//客户id
	private String product;// 产品
	private String version;// 版本
	private Date beginTime;// 生效时间
	private Date endTime;// 失效时间
	private String mac;//mac地址
	private String serviceType;//服务类型(1:短信,2:彩信)
	private Long operatorId;//创建人id
	private String operatorName;//创建人
	private Date createTime;// 创建时间
	
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSystemEnterpriseId() {
		return systemEnterpriseId;
	}
	public void setSystemEnterpriseId(Long systemEnterpriseId) {
		this.systemEnterpriseId = systemEnterpriseId;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public Long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}