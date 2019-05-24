package cn.emay.eucp.common.moudle.db.system;

import java.util.Date;

/**
 * 系统用户
 * 
 * @author Frank
 *
 */
public class User implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public final static int STATE_LOCKING = 3;// 锁定
	public final static int STATE_ON = 2;// 启用
	public final static int STATE_OFF = 1;// 停用
	public final static int STATE_DELETE = 0;// 删除
	
	public final static String USER_TYPE_EMAY = "EMAY";// 公司用户
	public final static String USER_TYPE_CLIENT = "CLIENT";// 客户用户

	private Long id;
	private String username;// 用户名
	private String password;// 密码
	private String realname;// 真实姓名
	private String mobile;// 手机号
	private String email;// 邮箱
	private Date createTime;// 创建时间
	private Integer state;// 状态[0-删除，1-停用，2-启用,3-锁定]
	private String remark;// 说明
	private Long operatorId;// 创建者
	private Date lastChangePasswordTime;// 最后一次修改密码时间[用来判断登陆修改密码]
	private String userType;//用户类型，EMAY-公司用户，CLIENT-客户用户
	
	public User(String realname, String username, String password, String email, String mobile, Long operatorId,String userType) {
		this.realname = realname;
		this.username = username;
		this.password = password;
		this.email = email;
		this.mobile = mobile;
		this.userType = userType;
		this.operatorId = operatorId;
		this.createTime = new Date();
		this.state = STATE_ON;
	}

	public User() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Date getLastChangePasswordTime() {
		return lastChangePasswordTime;
	}

	public void setLastChangePasswordTime(Date lastChangePasswordTime) {
		this.lastChangePasswordTime = lastChangePasswordTime;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
}