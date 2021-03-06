package cn.emay.eucp.data.dao.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.statices.FmsEnterpriseContentTypeReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeYear;
import cn.emay.eucp.data.dao.common.PojoDaoImpl;
import cn.emay.eucp.data.dao.fms.FmsEnterpriseContentTypeYearDao;
import cn.emay.util.DateUtil;

@Repository
public class FmsEnterpriseContentTypeYearDaoImpl extends PojoDaoImpl<FmsEnterpriseContentTypeYear> implements FmsEnterpriseContentTypeYearDao {

	@Override
	public void deleteYear(String year) {
		Date d = DateUtil.parseDate(year + "-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		Date d2 = DateUtil.parseDate(year + "-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");
		String sql = "DELETE FROM fms_enterprise_content_type_year  where report_time >= '" + DateUtil.toString(d, "yyyy-MM-dd HH:mm:ss") + "' and report_time <= '"
				+ DateUtil.toString(d2, "yyyy-MM-dd HH:mm:ss") + "'";
		this.execSql(sql);
	}

	@Override
	public void saveList(List<FmsEnterpriseContentTypeYear> list) {
		List<Object[]> list1 = new ArrayList<Object[]>();
		for (FmsEnterpriseContentTypeYear day : list) {
			list1.add(new Object[] { day.getBusinessTypeId(), day.getEnterpriseId(), day.getTotalNumber(), day.getCmccNumber(), day.getCuccNumber(), day.getCtccNumber(), day.getSuccessNumber(),
					day.getCmccSuccessNumber(), day.getCuccSuccessNumber(), day.getCtccSuccessNumber(), day.getFailNumber(), day.getCmccFailNumber(), day.getCuccFailNumber(), day.getCtccFailNumber(),
					day.getTimeoutNumber(), day.getCmccTimeoutNumber(), day.getCuccTimeoutNumber(), day.getCtccTimeoutNumber(), day.getReportTime(), new Date() });
		}
		String sql = "";
		if (list1.size() > 0) {
			sql = "INSERT INTO fms_enterprise_content_type_year (business_type_id,enterprise_id,total_number,cmcc_number,cucc_number,ctcc_number,success_number,"
					+ " cmcc_success_number,cucc_success_number,ctcc_success_number,fail_number,cmcc_fail_number,cucc_fail_number,ctcc_fail_number,timeout_number,"
					+ " cmcc_timeout_number,cucc_timeout_number,ctcc_timeout_number,report_time,create_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
			this.getJdbcTemplate().batchUpdate(sql, list1);
		}

	}

	@Override
	public Page<FmsEnterpriseContentTypeReportDto> findPage(Long businessTypeId, Long serviceCodeId, Date startTime, Date endTime, int start, int limit, String enterpriseIds) {
		String sql = " select * from fms_enterprise_content_type_year where 1=1 ";
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
