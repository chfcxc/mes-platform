package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.statices.FmsServicecodeConsumptionReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionMonth;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionYear;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsServicecodeConsumptionMonthDao;
import cn.emay.util.DateUtil;

@Repository
public class FmsServicecodeConsumptionMonthDaoImpl extends PojoDaoImpl<FmsServicecodeConsumptionMonth> implements FmsServicecodeConsumptionMonthDao {

	@Override
	public Long countByServiceCode(List<Long> serviceCodeId, Date startTime, Date endTime) {
		StringBuffer sqlbuilder = new StringBuffer("select count(id) from fms_servicecode_consumption_month where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if (null != serviceCodeId && serviceCodeId.size() > 0) {
			sqlbuilder.append("and servicecode_id in ( ");
			for (int i = 0; i < serviceCodeId.size(); i++) {
				if (i == serviceCodeId.size() - 1) {
					sqlbuilder.append("?");
				} else {
					sqlbuilder.append("?,");
				}
				params.add(serviceCodeId.get(i));
			}
			sqlbuilder.append(")");
		}
		if (null != startTime) {
			String starttime = DateUtil.toString(startTime, "yyyy-MM-dd HH:mm:ss");
			sqlbuilder.append(" and report_time >= ?");
			params.add(starttime);
		}
		if (null != endTime) {
			String endtime = DateUtil.toString(endTime, "yyyy-MM-dd HH:mm:ss");
			sqlbuilder.append(" and report_time <= ?");
			params.add(endtime);
		}
		return this.jdbcTemplate.queryForObject(sqlbuilder.toString(), params.toArray(), Long.class);
	}

	@Override
	public void deleteMonth(String month) {
		Date date = new Date();
		String string = DateUtil.toString(date, "yyyy-MM");
		Date start = DateUtil.parseDate(month, "yyyy-MM");
		String sql = "";
		if (!month.equals(string)) {
			Date lastDay = DateUtil.getNowMonthLastDay();
			sql = "DELETE FROM fms_servicecode_consumption_month  where report_time >= '" + DateUtil.toString(start, "yyyy-MM-dd HH:mm:ss") + "' and report_time <= '"
					+ DateUtil.toString(lastDay, "yyyy-MM-dd HH:mm:ss") + "'";
		} else {
			String s = DateUtil.toString(DateUtil.getTheMonthLastDay(start), "yyyy-MM-dd");
			Date end = DateUtil.parseDate(s + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql = "DELETE FROM fms_servicecode_consumption_month  where report_time >= '" + DateUtil.toString(start, "yyyy-MM-dd HH:mm:ss") + "' and report_time <= '"
					+ DateUtil.toString(end, "yyyy-MM-dd HH:mm:ss") + "'";
		}
		this.execSql(sql);

	}

	@Override
	public void batchSave(List<FmsServicecodeConsumptionMonth> insertList) {
		List<Object[]> list1 = new ArrayList<Object[]>();
		for (FmsServicecodeConsumptionMonth day : insertList) {
			list1.add(new Object[] { day.getServicecodeId(), day.getEnterpriseId(), day.getAppId(), day.getTotalNumber(), day.getCmccNumber(), day.getCuccNumber(), day.getCtccNumber(),
					day.getSuccessNumber(), day.getCmccSuccessNumber(), day.getCuccSuccessNumber(), day.getCtccSuccessNumber(), day.getFailNumber(), day.getCmccFailNumber(), day.getCuccFailNumber(),
					day.getCtccFailNumber(), day.getTimeoutNumber(), day.getCmccTimeoutNumber(), day.getCuccTimeoutNumber(), day.getCtccTimeoutNumber(), day.getReportTime(), new Date() });
		}
		String sql = "";
		if (list1.size() > 0) {
			sql = "INSERT INTO fms_servicecode_consumption_month (servicecode_id,enterprise_id,app_id,total_number,cmcc_number,cucc_number,ctcc_number,success_number,"
					+ " cmcc_success_number,cucc_success_number,ctcc_success_number,fail_number,cmcc_fail_number,cucc_fail_number,ctcc_fail_number,timeout_number,"
					+ " cmcc_timeout_number,cucc_timeout_number,ctcc_timeout_number,report_time,create_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			this.getJdbcTemplate().batchUpdate(sql, list1);
		}

	}

	@Override
	public List<FmsServicecodeConsumptionYear> findReportYear(String year, int currentPage, int pageSize) {
		String startTime = year + "-01-01 00:00:00";
		String endTime = year + "-12-31 23:59:59";
		List<Object> list = new ArrayList<Object>();
		String sql = " select servicecode_id,enterprise_id,app_id,sum(total_number) as totalNumber,sum(cmcc_number) as cmccNumber,sum(cucc_number) as cuccNumber,sum(ctcc_number) as ctccNumber,sum(success_number) as successNumber,"
				+ " sum(cmcc_success_number) as cmccSuccessNumber,sum(cucc_success_number) as cuccSuccessNumber ,sum(ctcc_success_number) as ctccSuccessNumber,sum(fail_number) as failNumber,sum(cmcc_fail_number) as cmccFailNumber,"
				+ "sum(cucc_fail_number) as cuccFailNumber,sum(ctcc_fail_number) as ctccFailNumber,sum(timeout_number) as timeoutNumber, sum(cmcc_timeout_number) as cmccTimeoutNumber,sum(cucc_timeout_number) as cuccTimeoutNumber,"
				+ "sum(ctcc_timeout_number) as ctccTimeoutNumber,DATE_FORMAT(report_time,'%Y') as reportTimeStr from fms_servicecode_consumption_month where report_time >=? and report_time <=? GROUP BY servicecode_id ,enterprise_id,app_id,reportTimeStr ";
		list.add(startTime);
		list.add(endTime);
		return this.findSqlForListForMysql(FmsServicecodeConsumptionYear.class, sql, list, currentPage, pageSize);
	}

	@Override
	public Page<FmsServicecodeConsumptionReportDto> findPage(String enterpriseId, Long serviceCodeId, Date startTime, Date endTime, int start, int limit) {
		String sql = " select * from fms_servicecode_consumption_month where 1=1 ";
		List<Object> list = new ArrayList<Object>();
		if (!StringUtils.isEmpty(enterpriseId)) {
			sql += " and enterprise_id in ('" + enterpriseId + "')";
		}
		if (serviceCodeId != 0L) {
			sql += " and servicecode_id =? ";
			list.add(serviceCodeId);
		}
		if (null != startTime) {
			sql += " and report_time >=? ";
			list.add(startTime);
		}
		if (null != endTime) {
			sql += " and report_time <=? ";
			list.add(endTime);
		}
		return this.findSqlForPageForMysql(FmsServicecodeConsumptionReportDto.class, sql, list, start, limit);
	}

}
