package cn.emay.eucp.data.dao.system;

import java.util.List;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Role;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * cn.emay.eucp.common.moudle.db.system.Role Dao super
 * 
 * @author frank
 */
public interface RoleDao extends BaseSuperDao<Role> {

	Page<Role> findRolePage(int start, int limit, String roleName,String roleType);

	Long countNumberByRoleName(String roleName, Long id);

	List<Role> findAllNotDelete();
	
	List<Role> findAll(String roleType);
	
	/**
	 * 根据id和类型批量查询角色
	 * @param ids
	 * @param roleType
	 * @return
	 */
	List<Role> findRoleByIds(List<Long> ids, String roleType);
	
	/**
	 * 根据用户id查询用户角色类型[MANAGE-管理，CLIENT-客户]
	 * @param userId
	 * @return
	 */
	String findRoleTypeByUserId(Long userId);
	/**
	 * 
	 * 查找客户端角色
	 * @return
	 */
	List<Role> findAllname();
	/**
	 * 
	 * 批量查询
	 * @param ids
	 * @return
	 */
	List<Role> findbyIds(List<Long> ids);
	
	/**
	 * 根据id查找角色
	 * @param id
	 * @return
	 */
	Role findRoleById(Long id);
}