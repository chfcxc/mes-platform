package cn.emay.eucp.data.service.system.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.ClientUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.common.moudle.db.system.UserDepartmentAssign;
import cn.emay.eucp.data.dao.system.ClientUserOperLogDao;
import cn.emay.eucp.data.dao.system.UserDao;
import cn.emay.eucp.data.dao.system.UserDepartmentAssignDao;
import cn.emay.eucp.data.service.system.ClientUserOperLogService;

@Service("clientUserOperLogService")
public class ClientUserOperLogServiceImpl implements ClientUserOperLogService{
	@Resource
	private ClientUserOperLogDao clientUserOperLogDao;
	@Resource
	private UserDao userDao;
	@Resource
	private UserDepartmentAssignDao userDepartmentAssignDao;
	@Override
	public Page<ClientUserOperLog> findall(int start, int limit, String username, String content, Date startDate, Date endDate,Long userId) {
		Long enterpriseId = userDao.findenterpriseId(userId);
		UserDepartmentAssign userDepartmentAssign = userDepartmentAssignDao.findByUserId(userId);
		Page<ClientUserOperLog> page =null;
		if(userDepartmentAssign.getIdentity()==1){
			 page = clientUserOperLogDao.findall(start, limit, username, content, startDate, endDate,enterpriseId,null);
		}else{
			 page = clientUserOperLogDao.findall(start, limit, username, content, startDate, endDate,enterpriseId,userId);
		}
		return page;
	}
	@Override
	public void save(String service, String module, User user, String content, String type) {
		ClientUserOperLog log=new ClientUserOperLog(user.getId(), user.getUsername(), module, content, type, new Date(), service);
		clientUserOperLogDao.save(log);
	}

}
