package cn.emay.eucp.data.dao.system;

import java.util.List;

import cn.emay.eucp.common.moudle.db.system.RoleResourceAssign;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * cn.emay.eucp.common.moudle.db.system.RoleResourceAssign Dao super
 * 
 * @author frank
 */
public interface RoleResourceAssignDao extends BaseSuperDao<RoleResourceAssign> {

	void deleteBatch(Long roleId);

	List<RoleResourceAssign> findAllAuthByRoleId(Long roleId);
	
	
}