package cn.emay.eucp.data.service.system;

import java.util.List;

import cn.emay.eucp.common.dto.auth.AuthTree;
import cn.emay.eucp.common.moudle.db.system.Resource;

/**
 * cn.emay.eucp.common.moudle.db.system.Resource Service Super
 * 
 * @author frank
 */
public interface ResourceService {

	/**
	 * 查询用户的所有可访问资源
	 * 
	 * @param userId
	 * @return
	 */
	List<Resource> findResourceByUserId(Long userId);

	/**
	 * 获取所有系统资源
	 * 
	 * @return
	 */
	List<Resource> getAllResource(String resourceFor);

	/**
	 * 根据父类id查询相关权限资源
	 * 
	 * @param id
	 * @return
	 */
	List<Resource> findResourceByParentId(Long id);

	/**
	 * 获取系统权限树
	 * 
	 * @return
	 */
	AuthTree findSystemAuthTree(String roleType);

	/**
	 * 根据权限类型查找权限
	 * 
	 * @param resourceType
	 * @return
	 */
	List<Resource> findAuthByType(String resourceType,String roleType);
	
	/**
	 * 获取客户端服务号资源详情
	 * @param resourceCode
	 * @param roleType
	 * @return
	 */
	Resource findClientServiceCode(String resourceCode,String roleType);

	List<Resource> findAll();

	AuthTree findMenuAuthTree(String resourceFor);

	List<Resource> findByConditions(String systemType, String businessType);
	
}