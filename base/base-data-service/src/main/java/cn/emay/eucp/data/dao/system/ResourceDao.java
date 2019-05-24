package cn.emay.eucp.data.dao.system;

import java.util.List;

import cn.emay.eucp.common.moudle.db.system.Resource;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * cn.emay.eucp.common.moudle.db.system.Resource Dao super
 * 
 * @author frank
 */
public interface ResourceDao extends BaseSuperDao<Resource> {

	List<Resource> findResourceByUserId(Long userId);

	List<Resource> getAllResource(String resourceFor);

	List<Resource> findResourceByParentId(Long id);

	List<Resource> findAuthByType(String resourceType,String roleType);
	
	Resource findClientServiceCode(String resourceCode,String roleType);

}