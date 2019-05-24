package cn.emay.eucp.data.dao.system;

import java.util.List;

import cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign Dao super
 * 
 * @author frank
 */
public interface UserDepartmentAssignDao extends BaseSuperDao<UserDepartmentAssign> {

	void deleteDataByUserId(Long userId);

	UserDepartmentAssign findByUserId(Long userId);

	Long findByDepId(Long id);

	/**
	 * 根据部门id查找主账号用户id
	 * @param id
	 * @return
	 */
	Long findByDepartmentId(Long id);
	Long findUsernumber(Long enterpriseId);
	
	/**
	 * 根据部门id查出所有部门下用户
	 * @param list
	 * @return
	 */
	List<Long> findByDepartmentIds(List<Long> list,int type);
	
	List<Long> findAllUserByDeptId(Long deptId);
}