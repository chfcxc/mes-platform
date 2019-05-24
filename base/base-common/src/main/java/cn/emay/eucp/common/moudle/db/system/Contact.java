package cn.emay.eucp.common.moudle.db.system;

import java.util.Date;

/**
 *   联系人
 * 
 */
public class Contact implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public final static int GROUP_TYPE_PERSONAL = 0;// 组类型-个人组
	public final static int GROUP_TYPE_SHARE = 1;// 组类型-共享组

	private Long id;
	private String mobile;// 手机号
//	private Integer groupType;// 组类型[0-个人组,1-共享组]
	private Long userId;// 用户id
//	private Long enterpriseId;// 企业id
	private String realName;// 姓名
	private String email;// 邮箱
	private String qq;// qq
	private Date birthday;//生日
	private String company;// 单位
	private String position;// 职位
	private String companyAddress;// 公司地址
	private Date createTime;// 创建时间
	private Boolean isDelete;// 是否删除

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

}
