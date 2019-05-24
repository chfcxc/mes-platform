package cn.emay.eucp.common.dto.license;

import java.util.Date;

public class LicenseDTO implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private Long systemEnterpriseId;//客户id
	private String nameCn;//客户名称
	private String linkman;//客户联系人
	private String mobile;//联系方式
	private String product;// 产品
	private String version;// 版本
	private String operatorName;//创建人
	private Date beginTime;// 生效时间
	private Date endTime;// 失效时间
	private String mac;//mac地址
	private String serviceType;//服务类型(1:短信,2:彩信)
	private Date createTime;// 创建时间
	private Integer termOfValidity;//有效期
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNameCn() {
		return nameCn;
	}
	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getSystemEnterpriseId() {
		return systemEnterpriseId;
	}
	public void setSystemEnterpriseId(Long systemEnterpriseId) {
		this.systemEnterpriseId = systemEnterpriseId;
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
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public Integer getTermOfValidity() {
		return termOfValidity;
	}
	public void setTermOfValidity(Integer termOfValidity) {
		this.termOfValidity = termOfValidity;
	}
	
}