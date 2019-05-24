package cn.emay.eucp.data.service.system;

import java.util.List;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.system.SystemSetting;

/**
 * cn.emay.eucp.common.moudle.db.system.SystemSetting Service Super
 * 
 * @author frank
 */
public interface SystemSettingService {

	/**
	 * 系统配置列表
	 * 
	 * @return
	 */
	List<SystemSetting> findList();

	/**
	 * 修改系统配置
	 * 
	 * @param id
	 * @param settingValue
	 */
	Result update(SystemSetting setting);

	/**
	 * 根据id查询配置
	 * 
	 * @param id
	 * @return
	 */
	SystemSetting findById(Long id);

	/**
	 * 根据属性查询系统配置信息
	 */
	SystemSetting findSystemSettingByProperty(String value);

	Result updateBySettingKey(String settingKey, String settingValue);

}