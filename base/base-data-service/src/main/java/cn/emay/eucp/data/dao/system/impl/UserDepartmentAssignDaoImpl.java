package cn.emay.eucp.data.dao.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.stereotype.Repository;

import cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.UserDepartmentAssignDao;

/**
 * cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign Dao implement
 * 
 * @author frank
 */
@Repository
public class UserDepartmentAssignDaoImpl extends PojoDaoImpl<UserDepartmentAssign> implements UserDepartmentAssignDao {

	@Override
	public void deleteDataByUserId(Long userId) {
		this.deleteByProperty("userId", userId);
	}

	@Override
	public UserDepartmentAssign findByUserId(Long userId) {
		return this.findByProperty("userId", userId);
	}

	@Override
	public Long findByDepId(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select count(*) from UserDepartmentAssign where departmentId=:id";
		params.put("id", id);
		return (Long) super.getUniqueResult(hql, params);
	}

	@Override
	public Long findByDepartmentId(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "select userId from UserDepartmentAssign where departmentId=:id and identity=:identity";
		params.put("id", id);
		params.put("identity", UserDepartmentAssign.IDENTITY_LEADER);
		return (Long) super.getUniqueResult(hql, params);
	}

	@Override
	public Long findUsernumber(Long enterpriseId) {
		String sql=" SELECT	count(ud.system_user_id) FROM system_user u INNER JOIN system_user_department_assign ud on u.id=ud.system_user_id INNER JOIN system_enterprise_department ed ON ud.enterprise_department_id = ed.id WHERE ud.identity=2 and u.state !=0 and ed.enterprise_id = ? ";
		List<Object> list = new ArrayList<Object>();
		list.add(enterpriseId);	
		return Long.parseLong(this.getUniqueResultBySql(sql, list.toArray()).toString());
	}

	@Override
	public List<Long> findByDepartmentIds(List<Long> list,int type) {
		String sql="select system_user_id from system_user_department_assign where enterprise_department_id in ("+StringUtils.join(list.toArray(), ",")+")";
		if(type>0){
			sql+=" and identity="+type;
		}
		SingleColumnRowMapper<Long> rowMapper = new SingleColumnRowMapper<Long>();
		return this.getJdbcTemplate().query(sql,rowMapper);
	}

	@Override
	public List<Long> findAllUserByDeptId(Long deptId){
		List<Object> params = new ArrayList<Object>();
		String sql = "select uda.system_user_id from system_enterprise_department d,system_user_department_assign uda where d.id = uda.enterprise_department_id and (d.full_path like ? or d.id = ?) and d.is_delete=0";
		params.add("%,"+ deptId +",%");
		params.add(deptId);
		return this.findSqlForSingleColumn(sql, params);
	}

}