package cn.emay.eucp.common.dto.log;

import java.io.Serializable;
import java.util.Date;

import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;

/**
 * 用户操作日志
 * 
 * @author Frank
 *
 */
public class ManageUserOperLogDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String OPERATE_ADD = "ADD";
	public static final String OPERATE_DELETE = "DELETE";
	public static final String OPERATE_MODIFY = "MODIFY";

	private Long id;//
	private Long userId;// 用户ID
	private String username;// 用户名
	private String module;// 模块名称
	private String content;// 内容
	private String type;// 操作类型[ADD-增，DELETE-删，MODIFY-改，SELECT-查]
	private Date operTime;// 操作时间[yyyy-MM-dd HH:mm:ss]
	private String service;// 服务模块
	private String omitContent;// 省略内容

	public ManageUserOperLogDTO() {

	}

	public ManageUserOperLogDTO(ManageUserOperLog log) {
		this.id = log.getId();
		this.userId = log.getUserId();
		this.username = log.getUsername();
		this.module = log.getModule();
		this.content = log.getContent();
		this.type = log.getType();
		this.operTime = log.getOperTime();
		this.service = log.getService();
		if (log.getContent().length() > 20) {
			this.omitContent = log.getContent().substring(0, 20) + ".....";
		} else {
			this.omitContent = log.getContent();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getOmitContent() {
		return omitContent;
	}

	public void setOmitContent(String omitContent) {
		this.omitContent = omitContent;
	}
}
