package cn.emay.eucp.data.service.system;

import java.util.Date;
import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.UpdateInfo;


public interface UpdateInfoService {
	
	public Page<UpdateInfo> selectUpdateInfo(String  versionInfo, String updateInfo, Integer start, Integer limit);
	
	/**
	 * 查询所有更新信息列表
	 */
	public  List<UpdateInfo>  findAllList();
	
	
	/**
	 * 条件查询获取信息
	 */
	public  List<UpdateInfo>  selectInfoByCondition(String version,String updateInfo);
	
	
	/**
	 * 信息保存
	 */
	public int[] saveBatchUpdateInfo(List<Object[]> params);
	
	
	/**
	 * 系统信息修改
	 */
	public void updateSystemInfo(UpdateInfo  info);
	
	
	public void saveUpdateInfo (String versionInfo,Date pubTime,Integer subSystem,Integer businessType,String updateInfo);

	
	public List<String> findVersionList();
	
	
	public  UpdateInfo  findById(Long  id);
	
	public List<UpdateInfo> findBySystemType(Integer subSystem);
	
	public  Boolean  getIsReadNewUpdateInfo(Long id,Long userId,String flag);
}
