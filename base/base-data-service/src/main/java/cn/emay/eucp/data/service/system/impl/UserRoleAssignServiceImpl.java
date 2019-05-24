package cn.emay.eucp.data.service.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.eucp.common.moudle.db.system.UserRoleAssign;
import cn.emay.eucp.data.dao.system.UserRoleAssignDao;
import cn.emay.eucp.data.service.system.UserRoleAssignService;

/**
 * cn.emay.eucp.common.moudle.db.system.UserRoleAssign Service implement
 * 
 * @author frank
 */
@Service("userRoleAssignService")
public class UserRoleAssignServiceImpl implements UserRoleAssignService {

	@Resource
	private UserRoleAssignDao userRoleAssignDao;

	@Override
	public void saveBatch(List<UserRoleAssign> urs) {
		userRoleAssignDao.saveBatch(urs);
	}

	@Override
	public void deleteByUserId(Long userId) {
		userRoleAssignDao.deleteByUserId(userId);
	}

	@Override
	public List<UserRoleAssign> findByUserId(Long userId) {
		return userRoleAssignDao.findByUserId(userId);
	}

	@Override
	public Long getNotDeleteUserCountByRole(Long roleId) {
		return userRoleAssignDao.getNotDeleteUserCountByRole(roleId);
	}
	
	@Override
	public List<Map<String,Object>> findUserRole(Long userId, String roleType) {
		List<Map<String,Object>> userRoleList=new ArrayList<Map<String,Object>>();
		List<?> list=userRoleAssignDao.findUserRole(userId, roleType);
		for(Object object:list){
			Object[] obj=(Object[])object;
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("roleId", obj[0]);
			map.put("roleName", obj[1]);
			map.put("userId", obj[2]);
			userRoleList.add(map);
		}
		return userRoleList;
	}

	@Override
	public void add(UserRoleAssign userRoleAssign) {
			userRoleAssignDao.save(userRoleAssign);
	}

	@Override
	public void update(UserRoleAssign userRoleAssign) {
		userRoleAssignDao.update(userRoleAssign);
		
	}

	@Override
	public List<UserRoleAssign> findclientByUserId(Long userId) {
		return userRoleAssignDao.findclientByUserId(userId);
	}

}