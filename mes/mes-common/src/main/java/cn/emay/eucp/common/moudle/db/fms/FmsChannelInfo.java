package cn.emay.eucp.common.moudle.db.fms;

import java.io.Serializable;

/**
 * @author dejun
 * @version 创建时间：2019年4月25日 下午2:43:39 类说明
 */
public class FmsChannelInfo implements Serializable {

	/** @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) */
	private static final long serialVersionUID = 1L;

	private Long id;
	private Long channelId;// 通道id
	private String propertieyName;// 属性
	private String propertieyValue;// 属性值
	private String propertyKey;// 属性值
	private Long userId;// 用户id

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getPropertieyName() {
		return propertieyName;
	}

	public void setPropertieyName(String propertieyName) {
		this.propertieyName = propertieyName;
	}

	public String getPropertieyValue() {
		return propertieyValue;
	}

	public void setPropertieyValue(String propertieyValue) {
		this.propertieyValue = propertieyValue;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}

}
