package cn.emay.eucp.data.dao.fms.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import cn.emay.eucp.common.dto.fms.template.UpdateTemplateAuditStateDto;
import cn.emay.eucp.common.moudle.db.fms.FmsTemplateChannelReport;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsTemplateChannelReportDao;

@Repository
public class FmsTemplateChannelReportDaoImpl extends PojoDaoImpl<FmsTemplateChannelReport> implements FmsTemplateChannelReportDao {

	@Override
	public List<FmsTemplateChannelReport> findByTempLetIdAndChannelId(Map<String, Set<Long>> queryMap) {
		if (queryMap.isEmpty()) {
			return new ArrayList<FmsTemplateChannelReport>();
		}
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("select a from FmsTemplateChannelReport a where ");
		int i = 0;
		for (Entry<String, Set<Long>> entry : queryMap.entrySet()) {
			String param1 = "templateId" + i;
			String param2 = "channelIds" + i;
			String append = "";
			if (i > 0) {
				append = "or";
			}
			hql.append(append).append(" (a.templateId=:" + param1 + " and a.channelId in(:" + param2 + "))");
			params.put(param1, entry.getKey());
			params.put(param2, entry.getValue());
			i++;
		}
		return this.getListResult(FmsTemplateChannelReport.class, hql.toString(), params);
	}

	@Override
	public void updateAuditState(List<UpdateTemplateAuditStateDto> list, final boolean isByOperatorCode) {
		String sql = "update fms_template_channel_report set state = ?,channel_template_id =?,audit_time = ? where template_id = ? and channel_id=?";
		if (isByOperatorCode) {
			sql += " and operator_code = ? ";
		}
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				UpdateTemplateAuditStateDto dto = list.get(i);
				ps.setInt(1, dto.getAuditState());
				ps.setString(2, dto.getChannelTemplateId());
				ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
				ps.setString(4, dto.getTemplateId());
				ps.setLong(5, dto.getChannelId());
				if (isByOperatorCode) {
					ps.setString(6, dto.getOperatorCode());
				}
			}

			@Override
			public int getBatchSize() {
				return list.size();
			}
		});
	}

	@Override
	public List<FmsTemplateChannelReport> findByTemplateId(String templateId) {
		if (null == templateId) {
			return new ArrayList<FmsTemplateChannelReport>();
		}
		String hql = "from FmsTemplateChannelReport where templateId=:templateId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("templateId", templateId);
		return this.getListResult(FmsTemplateChannelReport.class, hql, params);
	}

	@Override
	public List<FmsTemplateChannelReport> findByTemplateIdAndChannelId(String templateId, Long channelId) {
		if (null == templateId && null == channelId) {
			return new ArrayList<FmsTemplateChannelReport>();
		}
		String hql = "from FmsTemplateChannelReport where 1=1";
		Map<String, Object> params = new HashMap<String, Object>();
		if (null != templateId) {
			hql += " and  templateId=:templateId ";
			params.put("templateId", templateId);
		}
		if (null != channelId) {
			hql += " and  channelId=:channelId ";
			params.put("channelId", channelId);
		}
		return this.getListResult(FmsTemplateChannelReport.class, hql, params);
	}

	@Override
	public FmsTemplateChannelReport findByParams(String templateId, Long channelId, String operatorCode) {
		if (null == templateId || null == channelId || null == operatorCode) {
			return null;
		}
		String hql = "from FmsTemplateChannelReport where templateId=:templateId and channelId=:channelId and operatorCode=:operatorCode ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("templateId", templateId);
		params.put("channelId", channelId);
		params.put("operatorCode", operatorCode);
		return this.getUniqueResult(FmsTemplateChannelReport.class, hql, params);
	}

	@Override
	public void deleteByTemplateIdAndChannelId(String templateId, Long channelId) {
		if (null != templateId && null != channelId) {
			String hql = "delete FmsTemplateChannelReport where templateId=:templateId and channelId=:channelId";
			Map<String, Object> params = new HashMap<String, Object>();
			this.execByHql(hql, params);
		}
	}

	@Override
	public void saveBatchByBeans(List<FmsTemplateChannelReport> list) {
		this.nameJdbcBatchExec(list, namedParameterJdbcTemplate, null, false, false);
	}

	@Override
	public List<FmsTemplateChannelReport> findByLastUpdateTime(Date date, int currentPage, int pageSize) {
		String sql = " select * from fms_template_channel_report where 1=1 ";
		List<Object> parameters = new ArrayList<Object>();
		if (null != date) {
			sql += " and last_update_time >= ?";
			parameters.add(date);
		}
		return this.findSqlForListForMysql(FmsTemplateChannelReport.class, sql, parameters, currentPage, pageSize);
	}
}
