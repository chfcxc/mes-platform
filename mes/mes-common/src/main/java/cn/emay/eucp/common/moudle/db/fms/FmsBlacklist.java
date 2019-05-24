package cn.emay.eucp.common.moudle.db.fms;

import java.io.Serializable;
import java.util.Date;

/** @author dejun
 * @version 创建时间：2019年4月25日 下午2:51:28 类说明 */
public class FmsBlacklist implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final Integer IS_DELETE_TRUE = 1;
	public static final Integer IS_DELETE_FALSE = 0;
	private Long id;

	private String mobile;// 手机号

	private Integer isDelete;// 是否已删除(0未删除(默认) 1已删除')

	private Date createTime;// 创建时间

	private String remark;// 说明

	private Long userId;// 用户id

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

	public Integer getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
