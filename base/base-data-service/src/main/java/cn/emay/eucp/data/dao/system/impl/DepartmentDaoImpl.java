package cn.emay.eucp.data.dao.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.Department;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.DepartmentDao;
import cn.emay.eucp.util.RegularUtils;

/**
 * cn.emay.eucp.common.moudle.db.system.Department Dao implement
 *
 * @author frank
 */
@Repository
public class DepartmentDaoImpl extends PojoDaoImpl<Department> implements DepartmentDao {

	@Override
	public List<Department> findByParentId(Long parentId,Long enterpriseId) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "from Department where isDelete=:isDelete and parentDepartmentId = :parentId and enterpriseId =:enterpriseId";
		param.put("isDelete", false);
		param.put("parentId", parentId);
		param.put("enterpriseId", enterpriseId);
		return this.getListResult(Department.class, hql, param);
	}

	@Override
	public Long findDepartmentByName(String departmentName, Long id) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "SELECT count(*) from Department where departmentName=:departmentName and isDelete =:isDelete";
		if (id != null) {
			hql = hql + " and id <>:id";
			param.put("id", id);
		}
		param.put("departmentName", departmentName);
		param.put("isDelete", false);
		return (Long) super.getUniqueResult(hql, param);
	}

	@Override
	public Page<Department> findDepartmentByLikeName(Long id, String departmentName,Long enterpriseId,int start,int limit) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "from Department where isDelete=:isDelete and parentDepartmentId =:id and enterpriseId =:enterpriseId";
		param.put("isDelete", false);
		param.put("id", id);
		param.put("enterpriseId", enterpriseId);
		if (!StringUtils.isEmpty(departmentName)) {
			hql = hql + " and departmentName like:departmentName";
			param.put("departmentName", "%" + RegularUtils.specialCodeEscape(departmentName) + "%");
		}
		Page<Department> page = this.getPageResult(hql, start, limit, param, Department.class);
		return page;
	}

	@Override
	public Long findCountByParentId(Long parentId) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "SELECT count(*) from Department where isDelete=:isDelete and parentDepartmentId = :parentId";
		param.put("isDelete", false);
		param.put("parentId", parentId);
		return (Long) super.getUniqueResult(hql, param);
	}

	@Override
	public List<Department> findByIds(List<Long> list) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "from Department where isDelete=:isDelete and id in(:ids)";
		param.put("isDelete", false);
		param.put("ids", list);
		return this.getListResult(Department.class, hql, param);
	}

	@Override
	public Long findCountById(Long id) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "SELECT count(*) from Department where isDelete=:isDelete and id = :id and parentDepartmentId=:paId";
		param.put("isDelete", false);
		param.put("id", id);
		param.put("paId",Department.MANAGE_ENTERPRISEID);
		return (Long) super.getUniqueResult(hql, param);
	}

	@Override
	public List<Department> findByParentIds(List<Long> list) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "from Department where isDelete=:isDelete and parentDepartmentId in(:ids)";
		param.put("isDelete", false);
		param.put("ids", list);
		return this.getListResult(Department.class, hql, param);
	}

	@Override
	public Department findById(Long id) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "from Department where isDelete=:isDelete and id = :id";
		param.put("isDelete", false);
		param.put("id", id);
		return this.getUniqueResult(Department.class, hql, param);
	}

	@Override
	public void updateDepartment(Long id,String clientNumber,String clientName) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = "update Department set departmentName=:departmentName where id = :id";
		params.put("id", id);
		params.put("departmentName", "["+clientNumber+"]"+clientName);
		this.execByHql(hql, params);
	}
	public List<Map<String, Object>> getDepartTree(Long parentId,Long enterpriseId){
		String sql ="SELECT a.id,\n" +
					"       a. department_name 'name',\n" +
					"       a.parent_department_id pid,\n" +
					"       CASE\n" +
					"         WHEN (SELECT COUNT(*)\n" +
					"                 FROM system_enterprise_department b\n" +
					"                WHERE b. is_delete = FALSE\n" +
					"                  AND b. parent_department_id = a. id) > 0 THEN\n" +
					"          'true'\n" +
					"         ELSE\n" +
					"          'false'\n" +
					"       END 'isParent',\n" +
					"       'pIcon01' as 'iconSkin'\n" +
					"  FROM system_enterprise_department a\n" +
					" WHERE a. parent_department_id = ?\n" +
					"   AND a. is_delete = FALSE\n" +
					"   AND a. enterprise_id =?";
		List<Map<String, Object>> queryForList = this.jdbcTemplate.queryForList(sql, new Object[]{parentId,enterpriseId});
		return queryForList;
	}

	@Override
	public List<Long> findByPath(String str) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "select id from Department  where isDelete=:isDelete and fullPath like :str";
		param.put("isDelete", false);
		param.put("str", str+"%");
		return this.getListResult(Long.class, hql, param);
	}
}