package cn.emay.eucp.data.dao.system.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.UpdateInfo;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.UpdateInfoDao;
import cn.emay.eucp.util.RegularUtils;

/**
 * 
 * @author gh
 */
@Repository
public class UpdateInfoDaoImpl extends PojoDaoImpl<UpdateInfo> implements UpdateInfoDao {

	@Override
	public List<UpdateInfo> findAllList() {
		String hql = "from UpdateInfo where 1=1 order  by  id  desc";
		return this.getListResult(UpdateInfo.class, hql);
	}

	@Override
	public List<UpdateInfo> selectInfoByCondition(String version, String updateInfo) {
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		String hql = "from UpdateInfo  where 1=1";
		
		if(!StringUtils.isEmpty(version)) {
			hql = hql + " and versionInfo=:version";
			params.put("version", version);
		}
		
		if(!StringUtils.isEmpty(updateInfo)) {
			hql = hql + " and updateInfo like :updateInfo";
			params.put("updateInfo", "%"+updateInfo+"%");
		}
		hql = hql + "  order  by  createTime  desc";
		return this.getListResult(UpdateInfo.class, hql, params);
		  
	}

	@Override
	public int[] saveBatchUpdateInfo(List<Object[]> params) {
		String sql = "insert system_update_info(version_info,pub_time,sub_system,busniess_type,update_info,create_time) values (?,?,?,?,?,now())";
		return this.jdbcTemplate.batchUpdate(sql, params);
	}

	@Override
	public Page<UpdateInfo> selectUpdateInfo(String versionInfo, String updateInfo, Integer start, Integer limit) {
		String hql = "from UpdateInfo where 1=1 ";
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		if (updateInfo != null) {
			hql += " and updateInfo like :updateInfo ";
			params.put("updateInfo", "%" + RegularUtils.specialCodeEscape(updateInfo) + "%");
		}
		if (versionInfo != null && versionInfo.trim().length() != 0) {
			hql += " and versionInfo = :versionInfo ";
			params.put("versionInfo", RegularUtils.specialCodeEscape(versionInfo));
		}
		hql += "order by id desc";
		return this.getPageResult(hql, start, limit, params, UpdateInfo.class);
	}
	
	public List<String> findVersionList() {
		String hql = "select versionInfo from UpdateInfo ";
		return this.getListResult(String.class, hql);
	}
	
	public List<UpdateInfo> findBySystemType(Integer subSystem) {
		String hql = "from UpdateInfo  where subSystem=:subSystem ";
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("subSystem", subSystem);
		hql += "order by id desc";
		List<UpdateInfo> list = this.getListResult(UpdateInfo.class, hql, params);
		return  list;
	}
	
}
