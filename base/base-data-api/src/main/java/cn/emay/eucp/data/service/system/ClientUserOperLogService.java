package cn.emay.eucp.data.service.system;

import java.util.Date;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.ClientUserOperLog;
import cn.emay.eucp.common.moudle.db.system.User;

public interface ClientUserOperLogService {
	public  void save(String service, String module, User user, String content, String type);
	public Page<ClientUserOperLog> findall(int start,int limit,String username,String content,Date startDate,Date endDate,Long userId);
}
