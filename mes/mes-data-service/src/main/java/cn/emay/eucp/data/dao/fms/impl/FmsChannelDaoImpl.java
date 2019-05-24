package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.channel.FmsChannelDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsChannelDao;

/**
 * @author dejun
 * @version 创建时间：2019年4月26日 下午4:02:05 类说明
 */
@Repository
public class FmsChannelDaoImpl extends PojoDaoImpl<FmsChannel> implements FmsChannelDao {

	@Override
	public Page<FmsChannelDTO> findPage(String channeName, String channelNumber, int state, int start, int limit, Long businessId) {
		String sql = " select c.id,c.channel_name,c.channel_number,c.state,c.providers,c.template_type,c.business_type_id from fms_channel c, fms_business_type b where c.business_type_id =b.id ";
		List<Object> list = new ArrayList<Object>();
		if (!StringUtils.isEmpty(channeName)) {
			sql += " and c.channel_name =? ";
			list.add(channeName);
		}
		if (!StringUtils.isEmpty(channelNumber)) {
			sql += " and c.channel_number =? ";
			list.add(channelNumber);
		}
		if (state != -1) {
			sql += " and c.state =? ";
			list.add(state);
		}
		if (businessId != 0L) {
			sql += " and  c.business_type_id in( select id from fms_business_type WHERE parent_id =? ) ";
			list.add(businessId);
		}

		return this.findSqlForPageForMysql(FmsChannelDTO.class, sql, list, start, limit);
	}

	@Override
	public List<FmsChannel> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		String sql = " select * from fms_channel where 1=1 ";
		List<Object> parameters = new ArrayList<Object>();
		if (null != date) {
			sql += " and last_update_time>=? ";
			parameters.add(date);
		}
		return this.findSqlForListForMysql(FmsChannel.class, sql, parameters, currentPage, pageSize);
	}

	@Override
	public FmsChannel findIdandState(Long id, String controlTypes) {
		if (null != controlTypes && !"".equals(controlTypes)) {
			String hql = "select sc from FmsChannel sc where sc.id =:id ";
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("id", Long.valueOf(id));
			return (FmsChannel) this.getUniqueResult(hql, params);
		}
		return null;
	}

	@Override
	public void updateChannelState(Long id, int state) {
		String sql = " update fms_channel set state='" + state + "' where id='" + id + "' ";
		this.execSql(sql);
	}

	@Override
	public Long isExist(Long id, String channeName) {
		List<Object> list = new ArrayList<Object>();
		String sql = "select count(id) from fms_channel where channel_name=?  ";
		list.add(channeName);
		if (id != 0L) {
			sql += " and id !=?";
			list.add(id);
		}
		return (Long) this.getUniqueResultBySql(sql, list.toArray());
	}

	@Override
	public List<FmsChannel> findAll(int state) {
		Map<String, Object> params = new HashMap<String, Object>();
		String hql = " from FmsChannel where 1=1 ";
		if (state != -1) {
			hql += " and state =:state ";
			params.put("state", state);
		}
		return this.getListResult(FmsChannel.class, hql, params);
	}

}
