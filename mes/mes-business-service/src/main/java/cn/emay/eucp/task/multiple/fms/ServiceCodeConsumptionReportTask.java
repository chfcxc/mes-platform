package cn.emay.eucp.task.multiple.fms;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionDay;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionMonth;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionYear;
import cn.emay.eucp.data.service.fms.FmsMessageService;
import cn.emay.eucp.data.service.fms.FmsServicecodeConsumptionDayService;
import cn.emay.eucp.data.service.fms.FmsServicecodeConsumptionMonthService;
import cn.emay.eucp.data.service.fms.FmsServicecodeConsumptionYearService;
import cn.emay.eucp.task.multiple.constant.CommonConstanct;
import cn.emay.eucp.task.multiple.util.DateUtil;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.CronTask;

public class ServiceCodeConsumptionReportTask implements CronTask {
	private Logger logger = Logger.getLogger(ServiceCodeConsumptionReportTask.class);
	private static FmsMessageService fmsMessageService = BeanFactoryUtils.getBean(FmsMessageService.class);
	private static FmsServicecodeConsumptionDayService fmsServicecodeConsumptionDayService = BeanFactoryUtils.getBean(FmsServicecodeConsumptionDayService.class);
	private static FmsServicecodeConsumptionMonthService fmsServicecodeConsumptionMonthService = BeanFactoryUtils.getBean(FmsServicecodeConsumptionMonthService.class);
	private static FmsServicecodeConsumptionYearService fmsServicecodeConsumptionYearService = BeanFactoryUtils.getBean(FmsServicecodeConsumptionYearService.class);

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
				fmsServicecodeConsumptionDayService.deleteByTime(day);
				for (;;) {
					List<FmsServicecodeConsumptionDay> list = fmsMessageService.findConsumptionReport(day, reportday, currentPage, pageSize);
					List<FmsServicecodeConsumptionDay> insertList = new ArrayList<FmsServicecodeConsumptionDay>();
					if (null != list && list.size() > 0) {
						for (FmsServicecodeConsumptionDay fmsServicecodeConsumptionDay : list) {
							fmsServicecodeConsumptionDay.setReportTime(reportday);
							insertList.add(fmsServicecodeConsumptionDay);
							if (insertList.size() == 1000) {
								fmsServicecodeConsumptionDayService.saveList(insertList);
								insertList.clear();
							}
						}
						if (insertList.size() > 0) {
							fmsServicecodeConsumptionDayService.saveList(insertList);
							insertList.clear();
						}
					} else {
						break;
					}
					currentPage++;
				}

				logger.info("【ServiceCodeConsumptionReportTask】--" + DateUtil.toString(date, "yyyy-MM-dd") + "日服务号消费统计执行成功，消耗时间为：[" + (System.currentTimeMillis() - beginTime) + "ms]");

				long beginmonthTime = System.currentTimeMillis();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM");
				// 3号之前去本月以及上月
				String lastmonth = null;
				SimpleDateFormat monthnum = new SimpleDateFormat("dd");
				String currentmonth = monthnum.format(startParseDate);
				Integer num = Integer.valueOf(currentmonth);
				String format = formatter.format(date);
				currentPage = 1;
				if (num <= count) {
					Date lastMonthFirstDay = DateUtil.getLastMonthFirstDay();
					lastmonth = formatter.format(lastMonthFirstDay);
					fmsServicecodeConsumptionMonthService.deleteMonth(lastmonth);
					for (;;) {
						List<FmsServicecodeConsumptionMonth> reportMonth = fmsServicecodeConsumptionDayService.findReportMonth(DateUtil.getLastMonthFirstDay(),
								DateUtil.getMonthLastDay(startParseDate), currentPage, pageSize);
						if (null != reportMonth && !reportMonth.isEmpty()) {
							List<FmsServicecodeConsumptionMonth> insertList = new ArrayList<FmsServicecodeConsumptionMonth>();
							for (FmsServicecodeConsumptionMonth insert : reportMonth) {
								Date parseDate = DateUtil.parseDate(insert.getReportTimeStr() + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
								insert.setReportTime(parseDate);
								insertList.add(insert);
								if (insertList.size() == 1000) {
									fmsServicecodeConsumptionMonthService.batchSave(insertList);
									insertList.clear();
								}
							}
							if (insertList.size() > 0) {
								fmsServicecodeConsumptionMonthService.batchSave(insertList);
								insertList.clear();
							}
						} else {
							break;
						}
						currentPage++;
					}
				} else {
					fmsServicecodeConsumptionMonthService.deleteMonth(format);
					for (;;) {
						List<FmsServicecodeConsumptionMonth> report = fmsServicecodeConsumptionDayService.findReportMonth(DateUtil.getMonthFirstDay(startParseDate),
								DateUtil.getMonthLastDay(startParseDate), currentPage, pageSize);
						if (null != report && !report.isEmpty()) {
							List<FmsServicecodeConsumptionMonth> insertList = new ArrayList<FmsServicecodeConsumptionMonth>();
							for (FmsServicecodeConsumptionMonth insert : report) {
								Date parseDate = DateUtil.parseDate(insert.getReportTimeStr() + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
								insert.setReportTime(parseDate);
								insertList.add(insert);
								if (insertList.size() == 1000) {
									fmsServicecodeConsumptionMonthService.batchSave(insertList);
									insertList.clear();
								}
							}
							if (insertList.size() > 0) {
								fmsServicecodeConsumptionMonthService.batchSave(insertList);
								insertList.clear();
							}
						} else {
							break;
						}
						currentPage++;
					}
				}
				logger.info("【ServiceCodeConsumptionReportTask】--" + DateUtil.toString(date, "yyyy-MM-dd") + "月服务号消费统计执行成功，消耗时间为：[" + (System.currentTimeMillis() - beginmonthTime) + "ms]");

				SimpleDateFormat formatteryear = new SimpleDateFormat("yyyy");
				long beginyearTime = System.currentTimeMillis();
				String year = formatteryear.format(date);
				fmsServicecodeConsumptionYearService.delete(year);
				currentPage = 1;
				for (;;) {
					List<FmsServicecodeConsumptionYear> list = fmsServicecodeConsumptionMonthService.findReportYear(year, currentPage, pageSize);
					if (null != list && !list.isEmpty()) {
						List<FmsServicecodeConsumptionYear> insertList = new ArrayList<FmsServicecodeConsumptionYear>();
						for (FmsServicecodeConsumptionYear insert : list) {
							Date parseDate = DateUtil.parseDate(insert.getReportTimeStr() + "-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
							insert.setReportTime(parseDate);
							insertList.add(insert);
							if (insertList.size() == 1000) {
								fmsServicecodeConsumptionYearService.bachSave(insertList);
								insertList.clear();
							}
						}
						if (insertList.size() > 0) {
							fmsServicecodeConsumptionYearService.bachSave(insertList);
							insertList.clear();
						}
					} else {
						break;
					}
					currentPage++;
				}
				logger.info("【ServiceCodeConsumptionReportTask】--" + DateUtil.toString(date, "yyyy-MM-dd") + "年服务号消费统计执行成功，消耗时间为：[" + (System.currentTimeMillis() - beginyearTime) + "ms]");
			}
			return TaskResult.doBusinessSuccessResult();
		} catch (Exception e) {
			logger.error("【ServiceCodeConsumptionReportTask】--", e);
			return TaskResult.doBusinessFailResult();
		}
	}

}
