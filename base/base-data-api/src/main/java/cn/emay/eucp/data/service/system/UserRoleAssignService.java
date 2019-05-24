package cn.emay.eucp.data.service.system;

import java.util.List;
import java.util.Map;

import cn.emay.eucp.common.moudle.db.system.UserRoleAssign;

/**
 * cn.emay.eucp.common.moudle.db.system.UserRoleAssign Service Super
 * 
 * @author frank
 */
public interface UserRoleAssignService {

	/**
	 * 批量插入
	 * 
	 * @param urs
	 */
	void saveBatch(List<UserRoleAssign> urs);

	/**
	 * 删除用户的所有角色
	 * 
	 * @param userId
	 */
	void deleteByUserId(Long userId);

	/**
	 * 查询用户的所有角色
	 * 
	 * @param userId
	 * @return
	 */
	List<UserRoleAssign> findByUserId(Long userId);

	/**
	 * 查询角色下是否有用户
	 * 
	 * @param roleId
	 * @return
	 */
	Long getNotDeleteUserCountByRole(Long roleId);
	
	/**
	 * 查询用户角色
	 * @param userId
	 * @param roleType
	 * @return
	 */
	List<Map<String,Object>> findUserRole(Long userId, String roleType);
	/**
	 * 
	 * 
	 */
	void add(UserRoleAssign userRoleAssign);
	
	void update(UserRoleAssign userRoleAssign);
	List<UserRoleAssign> findclientByUserId(Long userId);

}