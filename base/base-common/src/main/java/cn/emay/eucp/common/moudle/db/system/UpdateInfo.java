package cn.emay.eucp.common.moudle.db.system;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统更新日志表
 * 
 * @author gh
 */
public class UpdateInfo implements Serializable {

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
	private String versionInfo;// 版本号
	private String pubTime;// 发布时间
	private Integer subSystem;// 所属系统 1:管理端 2：销售端 3：客户端
	private Integer busniessType;// 业务类型 1:短信 2：彩信 3：流量 4：国际短信
	private String updateInfo;// 更新信息

	private Date createTime;// 创建时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(String versionInfo) {
		this.versionInfo = versionInfo;
	}

	public String getPubTime() {
		return pubTime;
	}

	public void setPubTime(String pubTime) {
		this.pubTime = pubTime;
	}

	public Integer getSubSystem() {
		return subSystem;
	}

	public void setSubSystem(Integer subSystem) {
		this.subSystem = subSystem;
	}

	public Integer getBusniessType() {
		return busniessType;
	}

	public void setBusniessType(Integer busniessType) {
		this.busniessType = busniessType;
	}

	public String getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
