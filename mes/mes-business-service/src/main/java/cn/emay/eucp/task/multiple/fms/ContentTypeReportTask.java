package cn.emay.eucp.task.multiple.fms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeDay;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeMonth;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeYear;
import cn.emay.eucp.data.service.fms.FmsEnterpriseContentTypeDayService;
import cn.emay.eucp.data.service.fms.FmsEnterpriseContentTypeMonthService;
import cn.emay.eucp.data.service.fms.FmsEnterpriseContentTypeYearService;
import cn.emay.eucp.data.service.fms.FmsMessageService;
import cn.emay.eucp.task.multiple.constant.CommonConstanct;
import cn.emay.eucp.task.multiple.util.DateUtil;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.CronTask;

public class ContentTypeReportTask implements CronTask {
	private Logger logger = Logger.getLogger(ContentTypeReportTask.class);
	private static FmsMessageService fmsMessageService = BeanFactoryUtils.getBean(FmsMessageService.class);
	private static FmsEnterpriseContentTypeDayService fmsEnterpriseContentTypeDayService = BeanFactoryUtils.getBean(FmsEnterpriseContentTypeDayService.class);
	private static FmsEnterpriseContentTypeMonthService fmsEnterpriseContentTypeMonthService = BeanFactoryUtils.getBean(FmsEnterpriseContentTypeMonthService.class);
	private static FmsEnterpriseContentTypeYearService fmsEnterpriseContentTypeYearService = BeanFactoryUtils.getBean(FmsEnterpriseContentTypeYearService.class);

	@Override
	public TaskResult exec(Map<String, String> arg0) {
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			long beginTime = System.currentTimeMillis();
			Date date = new Date();
			int count = CommonConstanct.FMS_REPORT_TIME;
			int currentPage = 1;
			int pageSize = 10000;
			for (int i = count; i >= 1; i--) {
				Date startParseDate = DateUtil.getOtherDayStart(date, i * (-1));
				Date reportday = DateUtil.getCalculateDay(date, i * (-1));
				String day = simpleDateFormat.format(startParseDate);
				fmsEnterpriseContentTypeDayService.deleteByTime(day);
				for (;;) {
					List<FmsEnterpriseContentTypeDay> list = fmsMessageService.findContentTypeReport(day, reportday, currentPage, pageSize);
					List<FmsEnterpriseContentTypeDay> insertList = new ArrayList<FmsEnterpriseContentTypeDay>();
					if (null != list && list.size() > 0) {
						for (FmsEnterpriseContentTypeDay fmsEnterpriseContentTypeDay : list) {
							insertList.add(fmsEnterpriseContentTypeDay);
							if (insertList.size() == 1000) {
								fmsEnterpriseContentTypeDayService.saveList(insertList);
								insertList.clear();
							}
						}
						if (insertList.size() > 0) {
							fmsEnterpriseContentTypeDayService.saveList(insertList);
							insertList.clear();
						}
					} else {
						break;
					}
					currentPage++;
				}

				logger.info("【ContentTypeReportTask】--" + DateUtil.toString(date, "yyyy-MM-dd") + "日企业内容类型发送执行成功，消耗时间为：[" + (System.currentTimeMillis() - beginTime) + "ms]");
				String lastmonth = null;
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
				SimpleDateFormat monthnum = new SimpleDateFormat("dd");
				String currentmonth = monthnum.format(startParseDate);
				int currentMonth = Integer.valueOf(currentmonth);
				String format = formatter.format(date);
				currentPage = 1;
				if (currentMonth <= count) {
					Date lastMonthFirstDay = DateUtil.getLastMonthFirstDay();
					lastmonth = formatter.format(lastMonthFirstDay);
					fmsEnterpriseContentTypeMonthService.deleteMonth(lastmonth);
					for (;;) {
						List<FmsEnterpriseContentTypeMonth> reportMonth = fmsEnterpriseContentTypeDayService.findMonth(DateUtil.getLastMonthFirstDay(), DateUtil.getMonthLastDay(startParseDate),
								currentPage, pageSize);
						if (null != reportMonth && !reportMonth.isEmpty()) {
							List<FmsEnterpriseContentTypeMonth> insertList = new ArrayList<FmsEnterpriseContentTypeMonth>();
							for (FmsEnterpriseContentTypeMonth insert : reportMonth) {
								Date parseDate = DateUtil.parseDate(insert.getReportTimeStr() + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
								insert.setReportTime(parseDate);
								insertList.add(insert);
								if (insertList.size() == 1000) {
									fmsEnterpriseContentTypeMonthService.saveList(insertList);
									insertList.clear();
								}
							}
							if (insertList.size() > 0) {
								fmsEnterpriseContentTypeMonthService.saveList(insertList);
								insertList.clear();
							}
						} else {
							break;
						}
						currentPage++;
					}
				} else {
					fmsEnterpriseContentTypeMonthService.deleteMonth(format);
					for (;;) {
						List<FmsEnterpriseContentTypeMonth> report = fmsEnterpriseContentTypeDayService.findMonth(DateUtil.getMonthFirstDay(startParseDate), DateUtil.getMonthLastDay(startParseDate),
								currentPage, pageSize);
						if (null != report && !report.isEmpty()) {
							List<FmsEnterpriseContentTypeMonth> insertList = new ArrayList<FmsEnterpriseContentTypeMonth>();
							for (FmsEnterpriseContentTypeMonth insert : report) {
								Date parseDate = DateUtil.parseDate(insert.getReportTimeStr() + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
								insert.setReportTime(parseDate);
								insertList.add(insert);
								if (insertList.size() == 1000) {
									fmsEnterpriseContentTypeMonthService.saveList(insertList);
									insertList.clear();
								}
							}
							if (insertList.size() > 0) {
								fmsEnterpriseContentTypeMonthService.saveList(insertList);
								insertList.clear();
							}
						} else {
							break;
						}
						currentPage++;
					}
				}

				logger.info("【ContentTypeReportTask】--" + DateUtil.toString(date, "yyyy-MM-dd") + "月企业内容类型发送执行成功，消耗时间为：[" + (System.currentTimeMillis() - beginTime) + "ms]");

				SimpleDateFormat formatteryear = new SimpleDateFormat("yyyy");
				long beginyearTime = System.currentTimeMillis();
				String year = formatteryear.format(date);
				fmsEnterpriseContentTypeYearService.deleteYear(year);
				currentPage = 1;
				for (;;) {
					List<FmsEnterpriseContentTypeYear> list = fmsEnterpriseContentTypeMonthService.findYear(year, currentPage, pageSize);
					if (null != list && !list.isEmpty()) {
						List<FmsEnterpriseContentTypeYear> insertList = new ArrayList<FmsEnterpriseContentTypeYear>();
						for (FmsEnterpriseContentTypeYear insert : list) {
							Date parseDate = DateUtil.parseDate(insert.getReportTimeStr() + "-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
							insert.setReportTime(parseDate);
							insertList.add(insert);
							if (insertList.size() == 1000) {
								fmsEnterpriseContentTypeYearService.saveList(insertList);
							}
						}
						if (insertList.size() > 0) {
							fmsEnterpriseContentTypeYearService.saveList(insertList);
							insertList.clear();
						}
					} else {
						break;
					}
					currentPage++;
				}

				logger.info("【ContentTypeReportTask】--" + DateUtil.toString(date, "yyyy-MM-dd") + "年企业内容类型发送执行成功，消耗时间为：[" + (System.currentTimeMillis() - beginyearTime) + "ms]");
			}
			return TaskResult.doBusinessSuccessResult();
		} catch (Exception e) {
			logger.error("【ContentTypeReportTask】--", e);
			return TaskResult.doBusinessFailResult();
		}

	}

}
