package cn.emay.eucp.data.service.system;

import java.util.List;

import cn.emay.eucp.common.moudle.db.system.RoleResourceAssign;

/**
 * cn.emay.eucp.common.moudle.db.system.RoleResourceAssign Service Super
 * 
 * @author frank
 */
public interface RoleResourceAssignService {

	/**
	 * 添加角色权限
	 * 
	 * @param list
	 * @return
	 */
	void saveRoleResourceAssign(RoleResourceAssign roleResourceAssign);

	/**
	 * 批量添加角色权限
	 * 
	 * @param roleId
	 * @param auths
	 */
	void saveBatch(Long roleId, String auths);

	/**
	 * 批量删除角色权限
	 * 
	 * @param auths
	 * @return
	 */
	void deleteBatch(Long roleId);

	/**
	 * 用户所有权限
	 * 
	 * @param RoleId
	 * @return
	 */
	List<RoleResourceAssign> findAllAuthByRoleId(Long roleId);

}