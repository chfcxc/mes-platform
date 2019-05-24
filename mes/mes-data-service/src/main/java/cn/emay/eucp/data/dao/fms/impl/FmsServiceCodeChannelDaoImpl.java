package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeChannelDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeChannel;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsServiceCodeChannelDao;

@Repository
public class FmsServiceCodeChannelDaoImpl extends PojoDaoImpl<FmsServiceCodeChannel> implements FmsServiceCodeChannelDao {

	@Override
	public List<FmsServiceCodeChannelDto> findChannelByAppIds(String... appIds) {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("select a.*,b.channel_name,b.report_type from fms_service_code_channel a,fms_channel b where a.channel_id = b.id ");
		if (null != appIds && appIds.length > 0) {
			sql.append(" and a.app_id in (");
			for (int i = 0; i < appIds.length; i++) {
				if (i != appIds.length - 1) {
					sql.append("?,");
				} else {
					sql.append("?)");
				}
				params.add(appIds[i]);
			}
		} else {
			return new ArrayList<FmsServiceCodeChannelDto>();
		}
		return this.findSqlForListForMysql(FmsServiceCodeChannelDto.class, sql.toString(), params, 0, 0);

	}

	@Override
	public List<FmsServiceCodeChannel> getListByServiceCodeId(Long serviceCodeId) {
		String sql = "select * from fms_service_code_channel where service_code_id ='" + serviceCodeId + "' ";
		return this.findSqlForListObj(FmsServiceCodeChannel.class, sql, null);
	}

	@Override
	public void deletebyServiceId(Long serviceCodeId) {
		String sql = "delete from fms_service_code_channel where service_code_id='" + serviceCodeId + "'";
		this.execSql(sql);

	}

	@Override
	public void saveServiceCodeChannel(List<FmsServiceCodeChannel> list) {
		List<Object[]> list1 = new ArrayList<Object[]>();
		for (FmsServiceCodeChannel channel : list) {
			list1.add(new Object[] { channel.getServiceCodeId(), channel.getAppId(), channel.getChannelId(), new Date(), channel.getOperatorCode(), channel.getTemplateType() });
		}
		String sql = "";
		if (null != list1 && list.size() > 0) {
			sql = " INSERT INTO fms_service_code_channel (service_code_id, app_id,channel_id,create_time,operator_code,template_type) VALUES (?,?,?,?,?,?) ";
			this.getJdbcTemplate().batchUpdate(sql, list1);
		}
	}

	@Override
	public List<FmsServiceCodeChannel> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		String sql = " select * from fms_service_code_channel where 1=1 ";
		List<Object> parameters = new ArrayList<Object>();
		if (null != date) {
			sql += " and last_update_time>=? ";
			parameters.add(date);
		}
		return this.findSqlForListForMysql(FmsServiceCodeChannel.class, sql, parameters, currentPage, pageSize);
	}

	@Override
	public List<FmsServiceCodeChannelDto> findBindingChannel(Long serviceCodeId) {
		String sql = "  select sc.id ,sc.channel_id ,c.channel_name,sc.app_id,sc.operator_code,sc.template_type ,c.report_type "
				+ "from fms_channel c ,fms_service_code_channel sc where c.id=sc.channel_id and sc.service_code_id='" + serviceCodeId + "' ";
		return this.findSqlForListObj(FmsServiceCodeChannelDto.class, sql, null);
	}

	@Override
	public FmsServiceCodeChannel findFmsServiceCodeChannel(String appId, int templateType, String operatorCode) {
		String sql = "  select sc.* " + "from fms_service_code_channel sc where sc.app_id='" + appId + "' and sc.template_type=" + templateType + " and sc.operator_code='" + operatorCode + "'";
		List<FmsServiceCodeChannel> list = this.findSqlForListObj(FmsServiceCodeChannel.class, sql, null);
		if (list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

}
