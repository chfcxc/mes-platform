package cn.emay.eucp.data.dao.fms.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.message.FmsMessagePageDto;
import cn.emay.eucp.common.dto.report.UpdateFmsDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeDay;
import cn.emay.eucp.common.moudle.db.fms.FmsMessage;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionDay;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsMessageDao;
import cn.emay.util.DateUtil;

/**
 * @author dejun
 * @version 创建时间：2019年4月26日 下午4:02:05 类说明
 */
@Repository
public class FmsMessageDaoImpl extends PojoDaoImpl<FmsMessage> implements FmsMessageDao {

	@Override
	public void saveBatchByBeans(List<FmsMessage> beans) {
		this.nameJdbcBatchExec(beans, namedParameterJdbcTemplate, null, false, true);
	}

	@Override
	public void updateFmsResponse(List<UpdateFmsDTO> updateFmsDTOs) {
		String sql = "update fms_message set state=?,operator_id = ?,channel_response_time=?  where id =? and state<=?";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return updateFmsDTOs.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				UpdateFmsDTO entity = updateFmsDTOs.get(i);
				String channelResponseTime = entity.getChannelResponseTime();
				int state = entity.getState();
				String fmsId = entity.getFmsId();
				ps.setInt(1, state);
				if (null != entity.getOperatorFmsId()) {
					ps.setString(2, entity.getOperatorFmsId());
				} else {
					ps.setNull(2, Types.VARCHAR);
				}
				ps.setString(3, channelResponseTime);
				ps.setString(4, fmsId);
				ps.setInt(5, state);
			}
		});

	}

	@Override
	public void updateFmsReport(List<UpdateFmsDTO> updateFmsDTOs) {
		String sql = "update fms_message  set state=?,operator_id = ?,channel_report_time=? ,channel_report_state=? ,channel_report_desc=?  where id =? and state<=?";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return updateFmsDTOs.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				UpdateFmsDTO entity = updateFmsDTOs.get(i);
				int state = entity.getState();
				Date reportTime = DateUtil.parseDate(entity.getReportTime(), "yyyy-MM-dd HH:mm:ss");
				String channelReportState = entity.getChannelReportState();
				String channelReportDesc = entity.getChannelReportDesc();
				String fmsId = entity.getFmsId();
				ps.setInt(1, state);
				if (null != entity.getOperatorFmsId()) {
					ps.setString(2, entity.getOperatorFmsId());
				} else {
					ps.setNull(2, Types.VARCHAR);
				}
				ps.setTimestamp(3, new java.sql.Timestamp(reportTime.getTime()));
				ps.setString(4, channelReportState);
				ps.setString(5, channelReportDesc);
				ps.setString(6, fmsId);
				ps.setInt(7, state);
			}
		});

	}

	@Override
	public Page<FmsMessagePageDto> findPage(String batchNumber, String title, Long serviceCodeId, int state, int sendType, int infoType, List<Long> contentIds, int start, int limit, Date startTime,
			Date endTime, Set<Long> set, int operator) {
		String sql = "";
		List<Object> list = new ArrayList<Object>();
		if (contentIds != null && contentIds.size() > 0) {
			sql += "select m.*,fbt.save_type from fms_message m,fms_business_type fbt where m.content_type_id=fbt.id and fbt.id in ("
					+ org.apache.commons.lang3.StringUtils.join(contentIds.toArray(), ",") + ")";
		} else {
			sql += "select m.*,fbt.save_type from fms_message m,fms_business_type fbt where m.content_type_id=fbt.id ";
		}
		if (!StringUtils.isEmpty(batchNumber)) {
			sql += " and m.batch_number =? ";
			list.add(batchNumber);
		}
		if (!StringUtils.isEmpty(title)) {
			sql += " and m.title =? ";
			list.add(title);
		}
		if (serviceCodeId != 0L) {
			sql += " and m.service_code_id =? ";
			list.add(serviceCodeId);
		}
		if (state != -1) {
			sql += " and m.state =? ";
			list.add(state);
		}
		if (sendType != -1) {
			sql += " and m.send_type =? ";
			list.add(sendType);
		}
		if (operator != -1) {
			sql += " and m.operator_code =? ";
			list.add(operator);
		}
		if (infoType != -1) {
			sql += " and m.template_type =? ";
			list.add(infoType);
		}
		if (null != startTime) {
			String starttime = DateUtil.toString(startTime, "yyyy-MM-dd HH:mm:ss");
			sql += " and m.submit_time >= ?";
			list.add(starttime);
		}
		if (null != endTime) {
			String endtime = DateUtil.toString(endTime, "yyyy-MM-dd HH:mm:ss");
			sql += " and m.submit_time <= ?";
			list.add(endtime);
		}
		if (!set.isEmpty()) {
			sql += " and m.service_code_id in ( " + org.apache.commons.lang3.StringUtils.join(set.toArray(), ",") + " )";
		}
		sql += " order by m.id desc ";
		return this.findSqlForPageForMysql(FmsMessagePageDto.class, sql, list, start, limit);

	}

	@Override
	public List<FmsServicecodeConsumptionDay> findConsumptionReport(String day, int currentPage, int pageSize) {
		// Map<String, Object> map = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		String startTime = day + " 00:00:00";
		String endTime = day + " 23:59:59";
		// Date startTime = DateUtil.parseDate(, "yyyy-MM-dd HH:mm:ss");
		// Date endTime = DateUtil.parseDate(day + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
		String sql = "  select m.service_code_id,m.enterprise_id,m.app_id,COUNT(1) as totalNumber,SUM(CASE when m.operator_code='CMCC' THEN 1 ELSE 0 END) as cmccNumber, "
				+ "	SUM(CASE when m.operator_code='CUCC' THEN 1 ELSE 0 END) as cuccNumber,SUM(CASE when m.operator_code='CTCC' THEN 1 ELSE 0 END) as ctccNumber, "
				+ "	SUM(CASE when m.state=3 THEN 1 ELSE 0 END) as successNumber, SUM(CASE when m.state=3 and m.operator_code='CMCC' THEN 1 ELSE 0 END) as cmccSuccessNumber,  "
				+ "	SUM(CASE when m.state=3 and m.operator_code='CUCC' THEN 1 ELSE 0 END) as cuccSuccessNumber,  "
				+ "	SUM(CASE when m.state=3 and m.operator_code='CTCC' THEN 1 ELSE 0 END) as ctccSuccessNumber,  SUM(CASE when m.state=4 THEN 1 ELSE 0 END) as failNumber, "
				+ "	SUM(CASE when m.state=4 and m.operator_code='CMCC' THEN 1 ELSE 0 END) as cmccFailNumber, SUM(CASE when m.state=4 and m.operator_code='CUCC' THEN 1 ELSE 0 END) as cuccFailNumber,  "
				+ "	SUM(CASE when m.state=4 and m.operator_code='CTCC' THEN 1 ELSE 0 END) as ctccFailNumber, SUM(CASE when m.state=5 THEN 1 ELSE 0 END ) as timeoutNumber,  "
				+ "	SUM(CASE when m.state=5 and m.operator_code='CMCC' THEN 1 ELSE 0 END) as cmccTimeoutNumber,  "
				+ "	SUM(CASE when m.state=5 and m.operator_code='CUCC' THEN 1 ELSE 0 END) as cuccTimeoutNumber,  "
				+ "	SUM(CASE when m.state=5 and m.operator_code='CTCC' THEN 1 ELSE 0 END) as ctccTimeoutNumber "
				+ " from fms_message m  where m.submit_time >=? and m.submit_time <=? GROUP BY m.service_code_id, m.enterprise_id,m.app_id";
		/*
		 * map.put("startTime", startTime); map.put("endTime", endTime);
		 */
		list.add(startTime);
		list.add(endTime);

		// List<?> pageListResultBySqlByHibernate = this.getPageListResultBySqlByHibernate(sql, start, limit, map);

		return this.findSqlForListForMysql(FmsServicecodeConsumptionDay.class, sql, list, currentPage, pageSize);
	}

	@Override
	public List<FmsEnterpriseContentTypeDay> findContentTypeReport(String day, int currentPage, int pageSize) {
		/*
		 * Map<String, Object> map = new HashMap<String, Object>(); Date startTime = DateUtil.parseDate(day + " 00:00:00", "yyyy-MM-dd HH:mm:ss"); Date endTime = DateUtil.parseDate(day + " 23:59:59",
		 * "yyyy-MM-dd HH:mm:ss");
		 */
		List<Object> list = new ArrayList<Object>();
		String startTime = day + " 00:00:00";
		String endTime = day + " 23:59:59";
		String sql = " select m.content_type_id as businessTypeId,m.enterprise_id,COUNT(1) as totalNumber,SUM(CASE when m.operator_code='CMCC' THEN 1 ELSE 0 END) as cmccNumber, "
				+ "SUM(CASE when m.operator_code='CUCC' THEN 1 ELSE 0 END) as cuccNumber,SUM(CASE when m.operator_code='CTCC' THEN 1 ELSE 0 END) as ctccNumber, "
				+ "SUM(CASE when m.state=3 THEN 1 ELSE 0 END) as successNumber, SUM(CASE when m.state=3 and m.operator_code='CMCC' THEN 1 ELSE 0 END) as cmccSuccessNumber,  "
				+ "SUM(CASE when m.state=3 and m.operator_code='CUCC' THEN 1 ELSE 0 END) as cuccSuccessNumber,  "
				+ "SUM(CASE when m.state=3 and m.operator_code='CTCC' THEN 1 ELSE 0 END) as ctccSuccessNumber,  SUM(CASE when m.state=4 THEN 1 ELSE 0 END) as failNumber,  "
				+ "SUM(CASE when m.state=4 and m.operator_code='CMCC' THEN 1 ELSE 0 END) as cmccFailNumber, SUM(CASE when m.state=4 and m.operator_code='CUCC' THEN 1 ELSE 0 END) as cuccFailNumber,  "
				+ "SUM(CASE when m.state=4 and m.operator_code='CTCC' THEN 1 ELSE 0 END) as ctccFailNumber, SUM(CASE when m.state=5 THEN 1 ELSE 0 END) as timeoutNumber,  "
				+ "SUM(CASE when m.state=5 and m.operator_code='CMCC' THEN 1 ELSE 0 END) as cmccTimeoutNumber,  "
				+ "SUM(CASE when m.state=5 and m.operator_code='CUCC' THEN 1 ELSE 0 END) as cuccTimeoutNumber,  "
				+ "SUM(CASE when m.state=5 and m.operator_code='CTCC' THEN 1 ELSE 0 END) as ctccTimeoutNumber from fms_message m  where m.submit_time >=? and m.submit_time <=? GROUP BY m.content_type_id,m.enterprise_id ";
		/*
		 * map.put("startTime", startTime); map.put("endTime", endTime);
		 */
		list.add(startTime);
		list.add(endTime);
		// List<?> pageListResultBySqlByHibernate = this.getPageListResultBySqlByHibernate(sql, start, limit, map);
		return this.findSqlForListForMysql(FmsEnterpriseContentTypeDay.class, sql, list, currentPage, pageSize);
	}
}
