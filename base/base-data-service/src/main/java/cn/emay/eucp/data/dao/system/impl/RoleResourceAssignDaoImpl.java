package cn.emay.eucp.data.dao.system.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.emay.eucp.common.moudle.db.system.RoleResourceAssign;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.RoleResourceAssignDao;

/**
 * cn.emay.eucp.common.moudle.db.system.RoleResourceAssign Dao implement
 * 
 * @author frank
 */
@Repository
public class RoleResourceAssignDaoImpl extends PojoDaoImpl<RoleResourceAssign> implements RoleResourceAssignDao {

	@Override
	public void deleteBatch(Long roleId) {
		this.deleteByProperty("roleId", roleId);
	}

	@Override
	public List<RoleResourceAssign> findAllAuthByRoleId(Long roleId) {
		return this.findListByProperty("roleId", roleId);
	}
}