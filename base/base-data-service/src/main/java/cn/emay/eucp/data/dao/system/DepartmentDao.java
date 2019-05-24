package cn.emay.eucp.data.dao.system;

import java.util.List;
import java.util.Map;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Department;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * cn.emay.eucp.common.moudle.db.system.Department Dao super
 *
 * @author frank
 */
public interface DepartmentDao extends BaseSuperDao<Department> {

	List<Department> findByParentId(Long parentId,Long enterpriseId);

	Long findDepartmentByName(String departmentName, Long id);

	Page<Department> findDepartmentByLikeName(Long id, String departmentName,Long enterpriseId,int start, int limit);

	Long findCountByParentId(Long parentId);

	List<Department> findByIds(List<Long> list);

	Long findCountById(Long id);

	List<Department> findByParentIds(List<Long> list);

	Department findById(Long id);

	/**
	 * 更新部门信息
	 * @param id
	 * @param clientNumber
	 * @param clientName
	 */
	void updateDepartment(Long id, String clientNumber, String clientName);
	List<Map<String, Object>> getDepartTree(Long parentId,Long enterpriseId);
	
	/**
	 * 根据部门id查找所有子部门
	 * @return
	 */
	List<Long> findByPath(String path);
}