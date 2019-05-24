package cn.emay.eucp.data.dao.system.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.alibaba.dubbo.common.utils.StringUtils;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.enterprise.EnterpriseUserDTO;
import cn.emay.eucp.common.moudle.db.system.Enterprise;
import cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.EnterpriseDao;
import cn.emay.eucp.util.RegularUtils;
import cn.emay.util.DateUtil;

/**
 * cn.emay.eucp.common.moudle.db.system.Enterprise Dao implement
 * 
 * @author frank
 */
@Repository
public class EnterpriseDaoImpl extends PojoDaoImpl<Enterprise> implements EnterpriseDao {

	@Override
	public List<Enterprise> findListByName(String enterpriseName) {
		String hql = " from Enterprise where 1=1 ";
		Map<String, Object> params = new HashMap<String, Object>();
		if (!StringUtils.isEmpty(enterpriseName)) {
			hql += " and nameCn like :nameCn";
			params.put("nameCn", "%" + RegularUtils.specialCodeEscape(enterpriseName) + "%");
		}
		return this.getListResult(Enterprise.class, hql, params);
	}

	@Override
	public Page<Enterprise> findPage(int start, int limit, Enterprise entity) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql.append("select e.*,u.realname as salesName,l.auth_name as authName from system_enterprise e left join system_enterprise_binding_sale bs on e.id=bs.system_enterprise_id"
				+ " left join system_name_binding_level l on  l.auth_level=e.authority left join system_user u on bs.system_user_id=u.id where e.is_delete=? ");
				
		params.add(false);
		if (!StringUtils.isEmpty(entity.getNameCn())) {
			hql.append(" and e.name_cn like ?");
			params.add("%" + RegularUtils.specialCodeEscape(entity.getNameCn()) + "%");
		}
		if (!StringUtils.isEmpty(entity.getClientNumber())) {
			hql.append(" and e.client_number= ?");
			params.add(RegularUtils.specialCodeEscape(entity.getClientNumber()));
		}
		if (!StringUtils.isEmpty(entity.getMobile())) {
			hql.append(" and e.mobile= ?");
			params.add(entity.getMobile());
		}
		if (!StringUtils.isEmpty(entity.getLinkman())) {
			hql.append(" and e.linkman like ?");
			params.add("%" + RegularUtils.specialCodeEscape(entity.getLinkman()) + "%");
		}
		if (entity.getViplevel() > -1) {
			hql.append(" and e.viplevel = ?");
			params.add(entity.getViplevel());
		}
		hql.append(" order by e.create_time desc");
		return this.findSqlForPageForMysql(Enterprise.class, hql.toString(), params, start, limit);
	}

	@Override
	public List<Enterprise> findListUseChannel(Long channelId) {
		String hql = "select e from Enterprise e , SmsServiceCode ssc , SmsServiceCodeSmsChannelAssign sa where e.id = ssc.enterpriseId and ssc.id = sa.smsServiceCodeId and sa.smsChannelId = :channelId group by e.nameCn";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("channelId", channelId);
		return this.getListResult(Enterprise.class, hql, params);
	}

	@Override
	public Enterprise findByClientNumber(String clientNumber) {
		return this.findByProperty("clientNumber", clientNumber);
	}

	@Override
	public Map<String, Object> findPage(int start, int limit, String clientName) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select id,nameCn,clientNumber,type,linkman,mobile from Enterprise where isDelete=:isDelete";
		params.put("isDelete", false);
		if (!StringUtils.isEmpty(clientName)) {
			hql += " and (nameCn like :nameCn or clientNumber=:clientNumber)";
			params.put("nameCn", "%" + RegularUtils.specialCodeEscape(clientName) + "%");
			params.put("clientNumber", clientName);
		}
		hql += " order by createTime desc";
		return this.getPageResultMap(hql, start, limit, params);
	}

	@Override
	public Map<String, Object> findPage(int start, int limit, String clientName, String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select id,nameCn,clientNumber,type,linkman,mobile from Enterprise where isDelete=:isDelete";
		params.put("isDelete", false);
		if (null != type && !type.isEmpty()) {
			hql += " and type=:type ";
			params.put("type", type);
		}
		if (!StringUtils.isEmpty(clientName)) {
			hql += " and (nameCn like :nameCn or clientNumber=:clientNumber)";
			params.put("nameCn", "%" + RegularUtils.specialCodeEscape(clientName) + "%");
			params.put("clientNumber", clientName);
		}

		hql += " order by createTime desc";
		return this.getPageResultMap(hql, start, limit, params);
	}

	@Override
	public Enterprise findeByName(String enterpriseName) {
		return this.findByProperty("nameCn", enterpriseName);
	}

	@Override
	public Enterprise findEnterpriseByProperty(String fieldName, Object value) {
		return this.findByProperty(fieldName, value);
	}

	@Override
	public Object findEnterpriseInfo(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select e.id,e.nameCn,e.clientNumber,e.linkman,e.mobile,u.username,e.email,e.isVip,e.telephone,e.address,e.type,e.authority,e.valueAddedService, e.viplevel, e.serviceType,e.startClientSelectTime,e.endClientSelectTime from Enterprise e,Department d,UserDepartmentAssign uda,User u where e.id=:id and e.id=d.enterpriseId and d.id=uda.departmentId and uda.userId=u.id and uda.identity=:identity";
		params.put("id", id);
		params.put("identity", UserDepartmentAssign.IDENTITY_LEADER);
		return this.getUniqueResult(hql, params);
	}

	@Override
	public Enterprise findEnterpriseByName(String enterpriseName) {
		return this.findByProperty("nameCn", enterpriseName);
	}

	@Override
	public Enterprise findEnterpriseByUserId(Long userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " select e from" + "	User u, " + "	UserDepartmentAssign uda, " + "	Department d, " + "	Enterprise e " + " where  u.id = uda.userId " + " and uda.departmentId =d.id "
				+ " and d.enterpriseId = e.id " + " and u.id = :userId";
		params.put("userId", userId);
		return this.getUniqueResult(Enterprise.class, hql, params);
	}

	@Override
	public Enterprise findEmail(String email) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Enterprise where email=:email ";
		params.put("email", email);
		return this.getUniqueResult(Enterprise.class, hql, params);
	}

	@Override
	public Enterprise findEnterpriseByServiceCode(String serviceCode) {
		String sql = "SELECT a.*\n" + "  FROM system_enterprise a, sms_service_code b\n" + " WHERE a. id = b. enterprise_id\n" + "   AND b. service_code = ?";
		return findSqlForObj(Enterprise.class, sql, Arrays.asList(new Object[] { serviceCode }));
	}

	@Override
	public List<Enterprise> getEnterprises(Boolean isDelete) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Enterprise where 1=1 ";
		if (null != isDelete) {
			hql += " and isDelete = :isDelete";
			params.put("isDelete", isDelete);
		}
		return this.getListResult(Enterprise.class, hql, params);
	}

	@Override
	public List<Enterprise> getEnterpriseByNameOrClientNumber(String NameOrClientNumber) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Enterprise where 1=1 ";
		if (null != NameOrClientNumber && !"".equals(NameOrClientNumber)) {
			hql += " and (nameCn like :nameCn or clientNumber = :NameOrClientNumber)";
			params.put("nameCn", "%" + RegularUtils.specialCodeEscape(NameOrClientNumber) + "%");
			params.put("NameOrClientNumber", NameOrClientNumber);
		}
		return this.getListResult(Enterprise.class, hql, params);
	}

	@Override
	public List<Map<String, Object>> getEnterpriseByIds(String ids) {
		String sql = "select * from system_enterprise where 1=1 ";
		if (null != ids && !"".equals(ids)) {
			sql += " and id in (" + ids + ")";
		}
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<Enterprise> findByLastUpdateTime(Date lastUpdateTime, int currentPage, int pageSize) {
		String sql = "select * from system_enterprise where 1=1 ";
		List<Object> parameters = new ArrayList<Object>();
		if (null != lastUpdateTime) {
			sql += "and last_update_time>= ? ";
			parameters.add(DateUtil.toString(lastUpdateTime, "yyyy-MM-dd HH:mm:ss"));
		}
		return this.findSqlForListForMysql(Enterprise.class, sql, parameters, currentPage, pageSize);
	}

	@Override
	public void batchUpdateBalance(List<Object[]> list) {
		String sql = "update system_enterprise set balance=balance+? where id=?";
		this.getJdbcTemplate().batchUpdate(sql, list);
	}

	@Override
	public List<Enterprise> getEnterpriseByNameAndClientNumber(String Name, String clientNumber) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Enterprise where 1=1 ";
		if (null != Name && !"".equals(Name)) {
			hql += " and nameCn like :Name";
			params.put("Name", "%" + RegularUtils.specialCodeEscape(Name) + "%");
		}
		if (null != clientNumber && !"".equals(clientNumber)) {
			hql += " and clientNumber like :clientNumber";
			params.put("clientNumber", "%" + RegularUtils.specialCodeEscape(clientNumber) + "%");
		}
		return this.getListResult(Enterprise.class, hql, params);
	}

	@Override
	public List<Enterprise> getEnterprisesByType(String nameCn, String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Enterprise where 1=1 ";
		if (null != type) {
			hql += " and type = :type";
			params.put("type", type);
		}
		if (null != nameCn && !"".equals(nameCn)) {
			hql += " and nameCn like :nameCn";
			params.put("nameCn", "%" + RegularUtils.specialCodeEscape(nameCn) + "%");
		}
		return this.getListResult(Enterprise.class, hql, params);
	}

	public List<Enterprise> findByids(Set<Long> idset) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Enterprise where 1=1 ";
		if (null != idset && idset.size() != 0) {
			String ids = "";
			for (Long id : idset) {
				ids += id.longValue() + ",";
			}
			if (!ids.isEmpty()) {
				ids = ids.substring(0, ids.length() - 1);
				hql += " and id in (" + RegularUtils.specialCodeEscape(ids) + ") ";
			}
		}
		return this.getListResult(Enterprise.class, hql, params);
	}

	@Override
	public List<Enterprise> findListByNameAndClientNumber(String enterpriseName, String clientNumber) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from Enterprise where 1=1 ";
		if (null != enterpriseName && !"".equals(enterpriseName)) {
			hql += " and nameCn like :enterpriseName";
			params.put("enterpriseName", "%" + RegularUtils.specialCodeEscape(enterpriseName) + "%");
		}
		if (null != clientNumber && !"".equals(clientNumber)) {
			hql += " and clientNumber = :clientNumber";
			params.put("clientNumber", clientNumber);
		}
		return this.getListResult(Enterprise.class, hql, params);
	}
	
	@Override
	public List<EnterpriseUserDTO> findListByNameAndRealName(String enterpriseName, String saleManager, Set<Long> enterpriseIdSet) {
		List<Object> parameters = new ArrayList<Object>();
		StringBuffer baseSql = new StringBuffer(
				"select se.id,se.name_cn,su.realname from system_enterprise  se,system_enterprise_binding_sale sebs,system_user su where se.id=sebs.system_enterprise_id and sebs.system_user_id=su.id ");
		if (!StringUtils.isEmpty(enterpriseName)) {
			baseSql.append("and se.name_cn like ?");
			parameters.add("%" + RegularUtils.specialCodeEscape(enterpriseName) + "%");
		}
		if (!StringUtils.isEmpty(saleManager)) {
			baseSql.append("and su.realname like ?");
			parameters.add("%" + RegularUtils.specialCodeEscape(saleManager) + "%");
		}
		if (enterpriseIdSet != null && enterpriseIdSet.size() > 0) {
			baseSql.append("and se.id in (" + org.apache.commons.lang3.StringUtils.join(enterpriseIdSet.toArray(), ",") + ") ");
		}
		return this.findSqlForListObj(EnterpriseUserDTO.class, baseSql.toString(), parameters);
	}

	@Override
	public Map<String, Object> findPageByServiceType(int start, int limit, String clientName, String serviceType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select id,nameCn,clientNumber,type,linkman,mobile from Enterprise where isDelete=:isDelete";
		params.put("isDelete", false);
		if (!StringUtils.isEmpty(clientName)) {
			hql += " and (nameCn like :nameCn or clientNumber=:clientNumber)";
			params.put("nameCn", "%" + RegularUtils.specialCodeEscape(clientName) + "%");
			params.put("clientNumber", clientName);
		}
		if (!StringUtils.isEmpty(serviceType)) {
			hql += " and serviceType like :serviceType";
			params.put("serviceType", "%" + serviceType + "%");
		}
		hql += " order by createTime desc";
		return this.getPageResultMap(hql, start, limit, params);
	}

}
