package cn.emay.eucp.common.dto.enterprise;

import java.io.Serializable;

public class EnterpriseUserDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id;//企业id
	private String nameCn;//企业名称
	private String realname;//销售经理名称
	
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
	public String getRealname() {
		return realname;
	}
	public void setRealname(String realname) {
		this.realname = realname;
	}
	

}
