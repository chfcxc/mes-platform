package cn.emay.eucp.common.dto.user;

import java.io.Serializable;

/**
 *	账号绑定dto
 */
public class UserBindingDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long userId;//用户id
	private String userName;// 用户名
	private String nameCn;// 客户名称
	private String clientNumber;// 客户编号
	private Integer identity;// 账号类型[1-管理账号,2-普通账号]
	private Integer state;// 状态[0-删除，1-停用，2-启用]
	private String serviceCode;// 绑定的服务号
	private String type;//客户类型
	
	public UserBindingDTO(){
		
	}
	
	public UserBindingDTO(Long userId,String type){
		this.userId=userId;
		this.type=type;
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNameCn() {
		return nameCn;
	}
	public void setNameCn(String nameCn) {
		this.nameCn = nameCn;
	}
	public String getClientNumber() {
		return clientNumber;
	}
	public void setClientNumber(String clientNumber) {
		this.clientNumber = clientNumber;
	}
	public Integer getIdentity() {
		return identity;
	}
	public void setIdentity(Integer identity) {
		this.identity = identity;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
