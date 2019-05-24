package cn.emay.eucp.data.service.system;

import java.util.Date;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;

/**
 * cn.emay.eucp.common.moudle.db.system.ManageUserOperLog Service Super
 * 
 * @author frank
 */
public interface ManageUserOperLogService {

	/**
	 * 存储日志
	 * 
	 * @param userOperLog
	 */
	void saveLog(String service, String module, User user, String content, String type);
	
	/**
	 * 存储日志(接口充值特殊日志)
	 * 
	 * @param userOperLog
	 */
	void saveLogReDe(String service, String module, String username, String content, String type);

	/**
	 * 分页查询
	 * 
	 * @param username
	 * @param start
	 * @param limit
	 * @return
	 */
	Page<ManageUserOperLog> findByPage(String username, String content, Date startDate, Date endDate, int start, int limit);

}