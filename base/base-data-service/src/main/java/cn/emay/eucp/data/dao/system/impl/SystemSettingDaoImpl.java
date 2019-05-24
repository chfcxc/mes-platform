package cn.emay.eucp.data.dao.system.impl;


import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.eucp.common.moudle.db.system.SystemSetting;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.SystemSettingDao;

/**
 * cn.emay.eucp.common.moudle.db.system.SystemSetting Dao implement
 * 
 * @author frank
 */
@Repository
public class SystemSettingDaoImpl extends PojoDaoImpl<SystemSetting> implements SystemSettingDao {
	
	@Override
	public SystemSetting findSystemSettingByProperty(String fieldName,String value){
		return this.findByProperty(fieldName, value);
	}
	
	@Override
	public void updateBySettingKey(String settingKey,String settingValue){
		if(StringUtils.isEmpty(settingKey)){
			return;
		}
		String sql="update system_settings set setting_value=? where setting_key=?";
		this.getJdbcTemplate().update(sql, settingValue, settingKey);
	}
}