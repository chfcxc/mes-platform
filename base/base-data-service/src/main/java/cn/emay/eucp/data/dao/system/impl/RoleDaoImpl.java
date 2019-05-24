package cn.emay.eucp.data.dao.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Role;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.RoleDao;
import cn.emay.eucp.util.RegularUtils;

/**
 * cn.emay.eucp.common.moudle.db.system.Role Dao implement
 * 
 * @author frank
 */
@Repository
public class RoleDaoImpl extends PojoDaoImpl<Role> implements RoleDao {

	@Override
	public List<Role> findAllNotDelete() {
		Map<String, Object> parms = new HashMap<String, Object>();
		String hql = "from Role where isDelete=:isDelete";
		parms.put("isDelete", false);
		return this.getListResult(Role.class, hql, parms);
	}
	
	@Override
	public List<Role> findAll(String roleType) {
		Map<String, Object> parms = new HashMap<String, Object>();
		String hql = "from Role where isDelete=:isDelete and roleType=:roleType";
		parms.put("isDelete", false);
		parms.put("roleType", roleType);
		return this.getListResult(Role.class, hql, parms);
	}

	@Override
	public Page<Role> findRolePage(int start, int limit, String roleName, String roleType) {
		Map<String, Object> parms = new HashMap<String, Object>();
		String hql = "from Role where isDelete=:isDelete and roleType=:roleType";
		parms.put("isDelete", false);
		parms.put("roleType", roleType);
		if (!StringUtils.isEmpty(roleName)) {
			hql += " and roleName like :roleName";
			parms.put("roleName", "%" + RegularUtils.specialCodeEscape(roleName) + "%");
		}
		hql += " order by id desc";
		return this.getPageResult(hql, start, limit, parms, Role.class);
	}

	@Override
	public Long countNumberByRoleName(String roleName, Long id) {
		Map<String, Object> parms = new HashMap<String, Object>();
		String hql = "select count(*) from Role where roleName=:roleName and isDelete!=:isDelete";
		parms.put("roleName", roleName);
		parms.put("isDelete", true);
		if (id > 0) {
			hql += " and id <> :id";
			parms.put("id", id);
		}
		return (Long) super.getUniqueResult(hql, parms);
	}

	@Override
	public List<Role> findRoleByIds(List<Long> ids, String roleType) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "from Role where id in (:ids) and isDelete=:isDelete";
		if (!StringUtils.isEmpty(roleType)) {
			hql += " and roleType=:roleType";
			params.put("roleType", roleType);
		}
		params.put("ids", ids);
		params.put("isDelete", false);
		return this.getListResult(Role.class, hql, params);
	}
	
	@Override
	public String findRoleTypeByUserId(Long userId){
		Map<String, Object> params = new HashMap<String, Object>();
		String hql="select r.roleType from Role r,UserRoleAssign ura where r.id=ura.roleId and ura.userId=:userId and r.isDelete=:isDelete";
		params.put("userId", userId);
		params.put("isDelete", false);
		return (String) this.getUniqueResult(hql, params);
	}

	@Override
	public List<Role> findAllname() {	
		String hql=" select r FROM Role r where roleType ='CLIENT' and isDelete='0' ";	 
		return this.getListResult(Role.class, hql);
	}

	@Override
	public List<Role> findbyIds(List<Long> ids) {
		String hql=" from Role where id in (:ids) and isDelete=0 ";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("ids", ids);	
		return this.getListResult(Role.class, hql, param);
	}

	@Override
	public Role findRoleById(Long id) {
		String hql=" from Role where id=:id and isDelete=0 ";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("id", id);	
		return this.getUniqueResult(Role.class, hql, param);
	}
}