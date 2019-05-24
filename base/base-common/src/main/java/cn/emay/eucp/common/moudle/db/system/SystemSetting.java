package cn.emay.eucp.common.moudle.db.system;

/**
 * 系统配置
 * 
 * @author lijunjian
 */
public class SystemSetting implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String depict;// 描述
	private String settingKey;// 设置键
	private String settingValue;// 设置值
	private String unit;// 单位
	private String remark;// 备注

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepict() {
		return depict;
	}

	public void setDepict(String depict) {
		this.depict = depict;
	}

	public String getSettingKey() {
		return settingKey;
	}

	public void setSettingKey(String settingKey) {
		this.settingKey = settingKey;
	}

	public String getSettingValue() {
		return settingValue;
	}

	public void setSettingValue(String settingValue) {
		this.settingValue = settingValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}