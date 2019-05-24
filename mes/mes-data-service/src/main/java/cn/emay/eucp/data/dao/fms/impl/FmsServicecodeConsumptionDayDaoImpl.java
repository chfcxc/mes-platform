package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.statices.FmsServicecodeConsumptionReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionDay;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionMonth;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsServicecodeConsumptionDayDao;
import cn.emay.util.DateUtil;

@Repository
public class FmsServicecodeConsumptionDayDaoImpl extends PojoDaoImpl<FmsServicecodeConsumptionDay> implements FmsServicecodeConsumptionDayDao {

	@Override
	public void deleteByTime(String day) {
		String sql = " delete from fms_servicecode_consumption_day where report_time >=? and report_time<=?";
		List<Object> list = new ArrayList<Object>();
		list.add(day + " 00:00:00");
		list.add(day + " 23:59:59");
		this.getJdbcTemplate().update(sql, list.toArray());
	}

	@Override
	public void saveList(List<FmsServicecodeConsumptionDay> list) {
		List<Object[]> list1 = new ArrayList<Object[]>();
		for (FmsServicecodeConsumptionDay day : list) {
			list1.add(new Object[] { day.getServicecodeId(), day.getEnterpriseId(), day.getAppId(), day.getTotalNumber(), day.getCmccNumber(), day.getCuccNumber(), day.getCtccNumber(),
					day.getSuccessNumber(), day.getCmccSuccessNumber(), day.getCuccSuccessNumber(), day.getCtccSuccessNumber(), day.getFailNumber(), day.getCmccFailNumber(), day.getCuccFailNumber(),
					day.getCtccFailNumber(), day.getTimeoutNumber(), day.getCmccTimeoutNumber(), day.getCuccTimeoutNumber(), day.getCtccTimeoutNumber(), day.getReportTime() });
		}
		String sql = "";
		if (list1.size() > 0) {
			sql = "INSERT INTO fms_servicecode_consumption_day (servicecode_id,enterprise_id,app_id,total_number,cmcc_number,cucc_number,ctcc_number,success_number,"
					+ " cmcc_success_number,cucc_success_number,ctcc_success_number,fail_number,cmcc_fail_number,cucc_fail_number,ctcc_fail_number,timeout_number,"
					+ " cmcc_timeout_number,cucc_timeout_number,ctcc_timeout_number,report_time,create_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW()) ";
			this.getJdbcTemplate().batchUpdate(sql, list1);
		}

	}

	@Override
	public List<FmsServicecodeConsumptionMonth> findReportMonth(Date startTime, Date endTime, int currentPage, int pageSize) {
		String startDate = DateUtil.toString(startTime, "yyyy-MM-dd HH:mm:ss");
		String endDate = DateUtil.toString(endTime, "yyyy-MM-dd HH:mm:ss");
		List<Object> list = new ArrayList<Object>();
		// Map<String, Object> map = new HashMap<String, Object>();
		String sql = " select servicecode_id,enterprise_id,app_id,sum(total_number) as totalNumber,sum(cmcc_number) as cmccNumber,sum(cucc_number) as cuccNumber,sum(ctcc_number) as ctccNumber,sum(success_number) as successNumber,"
				+ " sum(cmcc_success_number) as cmccSuccessNumber,sum(cucc_success_number) as cuccSuccessNumber ,sum(ctcc_success_number) as ctccSuccessNumber,sum(fail_number) as failNumber,sum(cmcc_fail_number) as cmccFailNumber,"
				+ "sum(cucc_fail_number) as cuccFailNumber,sum(ctcc_fail_number) as ctccFailNumber,sum(timeout_number) as timeoutNumber, sum(cmcc_timeout_number) as cmccTimeoutNumber,sum(cucc_timeout_number) as cuccTimeoutNumber,"
				+ "sum(ctcc_timeout_number) as ctccTimeoutNumber,DATE_FORMAT(report_time,'%Y-%m') as reportTimeStr from fms_servicecode_consumption_day where report_time >=? and report_time <=? GROUP BY servicecode_id ,enterprise_id,app_id,reportTimeStr ";
		// map.put("startTime", startTime);
		// map.put("endTime", endTime);
		list.add(startDate);
		list.add(endDate);

		// List<?> pageListResultBySqlByHibernate = this.getPageListResultBySqlByHibernate(sql, start, limit, map);
		return this.findSqlForListForMysql(FmsServicecodeConsumptionMonth.class, sql, list, currentPage, pageSize);
	}

	@Override
	public Page<FmsServicecodeConsumptionReportDto> findPage(String enterpriseId, Long serviceCodeId, Date startTime, Date endTime, int start, int limit) {
		String sql = " select * from fms_servicecode_consumption_day where 1=1 ";
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
