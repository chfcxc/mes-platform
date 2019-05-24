package cn.emay.eucp.data.service.system;

import java.util.List;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Role;

/**
 * cn.emay.eucp.common.moudle.db.system.Role Service Super
 * 
 * @author frank
 */
public interface RoleService {

	/**
	 * 查询表中所有数据
	 * 
	 * @return
	 */
	List<Role> findAll(String roleType);

	/**
	 * 分页查询所有系统角色
	 * 
	 * @param start
	 * @param limit
	 * @param roleName
	 * @return
	 */
	Page<Role> findRolePage(int start, int limit, String roleName,String roleType);

	/**
	 * 按角色id删除角色
	 * 
	 * @param roleId
	 * @return
	 */
	Result deleteRoleById(Long roleId);

	/**
	 * 查询用户名是否重复
	 * 
	 * @param roleName
	 * @param id
	 * @return
	 */
	Long countNumberByRoleName(String roleName, Long id);

	/**
	 * 添加角色
	 * 
	 * @param roleName
	 * @param auths
	 * @return
	 */
	Result addRole(String roleName, String auths, String remark,String roleType);

	/**
	 * 修改角色
	 * 
	 * @param roleId
	 * @param roleName
	 * @param auths
	 * @param remark
	 * @return
	 */
	Result modifyRole(Long roleId, String roleName, String auths, String remark);

	/**
	 * 角色id
	 * 
	 * @param roleId
	 * @return
	 */
	Role findById(Long roleId);
	/**
	 * 
	 * 查找客户端角色
	 * @return
	 */
	List<Role> findAllname();
	
	List<Role> findByIds(String ids);
	
	/**
	 * 根据id查找角色
	 * @param id
	 * @return
	 */
	Role findRoleById(Long id);

	List<Role> findRoleByIds(List<Long> roleIdList, String code);
}