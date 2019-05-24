package cn.emay.eucp.data.dao.system;

import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.UpdateInfo;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * 
 * @author gh
 */
public interface UpdateInfoDao extends BaseSuperDao<UpdateInfo> {

	/**
	 * 获取分页信息
	 * @param versionInfo
	 * @param updateInfo
	 * @param start
	 * @param limit
	 * @return
	 */
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
	 * 获取版本号列表
	 * @return
	 */
	public List<String> findVersionList();
	
	/**
	 * 获取所属系统列表
	 * @return
	 */
	public List<UpdateInfo> findBySystemType(Integer subSystem);
}
