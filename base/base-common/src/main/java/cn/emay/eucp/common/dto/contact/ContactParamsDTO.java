package cn.emay.eucp.common.dto.contact;

import java.io.Serializable;
import java.util.Date;

public class ContactParamsDTO implements Serializable {
	private static final long serialVersionUID = 1l;

	private String mobile;// 手机号
	private Long userId;// 用户id
//	private Long enterpriseId;// 企业id
	private String realName;// 姓名
	private String email;// 邮箱
	private String qq;// qq
	private Date birthday;//生日
	private String company;// 单位
	private String position;// 职位
	private String companyAddress;// 公司地址
//	private Integer groupType;// 组类型[0-个人组,1-共享组]
	private Long groupId;//组id
	private Long assignId;//关联表id
	private String groupName;//组名
	
	public ContactParamsDTO () {
		
	}
	
	public ContactParamsDTO (String realName,String mobile, String email,String qq,Long groupId,Date birthday,String company,String position,String companyAddress,Long userId,Long assignId) {
		this.realName = realName;
		this.mobile = mobile;
		this.email = email;
		this.qq = qq;
		this.groupId = groupId;
		this.birthday = birthday;
		this.company = company;
		this.position = position;
		this.companyAddress = companyAddress;
		this.userId = userId;
		this.assignId = assignId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Long getAssignId() {
		return assignId;
	}

	public void setAssignId(Long assignId) {
		this.assignId = assignId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}


}
