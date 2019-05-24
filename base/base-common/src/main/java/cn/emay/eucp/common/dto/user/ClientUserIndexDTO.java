package cn.emay.eucp.common.dto.user;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.emay.eucp.common.dto.servicemodule.ServiceModuleDto;

/**
 * 客户端首页信息
 * 
 * @author lijunjian
 *
 */
public class ClientUserIndexDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String username;// 用户名
	private String realname;// 真实姓名
	private String mobile;// 手机号
	private String serviceModules;// 客户账号服务模块
	private List<ServiceModuleDto> serviceModulesList;// 系统服务模块
	private Integer identity;// 账号类型[1-管理账号,2-普通账号]
	private String clientName;// 客户名称
	private String enterpriseName;// 企业名称
	private Integer serviceCount;// 已开服务数
	private Integer childCount;// 子账号数
	private Long enterpriseId;//企业id
	private List<UserDTO> childList = new ArrayList<UserDTO>();// 子账号
	// private List<ClientServiceCodeDto> serviceCodeList = new ArrayList<ClientServiceCodeDto>();//服务号
	private Long smsCount = 0l;// 短信发送量
	private BigDecimal flowCount;// 流量发送量
	private Long voiceCount = 0l;// 语音发送量
	private Long imsCount = 0l;// 国际短信发送量

	public ClientUserIndexDTO() {

	}

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getServiceModules() {
		return serviceModules;
	}

	public void setServiceModules(String serviceModules) {
		this.serviceModules = serviceModules;
	}

	public Integer getIdentity() {
		return identity;
	}

	public void setIdentity(Integer identity) {
		this.identity = identity;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getEnterpriseName() {
		return enterpriseName;
	}

	public void setEnterpriseName(String enterpriseName) {
		this.enterpriseName = enterpriseName;
	}

	public Integer getServiceCount() {
		return serviceCount;
	}

	public void setServiceCount(Integer serviceCount) {
		this.serviceCount = serviceCount;
	}

	public Integer getChildCount() {
		return childCount;
	}

	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}

	public List<UserDTO> getChildList() {
		return childList;
	}

	public void setChildList(List<UserDTO> childList) {
		this.childList = childList;
	}

	public Long getSmsCount() {
		return smsCount;
	}

	public void setSmsCount(Long smsCount) {
		this.smsCount = smsCount;
	}

	public BigDecimal getFlowCount() {
		return flowCount;
	}

	public void setFlowCount(BigDecimal flowCount) {
		this.flowCount = flowCount;
	}

	public Long getVoiceCount() {
		return voiceCount;
	}

	public void setVoiceCount(Long voiceCount) {
		this.voiceCount = voiceCount;
	}

	public List<ServiceModuleDto> getServiceModulesList() {
		return serviceModulesList;
	}

	public void setServiceModulesList(List<ServiceModuleDto> moduleList) {
		this.serviceModulesList = moduleList;
	}

	public Long getImsCount() {
		return imsCount;
	}

	public void setImsCount(Long imsCount) {
		this.imsCount = imsCount;
	}

}
