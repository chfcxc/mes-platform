package cn.emay.eucp.data.service.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.statices.FmsServicecodeConsumptionReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionDay;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionMonth;
import cn.emay.eucp.data.dao.fms.FmsServicecodeConsumptionDayDao;
import cn.emay.eucp.data.dao.fms.FmsServicecodeConsumptionMonthDao;
import cn.emay.eucp.data.dao.fms.FmsServicecodeConsumptionYearDao;
import cn.emay.eucp.data.service.fms.FmsServicecodeConsumptionDayService;
import cn.emay.util.DateUtil;

@Service("fmsServicecodeConsumptionDayService")
public class FmsServicecodeConsumptionDayServiceImpl implements FmsServicecodeConsumptionDayService {
	@Resource
	private FmsServicecodeConsumptionDayDao fmsServicecodeConsumptionDayDao;
	@Resource
	private FmsServicecodeConsumptionMonthDao fmsServicecodeConsumptionMonthDao;
	@Resource
	private FmsServicecodeConsumptionYearDao fmsServicecodeConsumptionYearDao;

	@Override
	public void deleteByTime(String day) {
		fmsServicecodeConsumptionDayDao.deleteByTime(day);
	}

	@Override
	public void saveList(List<FmsServicecodeConsumptionDay> list) {
		fmsServicecodeConsumptionDayDao.saveList(list);
	}

	@Override
	public List<FmsServicecodeConsumptionMonth> findReportMonth(Date lastMonthFirstDay, Date monthLastDay, int currentPage, int pageSize) {
		return fmsServicecodeConsumptionDayDao.findReportMonth(lastMonthFirstDay, monthLastDay, currentPage, pageSize);
	}

	@Override
	public Page<FmsServicecodeConsumptionReportDto> findPage(String rate, String enterpriseId, Long serviceCodeId, Date startTime, Date endTime, int start, int limit) {
		Page<FmsServicecodeConsumptionReportDto> page = null;
		if ("DAY".equalsIgnoreCase(rate)) {
			if (null != startTime) {
				startTime = DateUtil.parseDate(DateUtil.toString(startTime, "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			}
			if (null != endTime) {
				endTime = DateUtil.parseDate(DateUtil.toString(endTime, "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			}
			page = fmsServicecodeConsumptionDayDao.findPage(enterpriseId, serviceCodeId, startTime, endTime, start, limit);
		} else if ("MONTH".equalsIgnoreCase(rate)) {
			if (null != startTime) {
				startTime = DateUtil.parseDate(DateUtil.toString(startTime, "yyyy-MM") + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			}
			if (null != endTime) {
				endTime = DateUtil.parseDate(DateUtil.toString(DateUtil.getTheMonthLastDay(endTime), "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			}
			page = fmsServicecodeConsumptionMonthDao.findPage(enterpriseId, serviceCodeId, startTime, endTime, start, limit);
		} else {
			if (null != startTime) {
				startTime = DateUtil.parseDate(DateUtil.toString(startTime, "yyyy") + "-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			}
			if (null != endTime) {
				endTime = DateUtil.parseDate(DateUtil.toString(endTime, "yyyy") + "-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");
			}
			page = fmsServicecodeConsumptionYearDao.findPage(enterpriseId, serviceCodeId, startTime, endTime, start, limit);
		}
		if (page != null) {
			List<FmsServicecodeConsumptionReportDto> formatDateList = formatDate(rate, page.getList());
			page.setList(formatDateList);
		}
		return page;
	}

	private List<FmsServicecodeConsumptionReportDto> formatDate(String rate, List<FmsServicecodeConsumptionReportDto> dtos) {
		List<FmsServicecodeConsumptionReportDto> resultDto = new ArrayList<FmsServicecodeConsumptionReportDto>();
		for (FmsServicecodeConsumptionReportDto dto : dtos) {
			if ("MONTH".equalsIgnoreCase(rate)) {
				dto.setReportTimeStr(DateUtil.toString(dto.getReportTime(), "yyyy-MM"));
			} else if ("YEAR".equalsIgnoreCase(rate)) {
				dto.setReportTimeStr(DateUtil.toString(dto.getReportTime(), "yyyy"));
			} else {
				dto.setReportTimeStr(DateUtil.toString(dto.getReportTime(), "yyyy-MM-dd"));
			}
			resultDto.add(dto);
		}
		return resultDto;
	}

}
