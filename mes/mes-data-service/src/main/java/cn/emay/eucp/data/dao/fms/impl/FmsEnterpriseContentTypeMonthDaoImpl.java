package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.statices.FmsEnterpriseContentTypeReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeMonth;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeYear;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsEnterpriseContentTypeMonthDao;
import cn.emay.util.DateUtil;

@Repository
public class FmsEnterpriseContentTypeMonthDaoImpl extends PojoDaoImpl<FmsEnterpriseContentTypeMonth> implements FmsEnterpriseContentTypeMonthDao {

	@Override
	public void deleteMonth(String month) {
		Date date = new Date();
		String string = DateUtil.toString(date, "yyyy-MM");
		Date start = DateUtil.parseDate(month, "yyyy-MM");
		String sql = "";
		if (!month.equals(string)) {
			Date lastDay = DateUtil.getNowMonthLastDay();
			sql = "DELETE FROM fms_enterprise_content_type_month  where report_time >= '" + DateUtil.toString(start, "yyyy-MM-dd HH:mm:ss") + "' and report_time <= '"
					+ DateUtil.toString(lastDay, "yyyy-MM-dd HH:mm:ss") + "'";
		} else {
			String s = DateUtil.toString(DateUtil.getTheMonthLastDay(start), "yyyy-MM-dd");
			Date end = DateUtil.parseDate(s + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql = "DELETE FROM fms_enterprise_content_type_month  where report_time >= '" + DateUtil.toString(start, "yyyy-MM-dd HH:mm:ss") + "' and report_time <= '"
					+ DateUtil.toString(end, "yyyy-MM-dd HH:mm:ss") + "'";
		}
		this.execSql(sql);
	}

	@Override
	public void saveList(List<FmsEnterpriseContentTypeMonth> list) {
		List<Object[]> list1 = new ArrayList<Object[]>();
		for (FmsEnterpriseContentTypeMonth day : list) {
			list1.add(new Object[] { day.getBusinessTypeId(), day.getEnterpriseId(), day.getTotalNumber(), day.getCmccNumber(), day.getCuccNumber(), day.getCtccNumber(), day.getSuccessNumber(),
					day.getCmccSuccessNumber(), day.getCuccSuccessNumber(), day.getCtccSuccessNumber(), day.getFailNumber(), day.getCmccFailNumber(), day.getCuccFailNumber(), day.getCtccFailNumber(),
					day.getTimeoutNumber(), day.getCmccTimeoutNumber(), day.getCuccTimeoutNumber(), day.getCtccTimeoutNumber(), day.getReportTime(), new Date() });
		}
		String sql = "";
		if (list1.size() > 0) {
			sql = "INSERT INTO fms_enterprise_content_type_month (business_type_id,enterprise_id,total_number,cmcc_number,cucc_number,ctcc_number,success_number,"
					+ " cmcc_success_number,cucc_success_number,ctcc_success_number,fail_number,cmcc_fail_number,cucc_fail_number,ctcc_fail_number,timeout_number,"
					+ " cmcc_timeout_number,cucc_timeout_number,ctcc_timeout_number,report_time,create_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			this.getJdbcTemplate().batchUpdate(sql, list1);
		}

	}

	@Override
	public List<FmsEnterpriseContentTypeYear> findYear(String year, int currentPage, int pageSize) {
		// Date startTime = DateUtil.parseDate(year + "-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		// Date endTime = DateUtil.parseDate(year + "-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");
		// Map<String, Object> map = new HashMap<String, Object>();
		String startTime = year + "-01-01 00:00:00";
		String endTime = year + "-12-31 23:59:59";
		List<Object> list = new ArrayList<Object>();
		String sql = " select business_type_id,enterprise_id,sum(total_number) as totalNumber,sum(cmcc_number) as cmccNumber,sum(cucc_number) as cuccNumber,sum(ctcc_number) as ctccNumber,sum(success_number) as successNumber,"
				+ " sum(cmcc_success_number) as cmccSuccessNumber,sum(cucc_success_number) as cuccSuccessNumber ,sum(ctcc_success_number) as ctccSuccessNumber,sum(fail_number) as failNumber,sum(cmcc_fail_number) as cmccFailNumber,"
				+ "sum(cucc_fail_number) as cuccFailNumber,sum(ctcc_fail_number) as ctccFailNumber,sum(timeout_number) as timeoutNumber, sum(cmcc_timeout_number) as cmccTimeoutNumber,sum(cucc_timeout_number) as cuccTimeoutNumber,"
				+ "sum(ctcc_timeout_number) as ctccTimeoutNumber,DATE_FORMAT(report_time,'%Y') as reportTimeStr  from fms_enterprise_content_type_month where report_time >=? and report_time <=? GROUP BY business_type_id,enterprise_id,reportTimeStr ";
		list.add(startTime);
		list.add(endTime);
		// List<?> pageListResultBySqlByHibernate = this.getPageListResultBySqlByHibernate(sql, start, limit, map);
		return this.findSqlForListForMysql(FmsEnterpriseContentTypeYear.class, sql, list, currentPage, pageSize);
	}

	@Override
	public Page<FmsEnterpriseContentTypeReportDto> findPage(Long businessTypeId, Long serviceCodeId, Date startTime, Date endTime, int start, int limit, String enterpriseIds) {
		String sql = " select * from fms_enterprise_content_type_month where 1=1 ";
		List<Object> list = new ArrayList<Object>();
		if (serviceCodeId != 0L) {
			sql += " and servicecode_id =? ";
			list.add(serviceCodeId);
		}
		if (!StringUtils.isEmpty(enterpriseIds)) {
			sql += " and enterprise_id in ('" + enterpriseIds + "')";
		}
		if (businessTypeId != 0L) {
			sql += " and business_type_id =? ";
			list.add(businessTypeId);
		}
		if (null != startTime) {
			sql += " and report_time >=? ";
			list.add(startTime);
		}
		if (null != endTime) {
			sql += " and report_time <=? ";
			list.add(endTime);
		}
		return this.findSqlForPageForMysql(FmsEnterpriseContentTypeReportDto.class, sql, list, start, limit);
	}

}
