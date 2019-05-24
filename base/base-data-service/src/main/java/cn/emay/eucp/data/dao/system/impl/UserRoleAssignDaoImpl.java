package cn.emay.eucp.data.dao.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.moudle.db.system.UserRoleAssign;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.UserRoleAssignDao;

/**
 * cn.emay.eucp.common.moudle.db.system.UserRoleAssign Dao implement
 * 
 * @author frank
 */
@Repository
public class UserRoleAssignDaoImpl extends PojoDaoImpl<UserRoleAssign> implements UserRoleAssignDao {

	@Override
	public void deleteByUserId(Long userId) {
		this.deleteByProperty("userId", userId);
	}

	@Override
	public List<UserRoleAssign> findByUserId(Long userId) {
		return this.findListByProperty("userId", userId);
	}

	@Override
	public Long getNotDeleteUserCountByRole(Long roleId) {
		String hql = "select count(r) from UserRoleAssign r,User u where u.id = r.userId and u.state != :deletestate and r.roleId = :roleId ";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("deletestate", User.STATE_DELETE);
		param.put("roleId", roleId);
		return (Long) this.getUniqueResult(hql, param);
	}
	
	@Override
	public List<?> findUserRole(Long userId,String roleType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql="select ura.roleId,r.roleName,ura.userId from UserRoleAssign ura,Role r where ura.roleId=r.id and ura.userId=:userId and r.roleType=:roleType and r.isDelete=:isDelete";
		params.put("userId", userId);
		params.put("roleType", roleType);
		params.put("isDelete", false);
		return this.getListResult(hql, params);
	}

	@Override
	public List<UserRoleAssign> findclientByUserId(Long userId) {
		return this.findListByProperty("userId", userId);
	}

}