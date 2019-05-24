package cn.emay.eucp.common.moudle.db.system;

import java.util.Date;

/**
 * 企业
 * 
 * @author Frank
 * 
 */
public class Enterprise implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public final static String TYPE_NORMAL = "0";// 普通类型
	public final static String TYPE_AGENT = "1";// 代理商类型

	public static final int AUTHORITY_BASIC = 0;// 基础版
	public static final int AUTHORITY_PROFESSIONAL = 1;// 专业版
	public static final int AUTHORITY_PREMIUM = 2;// 高级版

	public static final String VALUE_ADDED_SERVICE_SIMPLE = "1";// 增值服务-短链接简易版
	public static final String VALUE_ADDED_SERVICE_ACCURATE = "2";// 增值服务-短链接精确版

	public static final int VIP_BASE_LEVEL = 0;// 普通
	public static final int VIP_HEAVY_LEVEL = 1;// 大客户
	public static final int VIP_HIGHEST_LEVEL = 2;// vip

	public static final String SMS_SERVICE = "1";// 短信服务
	public static final String FLOW_SERVICE = "2";// 流量服务
	public static final String IMS_SERVICE = "3";// 国际短信服务
	public static final String VOICE_SERVICE = "4";// 语音短信服务

	private Long id;
	private String nameCn;// 企业中文名称（客户名称）
	private String type;// 客户类型：0：普通客户；1：代理商
	private String clientNumber;// 客户编号
	private String linkman;// 联系人
	private String mobile;// 手机号
	private Date createTime;// 创建时间
	private Long operatorId;// 创建人
	private Boolean isDelete;// 是否删除
	private String email;// 邮箱
	private Boolean isVip;// 是否VIP
	private String telephone;// 企业电话
	private String address;// 企业地址
	private String salesName;// 所属销售
	private String remark;
	private Integer authority;// 客户权限
	private String valueAddedService;// 增值服务[1-短链接简易版,2-短链接精确版]
	private String authName;// 客户权限名称
	private Integer viplevel;// 客户等级
	private String serviceType;
	private Date startClientSelectTime;//客户端查询开始时间
	private Date endClientSelectTime;//客户端查询结束时间

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClientNumber() {
		return clientNumber;
	}

	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getIsVip() {
		return isVip;
	}

	public void setIsVip(Boolean isVip) {
		this.isVip = isVip;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSalesName() {
		return salesName;
	}

	public void setSalesName(String salesName) {
		this.salesName = salesName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getAuthority() {
		return authority;
	}

	public void setAuthority(Integer authority) {
		this.authority = authority;
	}

	public String getValueAddedService() {
		return valueAddedService;
	}

	public void setValueAddedService(String valueAddedService) {
		this.valueAddedService = valueAddedService;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public Integer getViplevel() {
		return viplevel;
	}

	public void setViplevel(Integer viplevel) {
		this.viplevel = viplevel;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Date getStartClientSelectTime() {
		return startClientSelectTime;
	}

	public void setStartClientSelectTime(Date startClientSelectTime) {
		this.startClientSelectTime = startClientSelectTime;
	}

	public Date getEndClientSelectTime() {
		return endClientSelectTime;
	}

	public void setEndClientSelectTime(Date endClientSelectTime) {
		this.endClientSelectTime = endClientSelectTime;
	}

}
