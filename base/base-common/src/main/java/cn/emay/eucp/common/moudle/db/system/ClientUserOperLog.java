package cn.emay.eucp.common.moudle.db.system;

import java.util.Date;

public class ClientUserOperLog implements java.io.Serializable {

	private static final long serialVersionUID = -4186837732819398407L;
	// Fields
	public static final String OPERATE_ADD = "ADD";
	public static final String OPERATE_DELETE = "DELETE";
	public static final String OPERATE_MODIFY = "MODIFY";
	public static final String OPERATE_DOWNLOAD = "DOWNLOAD";
	private Long id;
	private Long userId;
	private String username;
	private String module;
	private String content;
	private String operType;
	private Date operTime;
	private String service;

	// Constructors

	/** default constructor */
	public ClientUserOperLog() {
	}

	/** full constructor */
	public ClientUserOperLog(Long userId, String username, String module, String content, String operType,
			Date operTime, String service) {
		this.userId = userId;
		this.username = username;
		this.module = module;
		this.content = content;
		this.operType = operType;
		this.operTime = operTime;
		this.service = service;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getModule() {
		return this.module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOperType() {
		return this.operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public Date getOperTime() {
		return this.operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public String getService() {
		return this.service;
	}

	public void setService(String service) {
		this.service = service;
	}

}