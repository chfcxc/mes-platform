package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeInfoDTO;
import cn.emay.eucp.common.dto.fms.serviceCode.GeneratorSerciceCodeDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsServiceCodeDao;

@Repository
public class FmsServiceCodeDaoImpl extends PojoDaoImpl<FmsServiceCode> implements FmsServiceCodeDao {

	@Override
	public List<FmsServiceCode> findByEnterId(Long enterpriseId, int state) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from FmsServiceCode where enterpriseId = :enterpriseId";
		params.put("enterpriseId", enterpriseId);
		if (state > -1) {
			hql += " and state = :state";
			params.put("state", state);
		}
		return this.getListResult(FmsServiceCode.class, hql, params);
	}

	@Override
	public List<FmsServiceCode> findByIds(Long enterpriseId, List<Long> ids) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from FmsServiceCode where 1=1";
		if (enterpriseId != null) {
			hql += " and enterpriseId=:enterpriseId";
			params.put("enterpriseId", enterpriseId);
		}
		if (ids != null && ids.size() > 0) {
			hql += " and id in (:ids)";
			params.put("ids", ids);
		}
		return this.getListResult(FmsServiceCode.class, hql, params);
	}

	@Override
	public Page<FmsServiceCode> findlist(int start, int limit, String serviceCode, String appId, Long businessTypeId, Long enterpriseId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from FmsServiceCode where 1=1 ";
		if (enterpriseId != null) {
			hql += " and enterpriseId=:enterpriseId";
			params.put("enterpriseId", enterpriseId);
		}
		if (!StringUtils.isEmpty(serviceCode)) {
			hql += " and serviceCode=:serviceCode";
			params.put("serviceCode", serviceCode);
		}
		if (!StringUtils.isEmpty(appId)) {
			hql += " and appId=:appId";
			params.put("appId", appId);
		}
		if (businessTypeId.longValue() != 0L) {
			hql += " and businessTypeId =:businessTypeId";
			params.put("businessTypeId", businessTypeId);
		}
		hql += " order by id desc ";
		return this.getPageResult(hql, start, limit, params, FmsServiceCode.class);
	}

	@Override
	public Page<FmsServiceCode> findPage(int start, int limit, String serviceCode, String appId, Long businessTypeId, Set<Long> enterpriseId, int state, Long serviceCodeId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from FmsServiceCode where 1=1 ";
		if (enterpriseId != null) {
			hql += " and enterpriseId in(:enterpriseId)";
			params.put("enterpriseId", enterpriseId);
		}
		if (!StringUtils.isEmpty(serviceCode)) {
			hql += " and serviceCode=:serviceCode";
			params.put("serviceCode", serviceCode);
		}
		if (serviceCodeId != 0L) {
			hql += " and id=:serviceCodeId";
			params.put("serviceCodeId", serviceCodeId);
		}
		if (!StringUtils.isEmpty(appId)) {
			hql += " and appId=:appId";
			params.put("appId", appId);
		}
		if (businessTypeId.longValue() != 0L) {
			hql += " and businessTypeId =:businessTypeId";
			params.put("businessTypeId", businessTypeId);
		}
		if (state != -1) {
			hql += " and state=:state";
			params.put("state", state);
		}
		hql += " order by id desc ";
		return this.getPageResult(hql, start, limit, params, FmsServiceCode.class);
	}

	@Override
	public Set<String> findExitAppid(String appId) {
		String sql = "select a.`app_id` from `fms_service_code` a where 1=1";
		List<String> sqlList = new ArrayList<String>();
		Map<String, String> appIdMap = new HashMap<String, String>();
		List<Object> params = new ArrayList<Object>();
		String appIdSuffix = appId.substring(appId.lastIndexOf("-") + 1);
		sqlList.add("a.app_id like ?");
		params.add("%" + appIdSuffix);
		appIdMap.put(appIdSuffix, appId);// appIdMap确保生成的appId后5位唯一
		String sqlJoin = org.apache.commons.lang3.StringUtils.join(sqlList, " or ");
		sql += " and (" + sqlJoin + ")";
		List<String> queryList = this.getJdbcTemplate().queryForList(sql, params.toArray(), String.class);
		Set<String> result = new HashSet<String>();
		result.addAll(appIdMap.values());
		result.removeAll(queryList);
		return result;
	}

	@Override
	public void saveBatchServiceCode(GeneratorSerciceCodeDTO serviceSourceCode) {
		List<Object[]> list1 = new ArrayList<Object[]>();
		Date date = new Date();
		list1.add(new Object[] { serviceSourceCode.getServiceCode(), serviceSourceCode.getSn(), serviceSourceCode.getSecretKey(), serviceSourceCode.getEnterpriseId(), FmsServiceCode.STATE_DISABLE,
				date, serviceSourceCode.getBusinessTypeId(), serviceSourceCode.getRemark() });
		String sql = "";
		if (list1.size() > 0) {
			sql = "insert into fms_service_code(service_code,app_id,secret_key,enterprise_id,state,create_time,business_type_id,remark) values(?,?,?,?,?,?,?,?)";
			this.getJdbcTemplate().batchUpdate(sql, list1);
		}
	}

	@Override
	public FmsServiceCodeInfoDTO findbyid(Long id) {
		String sql = " select s.*,p.get_report_type,p.ip_configuration from  fms_service_code s LEFT JOIN fms_service_code_param p on s.app_id=p.app_id where s.id='" + id + "'";
		return this.findSqlForObj(FmsServiceCodeInfoDTO.class, sql, null);
	}

	@Override
	public List<FmsServiceCode> findByUserId(Long userId) {
		String sql = "select fsc.* from fms_service_code fsc,fms_user_service_code_assign fusca where fsc.id=fusca.service_code_id ";
		List<Object> object = new ArrayList<>();
		if (userId != null) {
			sql += " and fusca.user_id=? ";
			object.add(userId);
		}
		return this.findSqlForListObj(FmsServiceCode.class, sql, object);
	}

	@Override
	public List<FmsServiceCode> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		String sql = " select * from fms_service_code where 1=1 ";
		List<Object> params = new ArrayList<Object>();
		if (null != date) {
			sql += " and last_update_time >= ? ";
			params.add(date);
		}
		return this.findSqlForListForMysql(FmsServiceCode.class, sql, params, currentPage, pageSize);
	}

	@Override
	public FmsServiceCode findByserviceCode(String appId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from FmsServiceCode where appId = :appId";
		params.put("appId", appId);
		return this.getUniqueResult(FmsServiceCode.class, hql, params);
	}

	@Override
	public List<FmsServiceCode> findByIds(Set<Long> ids) {
		String sql = " select * from fms_service_code where id in (" + StringUtils.join(ids.toArray(), ",") + ") ";
		return this.findSqlForListObj(FmsServiceCode.class, sql, null);
	}

}
