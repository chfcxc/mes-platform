package cn.emay.eucp.common.moudle.db.system;

import java.util.Date;

/**
 * 问题反馈
 * 
 */
public class Feedback implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public final static int STATE_NO_HANDLE = 0;// 未处理
	public final static int STATE_HANDLE = 1;// 已处理

	private Long id;
	private String feedbackContent;// 反馈内容
	private String email;// 邮箱
	private String mobile;// 手机号
	private String qq;// qq
	private Date createTime;// 创建时间
	private Long operatorId;// 创建者
	private Integer handleState;// 处理状态
	private Long handleUserId;// 处理人
	private Date handleTime;// 处理时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFeedbackContent() {
		return feedbackContent;
	}

	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
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

	public Integer getHandleState() {
		return handleState;
	}

	public void setHandleState(Integer handleState) {
		this.handleState = handleState;
	}

	public Long getHandleUserId() {
		return handleUserId;
	}

	public void setHandleUserId(Long handleUserId) {
		this.handleUserId = handleUserId;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

}