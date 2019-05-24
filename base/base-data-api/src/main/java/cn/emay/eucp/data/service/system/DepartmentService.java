package cn.emay.eucp.data.service.system;

import java.util.List;
import java.util.Map;

import cn.emay.common.Result;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.department.DepartmentDTO;
import cn.emay.eucp.common.moudle.db.system.Department;

/**
 * 企业部门服务
 *
 * @author lijunjian
 *
 */
public interface DepartmentService {

	/**
	 * 根据部门ID查找部门
	 *
	 * @param departmentId
	 * @return
	 */
	Department findDepartmentById(Long departmentId);
	/**
	 * 根据父类id查找部门
	 *
	 * @param parentId
	 *            父类id
	 * @return
	 */
	List<Department> findByParentId(Long parentId,Long enterpriseId);
	List<Map<String, Object>> getDepartTree(Long parentId,Long enterpriseId);
	/**
	 * 添加部门
	 *
	 * @param department
	 * @return
	 */
	Result addDepartment(Department department);

	/**
	 * 根据名称查找部门
	 *
	 * @param departmentName
	 * @return
	 */
	Long findDepartmentByName(String departmentName, Long id);

	/**
	 * 根据名称模糊查找部门
	 *
	 * @param departmentName
	 * @return
	 */
	Page<DepartmentDTO> findDepartmentByLikeName(Long id, String departmentName,Long enterpriseId,int start, int limit);

	/**
	 * 删除部门
	 *
	 * @param departmentId
	 * @return
	 */
	Result deleteDepartment(Long departmentId);

	/**
	 * 修改部门
	 *
	 * @param department
	 * @return
	 */
	Result modifyDepartment(Department department);

	/**
	 * 根据父类id查找部门数量
	 *
	 * @param parentId
	 *            父类id
	 * @return
	 */
	Long findCountByParentId(Long parentId);

	/**
	 * 根据id查找部门数量
	 * @param id
	 * @return
	 */
	Long findCountById(Long id);

	/**
	 * 根据父级id查找部门集合
	 * @param list
	 * @return
	 */
	List<Department> findByParentIds(List<Long> list);
	
	/**
	 * 根据部门路径查找所有子部门
	 * @return
	 */
	List<Long> findByPath(String path);
}