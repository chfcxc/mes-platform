package cn.emay.eucp.data.service.system.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;
import cn.emay.eucp.data.dao.system.ManageUserOperLogDao;
import cn.emay.eucp.data.service.system.ManageUserOperLogService;
import cn.emay.eucp.util.RegularUtils;

/**
 * cn.emay.eucp.common.moudle.db.system.ManageUserOperLog Service implement
 * 
 * @author frank
 */
@Service("manageUserOperLogService")
public class ManageUserOperLogServiceImpl implements ManageUserOperLogService {

	@Resource
	private ManageUserOperLogDao manageUserOperLogDao;

	@Override
	public void saveLog(String service, String module, User user, String content, String type) {
		ManageUserOperLog log = new ManageUserOperLog();
		log.setContent(content);
		log.setModule(module);
		log.setUsername(user.getUsername());
		log.setOperTime(new Date());
		log.setType(type);
		log.setUserId(user.getId());
		log.setService(service);
		manageUserOperLogDao.save(log);
	}

	@Override
	public Page<ManageUserOperLog> findByPage(String username, String content, Date startDate, Date endDate, int start, int limit) {
		if(!RegularUtils.isEmpty(username)){
			username = username.toLowerCase();
		}
		return manageUserOperLogDao.findByPage(username, content, startDate, endDate, start, limit);
	}

	@Override
	public void saveLogReDe(String service, String module, String username,String content, String type) {
		ManageUserOperLog log = new ManageUserOperLog();
		log.setContent(content);
		log.setModule(module);
		log.setUsername(username);
		log.setOperTime(new Date());
		log.setType(type);
		log.setService(service);
		manageUserOperLogDao.save(log);
	}

}