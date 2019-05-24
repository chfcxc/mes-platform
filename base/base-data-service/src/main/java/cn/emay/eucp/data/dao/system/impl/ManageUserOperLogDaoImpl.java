package cn.emay.eucp.data.dao.system.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.moudle.db.system.ManageUserOperLog;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.system.ManageUserOperLogDao;
import cn.emay.eucp.util.RegularUtils;

/**
 * cn.emay.eucp.common.moudle.db.system.ManageUserOperLog Dao implement
 * 
 * @author frank
 */
@Repository
public class ManageUserOperLogDaoImpl extends PojoDaoImpl<ManageUserOperLog> implements ManageUserOperLogDao {

	@Override
	public Page<ManageUserOperLog> findByPage(String username, String content, Date startDate, Date endDate, int start, int limit) {
		Map<String, Object> param = new HashMap<String, Object>();
		String hql = "from ManageUserOperLog m where 1=1";
		if (!StringUtils.isEmpty(content)) {
			hql += " and  m.content like :context ";
			param.put("context", "%" + RegularUtils.specialCodeEscape(content) + "%");
		}
		if (!StringUtils.isEmpty(username)) {
			hql += " and  m.username = :username ";
			param.put("username", RegularUtils.specialCodeEscape(username));
		}
		if (!StringUtils.isEmpty(startDate)) {
			hql += " and m.operTime >= :startDate";
			param.put("startDate", startDate);
		}
		if (!StringUtils.isEmpty(endDate)) {
			hql += " and m.operTime <= :endDate";
			param.put("endDate", endDate);
		}
		hql += " order by m.id desc ";
		return this.getPageResult(hql, start, limit, param, ManageUserOperLog.class);
	}
}