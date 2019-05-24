package cn.emay.eucp.data.dao.system;

import java.util.Date;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

/**
 * cn.emay.eucp.common.moudle.db.system.ManageUserOperLog Dao super
 * 
 * @author frank
 */
public interface ManageUserOperLogDao extends BaseSuperDao<ManageUserOperLog> {

	Page<ManageUserOperLog> findByPage(String username, String content, Date startDate, Date endDate, int start, int limit);

}