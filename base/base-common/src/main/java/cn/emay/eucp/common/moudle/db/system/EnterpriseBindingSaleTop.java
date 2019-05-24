package cn.emay.eucp.common.moudle.db.system;

import java.io.Serializable;
import java.util.Date;

/**
 * 企业与销售人员关系实体
 * 
 * @author ChenXiyao
 * 
 */
public class EnterpriseBindingSaleTop implements Serializable {

	private static final long serialVersionUID = 2548545117921689371L;

	private Long id;
	private Long systemEnterpriseBindingSaleId;// 客户与销售关系表主键
	private Long top;// 客户管理置顶排序
	private String lastUpdateMan;
	private Date createTime;
	private String createMan;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSystemEnterpriseBindingSaleId() {
		return systemEnterpriseBindingSaleId;
	}

	public void setSystemEnterpriseBindingSaleId(Long systemEnterpriseBindingSaleId) {
		this.systemEnterpriseBindingSaleId = systemEnterpriseBindingSaleId;
	}

	public Long getTop() {
		return top;
	}

	public void setTop(Long top) {
		this.top = top;
	}

	public String getLastUpdateMan() {
		return lastUpdateMan;
	}

	public void setLastUpdateMan(String lastUpdateMan) {
		this.lastUpdateMan = lastUpdateMan;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateMan() {
		return createMan;
	}

	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}
}
