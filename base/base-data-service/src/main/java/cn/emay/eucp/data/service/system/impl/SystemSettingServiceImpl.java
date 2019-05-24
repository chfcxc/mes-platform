package cn.emay.eucp.data.service.system.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.system.SystemSetting;
import cn.emay.eucp.data.dao.system.SystemSettingDao;
import cn.emay.eucp.data.service.system.SystemSettingService;

/**
 * cn.emay.eucp.common.moudle.db.system.SystemSetting Service implement
 * 
 * @author frank
 */
@Service("systemSettingService")
public class SystemSettingServiceImpl implements SystemSettingService {

	@Resource
	private SystemSettingDao systemSettingDao;

	@Override
	public List<SystemSetting> findList() {
		return systemSettingDao.findAll();
	}

	@Override
	public Result update(SystemSetting setting) {
		systemSettingDao.update(setting);
		return Result.rightResult();
	}

	@Override
	public SystemSetting findById(Long id) {
		return systemSettingDao.findById(id);
	}

	@Override
	public SystemSetting findSystemSettingByProperty(String value) {
		return systemSettingDao.findSystemSettingByProperty("settingKey", value);
	}
	
	@Override
	public Result updateBySettingKey(String settingKey,String settingValue){
		systemSettingDao.updateBySettingKey(settingKey, settingValue);
		return Result.rightResult();
	}

}