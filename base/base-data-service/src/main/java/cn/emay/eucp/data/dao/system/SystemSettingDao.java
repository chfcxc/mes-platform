package cn.emay.eucp.data.dao.system;

import cn.emay.eucp.common.moudle.db.system.SystemSetting;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * cn.emay.eucp.common.moudle.db.system.SystemSetting Dao super
 * 
 * @author frank
 */
public interface SystemSettingDao extends BaseSuperDao<SystemSetting> {
	
	/**
	 * 根据属性查询系统配置信息
	 * @param fieldName
	 * @param value
	 * @return
	 */
	SystemSetting findSystemSettingByProperty(String fieldName, String value);

	void updateBySettingKey(String settingKey, String settingValue);

}