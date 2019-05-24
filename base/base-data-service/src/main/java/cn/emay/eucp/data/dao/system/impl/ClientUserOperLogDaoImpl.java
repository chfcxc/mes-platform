package cn.emay.eucp.data.dao.system.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.ClientUserOperLog;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.ClientUserOperLogDao;
import cn.emay.eucp.util.RegularUtils;
@Repository
public class ClientUserOperLogDaoImpl extends PojoDaoImpl<ClientUserOperLog> implements ClientUserOperLogDao{

	@Override
	public Page<ClientUserOperLog> findall(int start, int limit, String username, String content, Date startDate, Date endDate,Long enterpriseId,Long userId) {
		String sql=" SELECT l.* FROM system_client_user_oper_log l,system_user_department_assign ud,system_enterprise_department ed "+
				" where l.user_id=ud.system_user_id and ud.enterprise_department_id=ed.id and ed.enterprise_id=? ";
		List<Object> param = new ArrayList<Object>();
		param.add(enterpriseId);
		if(!StringUtils.isEmpty(userId)){
			sql += " and user_id=? ";
			param.add(userId);
		}
		if(!StringUtils.isEmpty(username)){
			sql +=" and l.username =? ";
			param.add(username);
		}
		if(!StringUtils.isEmpty(content)){
			sql +=" and l.content like ? ";
			param.add("%" + RegularUtils.specialCodeEscape(content) + "%");
		}
		if(!StringUtils.isEmpty(startDate)){
			sql +=" and l.oper_time >= ? ";
			param.add(startDate);
		}
		if(!StringUtils.isEmpty(endDate)){
			sql +=" and l.oper_time <= ? ";
			param.add(endDate);
		}
		sql +=" order by l.id desc ";
		
		return this.findSqlForPageForMysql(ClientUserOperLog.class, sql, param, start, limit);
	}

	

}
