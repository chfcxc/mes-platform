package cn.emay.eucp.data.dao.system.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Export;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.ExportDao;

@Repository
public class ExportDaoImpl extends PojoDaoImpl<Export> implements ExportDao {

	@Override
	public Export findSmsExport(String systemFor, String businessType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from Export where state=:state ";
		if (null != businessType) {
			hql += " and businessType = :businessType ";
			params.put("businessType", businessType);
		}
		if (null != systemFor) {
			hql += " and systemFor = :systemFor ";
			params.put("systemFor", systemFor);
		}
		hql += " order by createTime asc limit 1 ";
		params.put("state", Export.STATE_WAITING_GENERATION);
		return this.getUniqueResult(Export.class, hql, params);
	}

	@Override
	public Page<Export> findPage(int start, int limit, Long userId, String systemFor, String businessType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from Export s where s.userId=:userId ";
		params.put("userId", userId);
		if (null != businessType) {
			hql += " and businessType = :businessType ";
			params.put("businessType", businessType);
		}
		if (null != systemFor) {
			hql += " and systemFor = :systemFor ";
			params.put("systemFor", systemFor);
		}
		hql += " order by s.createTime desc ";
		return this.getPageResult(hql, start, limit, params, Export.class);
	}

	@Override
	public List<Export> findSmsExportBeforeDate(Date date, String systemFor, String businessType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from Export s where s.createTime<:date and (s.state=:state or s.state=:exceptState) ";
		if (null != businessType) {
			hql += " and businessType = :businessType ";
			params.put("businessType", businessType);
		}
		if (null != systemFor) {
			hql += " and systemFor = :systemFor ";
			params.put("systemFor", systemFor);
		}
		params.put("date", date);
		params.put("state", Export.STATE_GENERATION_COMPLETED);
		params.put("exceptState", Export.STATE_GENERATION_EXCEPTION);
		return this.getListResult(Export.class, hql, params);
	}

	@Override
	public Integer updateBatchSmsExport(Date date, int state, String systemFor, String businessType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "update Export set state=:newState,finishTime=:finishTime where createTime<:date and state=:state ";
		params.put("newState", Export.STATE_GENERATION_EXCEPTION);
		params.put("finishTime", new Date());
		params.put("date", date);
		params.put("state", state);
		if (null != businessType) {
			hql += " and businessType = :businessType ";
			params.put("businessType", businessType);
		}
		if (null != systemFor) {
			hql += " and systemFor = :systemFor ";
			params.put("systemFor", systemFor);
		}
		return this.execByHql(hql, params);
	}
}