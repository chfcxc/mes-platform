package cn.emay.eucp.common.moudle.db.system;

import java.io.Serializable;
import java.util.Date;

/**
 * 导出信息
 * 
 */
public class Export implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int STATE_WAITING_GENERATION = 0;// 文件待生成
	public static final int STATE_GENERATION = 1;// 文件生成中
	public static final int STATE_GENERATION_COMPLETED = 2;// 文件生成完成
	public static final int STATE_GENERATION_EXCEPTION = 3;// 文件生成异常

	public static final String BUSINESS_TYPE_SMS = "SMS";

	private Long id;
	private Long userId;// 用户id
	private String module;// 导出模块
	private String queryCriteria;// 查询条件
	private Date createTime;// 创建时间
	private Integer state;// 状态[0-文件待生成,1-文件生成中,2-文件生成完成,3-文件生成异常]
	private String path;// 文件生成路径
	private Date startTime;// 文件生成开始时间
	private Date finishTime;// 文件生成完成时间
	private String fileName;// 文件名称
	private String systemFor;// 所属系统
	private String businessType;// 业务类别 sms-短信

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

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getQueryCriteria() {
		return queryCriteria;
	}

	public void setQueryCriteria(String queryCriteria) {
		this.queryCriteria = queryCriteria;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSystemFor() {
		return systemFor;
	}

	public void setSystemFor(String systemFor) {
		this.systemFor = systemFor;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

}
