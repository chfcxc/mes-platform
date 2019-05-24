package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.emay.eucp.common.dto.fms.serviceCode.GeneratorSerciceCodeDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.common.moudle.db.fms.FmsUserServiceCodeAssign;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsUserServiceCodeAssignDao;

@Repository
public class FmsUserServiceCodeAssignDaoImpl extends PojoDaoImpl<FmsUserServiceCodeAssign> implements FmsUserServiceCodeAssignDao {

	@Override
	public List<FmsServiceCode> findByAssignUserId(Long userId) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select sc.id,sc.service_code,sc.app_id,sc.state,sc.secret_key from fms_service_code sc,fms_user_service_code_assign usa where sc.id=usa.service_code_id and usa.user_id=?";
		params.add(userId);
		return this.findSqlForListObj(FmsServiceCode.class, sql, params);
	}

	@Override
	public void deleteUserSCAssignByProperty(String string, Long userId) {
		this.deleteByProperty(string, userId);

	}

	@Override
	public void saveBatchUserSCAssign(List<Long> serviceCodeIdList, Long userId) {
		List<Object[]> userSCAssignList = new ArrayList<Object[]>();
		for (Long scId : serviceCodeIdList) {
			userSCAssignList.add(new Object[] { userId, scId });
		}
		String sql = "insert into fms_user_service_code_assign(user_id,service_code_id,last_update_time) values(?,?,NOW())";
		this.getJdbcTemplate().batchUpdate(sql, userSCAssignList);

	}

	@Override
	public List<FmsUserServiceCodeAssign> findUserSCAssignByProperty(String string, Long userId) {
		return this.findListByProperty(string, userId);
	}

	@Override
	public FmsUserServiceCodeAssign getMmsUserSCAssign(Long userId, Long serviceCodeId) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from FmsUserServiceCodeAssign where userId=:userId and serviceCodeId=:serviceCodeId";
		params.put("userId", userId);
		params.put("serviceCodeId", serviceCodeId);
		return this.getUniqueResult(FmsUserServiceCodeAssign.class, hql, params);
	}

	@Override
	public List<FmsServiceCode> findByAssignUserId(Long userId, int start, int end) {
		List<Object> params = new ArrayList<Object>();
		String sql = "select sc.id,sc.service_code,sc.app_id,sc.state,sc.secret_key from fms_service_code sc,fms_user_service_code_assign usa where sc.id=usa.service_code_id and usa.user_id=?";
		params.add(userId);
		return this.findSqlForListForMysql(FmsServiceCode.class, sql, params, start, end);
	}

	@Override
	public void saveBatchBind(List<GeneratorSerciceCodeDTO> listServiceSourceCodes, Long userId) {
		List<Object[]> paramsList = new ArrayList<Object[]>();
		String sql = "INSERT INTO fms_user_service_code_assign (user_id,service_code_id) SELECT ?,id as service_code_id FROM fms_service_code where app_id=?";
		for (GeneratorSerciceCodeDTO dto : listServiceSourceCodes) {
			paramsList.add(new Object[] { userId, dto.getSn() });
		}
		this.jdbcTemplate.batchUpdate(sql, paramsList);
	}

}
