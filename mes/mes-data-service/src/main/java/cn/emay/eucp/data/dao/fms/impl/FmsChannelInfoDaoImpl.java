package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.emay.eucp.common.moudle.db.fms.FmsChannelInfo;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsChannelInfoDao;

@Repository
public class FmsChannelInfoDaoImpl extends PojoDaoImpl<FmsChannelInfo> implements FmsChannelInfoDao {

	@Override
	public List<FmsChannelInfo> findByFmsChannelId(Long channelId) {
		List<Object> parameters = new ArrayList<Object>();
		String sql=" select * from fms_channel_info where channel_id =? ";
		parameters.add(channelId);
		return this.findSqlForListObj(FmsChannelInfo.class, sql, parameters);
	}

	@Override
	public void deleteByChannelId(Long channelId) {
		String sql = " delete from fms_channel_info where channel_id = '" + channelId + "'";
		this.execSql(sql);
	}

	@Override
	public List<FmsChannelInfo> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		String sql = " select * from fms_channel_info where 1=1 ";
		List<Object> parameters = new ArrayList<Object>();
		if (null != date) {
			sql += " and last_update_time >= ?";
			parameters.add(date);
		}
		return this.findSqlForListForMysql(FmsChannelInfo.class, sql, parameters, currentPage, pageSize);
	}

}
