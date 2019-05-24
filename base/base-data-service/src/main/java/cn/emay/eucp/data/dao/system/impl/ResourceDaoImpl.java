package cn.emay.eucp.data.dao.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.eucp.common.moudle.db.system.Resource;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.ResourceDao;

/**
 * cn.emay.eucp.common.moudle.db.system.Resource Dao implement
 * 
 * @author frank
 */
@Repository
public class ResourceDaoImpl extends PojoDaoImpl<Resource> implements ResourceDao {

	@Override
	public List<Resource> findResourceByUserId(Long userId) {
		String hql = " select r from UserRoleAssign ura , RoleResourceAssign rra , Resource r where  ura.userId = :userId and rra.roleId = ura.roleId and r.id = rra.resourceId";
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		return this.getListResult(Resource.class, hql, param);
	}

	@Override
	public List<Resource> getAllResource(String resourceFor) {
		return this.findListByProperty("resourceFor", resourceFor);
	}

	@Override
	public List<Resource> findResourceByParentId(Long id) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "from Resource where parentResourceId= :parentResourceId order by resourceIndex";
		param.put("parentResourceId", id);
		return this.getListResult(Resource.class, hql, param);
	}

	@Override
	public List<Resource> findAuthByType(String resourceType,String roleType) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "from Resource where  resourceFor=:resourceFor";
		if(!StringUtils.isEmpty(resourceType)){
			hql+=" and resourceType=:resourceType";
			param.put("resourceType", resourceType);
		}
		param.put("resourceFor", roleType);
		return this.getListResult(Resource.class, hql, param);
	}

	@Override
	public Resource findClientServiceCode(String resourceCode, String roleType) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "from Resource where resourceCode=:resourceCode and resourceFor=:resourceFor";
		param.put("resourceCode", resourceCode);
		param.put("resourceFor", roleType);
		return this.getUniqueResult(Resource.class, hql, param);
	}
}