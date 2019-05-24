package cn.emay.eucp.data.dao.system;

import java.util.Date;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.ClientUserOperLog;
import cn.emay.eucp.data.dao.common.BaseSuperDao;

public interface ClientUserOperLogDao  extends BaseSuperDao<ClientUserOperLog>{
	/**
	 * 
	 * 分页查询
	 */
	Page<ClientUserOperLog> findall(int start,int limit,String username,String content,Date startDate,Date endDate,Long enterpriseId,Long userId);
}
