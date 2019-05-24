package cn.emay.eucp.data.service.system.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.eucp.common.cache.CenterBaseRedisCache;
import cn.emay.eucp.common.dto.auth.AuthTree;
import cn.emay.eucp.common.moudle.db.system.Resource;
import cn.emay.eucp.data.dao.system.ResourceDao;
import cn.emay.eucp.data.service.system.ResourceService;

/**
 * Resource Service implement
 * 
 * @author frank
 */
@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {

	@Autowired
	private ResourceDao resourceDao;
	@javax.annotation.Resource
	private RedisClient redis;
	
	@Override
	public List<Resource> findResourceByUserId(Long userId) {
		return resourceDao.findResourceByUserId(userId);
	}
	
	@Override
	public List<Resource> findAll(){
		Resource[] listStr = redis.get(CenterBaseRedisCache.SYSTEM_RESOURCE_ALL,Resource[].class);
		if(listStr == null ||  listStr.length==0){
			List<Resource> list = resourceDao.findAll();
			redis.set(CenterBaseRedisCache.SYSTEM_RESOURCE_ALL,list, 60);
			return list;
		}
		return Arrays.asList(listStr);
	}

	@Override
	public List<Resource> getAllResource(String resourceFor) {
		return resourceDao.getAllResource(resourceFor);
	}

	@Override
	public List<Resource> findResourceByParentId(Long id) {
		return resourceDao.findResourceByParentId(id);
	}

	@Override
	public AuthTree findSystemAuthTree(String roleType) {
		List<Resource> opers = this.findAuthByType(Resource.RESOURCE_TYPE_OPER,roleType);
		List<Resource> pages = this.findAuthByType(Resource.RESOURCE_TYPE_PAGE,roleType);
		List<Resource> navigations = this.findAuthByType(Resource.RESOURCE_TYPE_NAV,roleType);
		List<Resource> moduleTions = this.findAuthByType(Resource.RESOURCE_TYPE_MOUDLE,roleType);
		AuthTree sysauth = new AuthTree(opers, pages, navigations, moduleTions);
		return sysauth;
	}

	@Override
	public List<Resource> findAuthByType(String resourceType,String roleType) {
		List<Resource> list=resourceDao.findAuthByType(resourceType,roleType);
		return list;
	}

	@Override
	public Resource findClientServiceCode(String resourceCode, String roleType) {
		return resourceDao.findClientServiceCode(resourceCode, roleType);
	}

	@Override
	public AuthTree findMenuAuthTree(String resourceFor) {
		List<Resource> resourceList=resourceDao.getAllResource(resourceFor);
		List<Resource> pages=new ArrayList<Resource>();
		List<Resource> navigations=new ArrayList<Resource>();
		List<Resource> moduleTions=new ArrayList<Resource>();
		for(Resource resource:resourceList){
			if(Resource.RESOURCE_TYPE_PAGE.equals(resource.getResourceType())){
				pages.add(resource);
			}else if(Resource.RESOURCE_TYPE_NAV.equals(resource.getResourceType())){
				navigations.add(resource);
			}else if(Resource.RESOURCE_TYPE_MOUDLE.equals(resource.getResourceType())){
				moduleTions.add(resource);
			}
		}
		AuthTree sysauth = new AuthTree(null, pages, navigations, moduleTions);
		return sysauth;
	}
	
	@Override
	public List<Resource> findByConditions(String systemType,String businessType){
		List<Resource> list = new ArrayList<Resource>();
		if(StringUtils.isEmpty(businessType)){//系统服务businessType为null
			businessType="";
		}
		List<Resource> allList = this.findAll();
		for(Resource resource :allList){
			if(!systemType.equalsIgnoreCase(resource.getResourceFor())){
				continue;
			}
			if(!businessType.equalsIgnoreCase(resource.getBusinessType()) && !Resource.RESOURCE_TYPE_MOUDLE.equalsIgnoreCase(resource.getResourceType())){
				continue;
			}
			list.add(resource);
		}
		return list;
	}

}