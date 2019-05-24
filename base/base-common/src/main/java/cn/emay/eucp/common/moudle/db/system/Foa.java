package cn.emay.eucp.common.moudle.db.system;

import java.io.Serializable;
import java.util.Date;

/**
 * foa表
 * 
 * @author gh
 */
public class Foa implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int STATE_SUB_SYSTEM_MANNGER = 1;// 管理端
	public static final int STATE_SUB_SYSTEM_SALE = 2;// 销售端
	public static final int STATE_SUB_SYSTEM_CLIENT = 3;// 客户端

	public static final int BUSINESS_TYPE_SMS = 1;// 短信
	public static final int BUSINESS_TYPE_MMS = 2;// 彩信
	public static final int BUSINESS_TYPE_FLOW = 3;// 流量
	public static final int BUSINESS_TYPE_IMS = 4;// 国际短信
	public static final int BUSINESS_TYPE_VOICE = 5;// 语音
	private Long id;
	private Integer subSystem;// 所属系统 1:管理端 2：销售端 3：客户端
	private Integer businessType;// 业务类型 1:短信 2：彩信 3：流量 4：国际短信
	private String descProblem;// 问题描述
	private String reply;// 问题答复
	private Date createTime;// 创建时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSubSystem() {
		return subSystem;
	}

	public void setSubSystem(Integer subSystem) {
		this.subSystem = subSystem;
	}

	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}

	public String getDescProblem() {
		return descProblem;
	}

	public void setDescProblem(String descProblem) {
		this.descProblem = descProblem;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
