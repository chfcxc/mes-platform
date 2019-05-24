package cn.emay.eucp.common.moudle.db.fms;

import java.io.Serializable;

/** @author dejun
 * @version 创建时间：2019年4月25日 上午11:52:00 类说明 */
public class FmsUserServiceCodeAssign implements Serializable {

	/** @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long userId;// 用户ID
	private Long serviceCodeId;// 服务号ID

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

	public Long getServiceCodeId() {
		return serviceCodeId;
	}

	public void setServiceCodeId(Long serviceCodeId) {
		this.serviceCodeId = serviceCodeId;
	}

}
