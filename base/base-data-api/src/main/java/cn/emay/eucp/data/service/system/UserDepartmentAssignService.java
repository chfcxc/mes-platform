package cn.emay.eucp.data.service.system;

import java.util.List;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign;

/**
 * cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign Service Super
 * 
 * @author frank
 */
public interface UserDepartmentAssignService {

	/**
	 * 添加数据
	 */
	void addDepartment(UserDepartmentAssign userDepartmentAssign);

	/**
	 * 删除用户部门
	 * 
	 * @param userId
	 */
	void deleteDataByUserId(Long userId);

	/**
	 * 批量删除部门用户
	 * 
	 * @param userIds
	 * @param departmentId
	 * @return
	 */
	Result deleteBatch(String userIds);

	/**
	 * 批量增加部门用户
	 * 
	 * @param departmentId
	 * @param userIds
	 * @return
	 */
	Result addBatch(Long departmentId, String userIds);

	/**
	 * 查询用户所属的部门
	 * 
	 * @param userId
	 * @return
	 */
	UserDepartmentAssign findByUserId(Long userId);

	/**
	 * 查询部门下是否还有用户
	 * 
	 * @param list
	 * @return
	 */
	Long findByDepId(Long id);
	
	void add(UserDepartmentAssign userDepartmentAssign);
	Long findUsernumber(Long userId);
	
	/**
	 * 根据部门id查出所有部门下用户
	 * @param list
	 * @return
	 */
	List<Long> findByDepartmentIds(List<Long> list,int type);

	List<Long> findAllUserByDeptId(Long deptId);

}