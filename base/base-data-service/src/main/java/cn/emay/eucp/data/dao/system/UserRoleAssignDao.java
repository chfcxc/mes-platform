package cn.emay.eucp.data.dao.system;

import java.util.List;

import cn.emay.eucp.common.moudle.db.system.UserRoleAssign;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * cn.emay.eucp.common.moudle.db.system.UserRoleAssign Dao super
 * 
 * @author frank
 */
public interface UserRoleAssignDao extends BaseSuperDao<UserRoleAssign> {

	void deleteByUserId(Long userId);

	List<UserRoleAssign> findByUserId(Long userId);

	Long getNotDeleteUserCountByRole(Long roleId);
	
	/**
	 * 查询用户关联的角色
	 * @param userId
	 * @param roleType
	 * @return
	 */
	List<?> findUserRole(Long userId, String roleType);
	
	List<UserRoleAssign> findclientByUserId(Long userId);

}