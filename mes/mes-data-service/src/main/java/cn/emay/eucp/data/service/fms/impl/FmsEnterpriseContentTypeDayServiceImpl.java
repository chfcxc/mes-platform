package cn.emay.eucp.data.service.fms.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.emay.common.db.Page;
import cn.emay.eucp.common.dto.fms.statices.FmsEnterpriseContentTypeReportDto;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeDay;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeMonth;
import cn.emay.eucp.data.dao.fms.FmsEnterpriseContentTypeDayDao;
import cn.emay.eucp.data.dao.fms.FmsEnterpriseContentTypeMonthDao;
import cn.emay.eucp.data.dao.fms.FmsEnterpriseContentTypeYearDao;
import cn.emay.eucp.data.service.fms.FmsEnterpriseContentTypeDayService;
import cn.emay.util.DateUtil;

@Service("fmsEnterpriseContentTypeDayService")
public class FmsEnterpriseContentTypeDayServiceImpl implements FmsEnterpriseContentTypeDayService {
	@Resource
	private FmsEnterpriseContentTypeDayDao fmsEnterpriseContentTypeDayDao;
	@Resource
	private FmsEnterpriseContentTypeMonthDao fmsEnterpriseContentTypeMonthDao;
	@Resource
	private FmsEnterpriseContentTypeYearDao fmsEnterpriseContentTypeYearDao;

	@Override
	public void deleteByTime(String day) {
		fmsEnterpriseContentTypeDayDao.deleteByTime(day);
	}

	@Override
	public void saveList(List<FmsEnterpriseContentTypeDay> list) {
		fmsEnterpriseContentTypeDayDao.saveList(list);
	}

	@Override
	public List<FmsEnterpriseContentTypeMonth> findMonth(Date startTime, Date endTime, int currentPage, int pageSize) {
		return fmsEnterpriseContentTypeDayDao.findMonth(startTime, endTime, currentPage, pageSize);
	}

	@Override
	public Page<FmsEnterpriseContentTypeReportDto> findPage(String rate, Long businessTypeId, Long serviceCodeId, Date startTime, Date endTime, int start, int limit, String enterpriseIds) {
		Page<FmsEnterpriseContentTypeReportDto> page = null;
		if ("DAY".equalsIgnoreCase(rate)) {
			if (null != startTime) {
				startTime = DateUtil.parseDate(DateUtil.toString(startTime, "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			}
			if (null != endTime) {
				endTime = DateUtil.parseDate(DateUtil.toString(endTime, "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			}
			page = fmsEnterpriseContentTypeDayDao.findPage(businessTypeId, serviceCodeId, startTime, endTime, start, limit, enterpriseIds);
		} else if ("MONTH".equalsIgnoreCase(rate)) {
			if (null != startTime) {
				startTime = DateUtil.parseDate(DateUtil.toString(startTime, "yyyy-MM") + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			}
			if (null != endTime) {
				endTime = DateUtil.parseDate(DateUtil.toString(DateUtil.getTheMonthLastDay(endTime), "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			}
			page = fmsEnterpriseContentTypeMonthDao.findPage(businessTypeId, serviceCodeId, startTime, endTime, start, limit, enterpriseIds);
		} else {
			if (null != startTime) {
				startTime = DateUtil.parseDate(DateUtil.toString(startTime, "yyyy") + "-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
			}
			if (null != endTime) {
				endTime = DateUtil.parseDate(DateUtil.toString(endTime, "yyyy") + "-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss");
			}
			page = fmsEnterpriseContentTypeYearDao.findPage(businessTypeId, serviceCodeId, startTime, endTime, start, limit, enterpriseIds);
		}
		if (page != null) {
			List<FmsEnterpriseContentTypeReportDto> formatDateList = formatDate(rate, page.getList());
			page.setList(formatDateList);
		}
		return page;
	}

	private List<FmsEnterpriseContentTypeReportDto> formatDate(String rate, List<FmsEnterpriseContentTypeReportDto> dtos) {
		List<FmsEnterpriseContentTypeReportDto> resultDto = new ArrayList<FmsEnterpriseContentTypeReportDto>();
		for (FmsEnterpriseContentTypeReportDto dto : dtos) {
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
