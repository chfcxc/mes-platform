package cn.emay.eucp.data.service.system.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign;
import cn.emay.eucp.data.dao.system.UserDao;
import cn.emay.eucp.data.dao.system.UserDepartmentAssignDao;
import cn.emay.eucp.data.service.system.UserDepartmentAssignService;

/**
 * cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign Service implement
 * 
 * @author frank
 */
@Service("userDepartmentAssignService")
public class UserDepartmentAssignServiceImpl implements UserDepartmentAssignService {

	@Resource
	private UserDepartmentAssignDao userDepartmentAssignDao;
	@Resource
	private UserDao userDao;
	@Override
	public void addDepartment(UserDepartmentAssign userDepartmentAssign) {
		userDepartmentAssignDao.save(userDepartmentAssign);
	}

	@Override
	public void deleteDataByUserId(Long userId) {
		userDepartmentAssignDao.deleteDataByUserId(userId);
	}

	@Override
	public Result deleteBatch(String userIds) {
		String[] ids = userIds.split(",");
		List<UserDepartmentAssign> list = new ArrayList<UserDepartmentAssign>();
		for (String id : ids) {
			UserDepartmentAssign ud = userDepartmentAssignDao.findByUserId(Long.valueOf(id));
			list.add(ud);
		}
		userDepartmentAssignDao.deleteBatchByPKid(list);
		return Result.rightResult();
	}

	@Override
	public Result addBatch(Long departmentId, String userIds) {
		String[] ids = userIds.split(",");
		List<UserDepartmentAssign> list = new ArrayList<UserDepartmentAssign>();
		for (String id : ids) {
			UserDepartmentAssign ud = new UserDepartmentAssign();
			ud.setDepartmentId(departmentId);
			ud.setUserId(Long.valueOf(id));
			list.add(ud);
		}
		userDepartmentAssignDao.saveBatch(list);
		return Result.rightResult();
	}

	@Override
	public UserDepartmentAssign findByUserId(Long userId) {
		return userDepartmentAssignDao.findByUserId(userId);
	}

	@Override
	public Long findByDepId(Long id) {
		return userDepartmentAssignDao.findByDepId(id);
	}

	@Override
	public void add(UserDepartmentAssign userDepartmentAssign) {
		userDepartmentAssignDao.save(userDepartmentAssign);
		
	}

	@Override
	public Long findUsernumber(Long userId) {
		Long enterpriseId = userDao.findenterpriseId(userId);
		Long usernumber = userDepartmentAssignDao.findUsernumber(enterpriseId);
		return usernumber;
	}

	@Override
	public List<Long> findByDepartmentIds(List<Long> list,int type) {
		return userDepartmentAssignDao.findByDepartmentIds(list,type);
	}
	
	@Override
	public List<Long> findAllUserByDeptId(Long deptId){
		return userDepartmentAssignDao.findAllUserByDeptId(deptId);
	}

}