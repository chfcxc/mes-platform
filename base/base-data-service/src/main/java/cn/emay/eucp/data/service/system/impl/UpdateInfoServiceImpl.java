package cn.emay.eucp.data.service.system.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.cache.CenterBaseRedisCache;
import cn.emay.eucp.common.moudle.db.system.UpdateInfo;
import cn.emay.eucp.data.dao.system.UpdateInfoDao;
import cn.emay.eucp.data.service.system.UpdateInfoService;
import cn.emay.util.DateUtil;

@Service("updateInfoService")
public class UpdateInfoServiceImpl implements UpdateInfoService {
	@Resource
	private 	UpdateInfoDao updateInfoDao;

	@Resource
	private RedisClient redis;
	
	@Override
	public List<UpdateInfo> findAllList() {
		return updateInfoDao.findAllList();
	}

	@Override
	public List<UpdateInfo> selectInfoByCondition(String version, String updateInfo) {
		return updateInfoDao.selectInfoByCondition(version, updateInfo);
	}

	@Override
	public int[] saveBatchUpdateInfo(List<Object[]> params) {
		return updateInfoDao.saveBatchUpdateInfo(params);
	}
 
	 
	@Override
	public void updateSystemInfo(UpdateInfo  info) {
		 updateInfoDao.update(info);
	}

	@Override
	public void saveUpdateInfo(String versionInfo, Date pubTime, Integer subSystem, Integer businessType, String updateInfo) {
		UpdateInfo  info=new UpdateInfo();
		Date date=new Date();
		info.setBusniessType(businessType);
		info.setCreateTime(date);
		info.setPubTime(DateUtil.toString(pubTime, "yyyy-MM-dd"));
		info.setUpdateInfo(updateInfo);
		info.setSubSystem(subSystem);
		info.setVersionInfo(versionInfo);
		updateInfoDao.save(info);
		
		//移除旧的更新信息
		List<UpdateInfo> infos = updateInfoDao.findBySystemType(info.getSubSystem());
		String flag="";
		if(null!=infos && infos.size()>0) {
			for (UpdateInfo info2 : infos) {
				if(UpdateInfo.STATE_SUB_SYSTEM_MANNGER==subSystem) {
					flag="manage";
				}else if(UpdateInfo.STATE_SUB_SYSTEM_CLIENT==subSystem) {
					flag="client";
				}else if(UpdateInfo.STATE_SUB_SYSTEM_SALE==subSystem) {
					flag="sales";
				}
				redis.del(CenterBaseRedisCache.SYSTEM_UPDATE_INFO_NEW+"_"+flag.toUpperCase()+"_"+info2.getId());
			}
		}
		
	}

	@Override
	public Page<UpdateInfo> selectUpdateInfo(String versionInfo, String updateInfo, Integer start, Integer limit) {
		return updateInfoDao.selectUpdateInfo(versionInfo, updateInfo, start, limit);
	}

	public List<String> findVersionList(){
		return updateInfoDao.findVersionList();
	}

	@Override
	public UpdateInfo findById(Long id) {
		return updateInfoDao.findById(id);
	}

	@Override
	public List<UpdateInfo> findBySystemType(Integer subSystem) {
		return  updateInfoDao.findBySystemType(subSystem);
	}

	@Override
	public Boolean getIsReadNewUpdateInfo(Long id, Long userId, String flag) {
		Boolean flagReturn=false;
		String isRead = redis.hget(CenterBaseRedisCache.SYSTEM_UPDATE_INFO_NEW+"_"+flag.toUpperCase()+"_"+id, String.valueOf(userId));
		if(null!=isRead) {
			flagReturn=false;
		}else {
			redis.hset(CenterBaseRedisCache.SYSTEM_UPDATE_INFO_NEW+"_"+flag.toUpperCase()+"_"+id, String.valueOf(userId), userId, 0);
			flagReturn=true;
		}
		return  flagReturn;
	}
	
}
