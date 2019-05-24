package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeParam;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsServiceCodeParamDao;

@Repository
public class FmsServiceCodeParamDaoImpl extends PojoDaoImpl<FmsServiceCodeParam> implements FmsServiceCodeParamDao {

	@Override
	public List<FmsServiceCodeParam> findByAppid(List<String> list) {
		String sql = " select * from fms_service_code_param s where 1=1";
		if (list != null && list.size() > 0) {
			sql += " and s.app_id in ('" + org.apache.commons.lang3.StringUtils.join(list.toArray(), "','") + "')";
		}
		return this.findSqlForListObj(FmsServiceCodeParam.class, sql, null);
	}

	@Override
	public List<FmsServiceCodeParam> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		String sql = " select * from fms_service_code_param where 1=1 ";
		List<Object> parameters = new ArrayList<Object>();
		if (null != date) {
			sql += " and last_update_time >= ?";
			parameters.add(date);
		}
		return this.findSqlForListForMysql(FmsServiceCodeParam.class, sql, parameters, currentPage, pageSize);
	}

	@Override
	public void deletebyAppid(String appId) {
		String sql = "delete from fms_service_code_param where app_id='" + appId + "'";
		this.execSql(sql);
	}

	@Override
	public FmsServiceCodeParam findbyAppId(String appId) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hql = " from FmsServiceCodeParam where appId =:appId ";
		map.put("appId", appId);
		return this.getUniqueResult(FmsServiceCodeParam.class, hql, map);
	}

}
