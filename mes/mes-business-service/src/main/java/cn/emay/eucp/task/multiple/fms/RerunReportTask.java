package cn.emay.eucp.task.multiple.fms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeDay;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeMonth;
import cn.emay.eucp.common.moudle.db.fms.FmsEnterpriseContentTypeYear;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionDay;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionMonth;
import cn.emay.eucp.common.moudle.db.fms.FmsServicecodeConsumptionYear;
import cn.emay.eucp.data.service.fms.FmsEnterpriseContentTypeDayService;
import cn.emay.eucp.data.service.fms.FmsEnterpriseContentTypeMonthService;
import cn.emay.eucp.data.service.fms.FmsEnterpriseContentTypeYearService;
import cn.emay.eucp.data.service.fms.FmsMessageService;
import cn.emay.eucp.data.service.fms.FmsServicecodeConsumptionDayService;
import cn.emay.eucp.data.service.fms.FmsServicecodeConsumptionMonthService;
import cn.emay.eucp.data.service.fms.FmsServicecodeConsumptionYearService;
import cn.emay.eucp.task.multiple.util.DateUtil;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;

public class RerunReportTask implements PeriodTask {
	public final static String SERVICE_CODE_CONSUMPTION_REPORT_DAY = "SERVICE_CODE_CONSUMPTION_REPORT_DAY";
	public final static String SERVICE_CODE_CONSUMPTION_REPORT_MONTH = "SERVICE_CODE_CONSUMPTION_REPORT_MONTH";
	public final static String SERVICE_CODE_CONSUMPTION_REPORT_YEAR = "SERVICE_CODE_CONSUMPTION_REPORT_YEAR";
	public final static String CONTENT_TYPE_REPORT_DAY = "CONTENT_TYPE_REPORT_DAY";
	public final static String CONTENT_TYPE_REPORT_MONTH = "CONTENT_TYPE_REPORT_MONTH";
	public final static String CONTENT_TYPE_REPORT_YEAR = "CONTENT_TYPE_REPORT_YEAR";
	private static FmsMessageService fmsMessageService = BeanFactoryUtils.getBean(FmsMessageService.class);
	private static FmsEnterpriseContentTypeDayService fmsEnterpriseContentTypeDayService = BeanFactoryUtils.getBean(FmsEnterpriseContentTypeDayService.class);
	private static FmsEnterpriseContentTypeMonthService fmsEnterpriseContentTypeMonthService = BeanFactoryUtils.getBean(FmsEnterpriseContentTypeMonthService.class);
	private static FmsEnterpriseContentTypeYearService fmsEnterpriseContentTypeYearService = BeanFactoryUtils.getBean(FmsEnterpriseContentTypeYearService.class);
	private static FmsServicecodeConsumptionDayService fmsServicecodeConsumptionDayService = BeanFactoryUtils.getBean(FmsServicecodeConsumptionDayService.class);
	private static FmsServicecodeConsumptionMonthService fmsServicecodeConsumptionMonthService = BeanFactoryUtils.getBean(FmsServicecodeConsumptionMonthService.class);
	private static FmsServicecodeConsumptionYearService fmsServicecodeConsumptionYearService = BeanFactoryUtils.getBean(FmsServicecodeConsumptionYearService.class);
	private static RedisClient redis = BeanFactoryUtils.getBean(RedisClient.class);
	private static final Logger logger = Logger.getLogger(RerunReportTask.class);

	@Override
	public TaskResult exec(Map<String, String> arg0) {
		try {
			Date date = new Date();
			String consumptionDay = redis.rpop(SERVICE_CODE_CONSUMPTION_REPORT_DAY);
			int currentPage = 1;
			int pageSize = 10000;
			if (null != consumptionDay) {
				serviceCodeDay(consumptionDay, date, currentPage, pageSize);
				String monthTime = getMonthTime(consumptionDay);
				serviceCodeMonth(monthTime, currentPage, pageSize, date);
				String yearTime = getYearTime(consumptionDay);
				serviceCodeYear(yearTime, currentPage, pageSize, date);
			}

			String consumptionMonth = redis.rpop(SERVICE_CODE_CONSUMPTION_REPORT_MONTH);
			if (null != consumptionMonth) {
				serviceCodeMonth(consumptionMonth, currentPage, pageSize, date);
			}

			String consumptionYear = redis.rpop(SERVICE_CODE_CONSUMPTION_REPORT_YEAR);
			if (null != consumptionYear) {
				serviceCodeYear(consumptionYear, currentPage, pageSize, date);
			}
			String contentDay = redis.rpop(CONTENT_TYPE_REPORT_DAY);
			if (null != contentDay) {
				contentDay(contentDay, currentPage, pageSize, date);
				String monthTime = getMonthTime(consumptionDay);
				contentMonth(monthTime, currentPage, pageSize, date);
				String yearTime = getYearTime(consumptionDay);
				contentYear(yearTime, currentPage, pageSize, date);
			}

			String contentMonth = redis.rpop(CONTENT_TYPE_REPORT_MONTH);
			if (null != contentMonth) {
				contentMonth(contentMonth, currentPage, pageSize, date);
			}

			String contentYear = redis.rpop(CONTENT_TYPE_REPORT_YEAR);
			if (null != contentYear) {
				contentYear(contentYear, currentPage, pageSize, date);
			}
			return TaskResult.doBusinessSuccessResult();
		} catch (Exception e) {
			logger.error("重跑任务异常" + e);
			return TaskResult.doBusinessFailResult();
		}
	}

	@Override
	public long period() {
		return 0;
	}

	private String getMonthTime(String day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		Date parse;
		String month = "";
		try {
			parse = format.parse(day);
			month = format.format(parse);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return month;
	}

	private String getYearTime(String day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Date parse1;
		String year = "";
		try {
			parse1 = format.parse(day);
			year = format.format(parse1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return year;
	}

	private void serviceCodeDay(String consumptionDay, Date date, int currentPage, int pageSize) {

		Map<String, String> map = this.day(consumptionDay);
		if (map.isEmpty()) {
			logger.error("【RerunReportTask】：SERVICE_CODE_CONSUMPTION_REPORT_DAY队列输入时间格式有误");
		}
		String string = map.get("start");
		String endday = map.get("end");

		Date parseDate = DateUtil.parseDate(string, "yyyy-MM-dd");
		Date end = DateUtil.parseDate(endday, "yyyy-MM-dd");
		if (parseDate.getTime() > end.getTime()) {
			logger.error("【RerunReportTask】：SERVICE_CODE_CONSUMPTION_REPORT_DAY队列起始时间" + string + "大于结束时间" + end);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate);
		Integer poor = this.getDatePoor(consumptionDay, "day");
		for (int i = 0; i <= poor; i++) {
			if (i != 0) {
				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
			currentPage = 1;
			Date time = cal.getTime();

			String scday = DateUtil.toString(time, "yyyy-MM-dd");
			if (time.before(date)) {
				fmsServicecodeConsumptionDayService.deleteByTime(scday);
				for (;;) {
					List<FmsServicecodeConsumptionDay> list = fmsMessageService.findConsumptionReport(scday, time, currentPage, pageSize);
					List<FmsServicecodeConsumptionDay> insertList = new ArrayList<FmsServicecodeConsumptionDay>();
					if (null != list && list.size() > 0) {
						for (FmsServicecodeConsumptionDay fmsServicecodeConsumptionDay : list) {
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
			} else {
				logger.error("日期错误");
			}
		}
		logger.info("【RerunReportTask】--" + DateUtil.toString(date, "yyyy-MM-dd") + "日服务号消费统计报表重跑执行成功");

	}

	private void serviceCodeMonth(String consumptionMonth, int currentPage, int pageSize, Date date) {

		Integer poor = this.getDatePoor(consumptionMonth, "month");
		Map<String, String> map = this.day(consumptionMonth);
		if (map.isEmpty()) {
			logger.error("【RerunReportTask】:SERVICE_CODE_CONSUMPTION_REPORT_MONTH队列：输入时间格式有误");
		}
		String string = map.get("start");
		String endMonth = map.get("end");
		Date starttime = DateUtil.parseDate(string, "yyyy-MM");
		Date endtime = DateUtil.parseDate(endMonth, "yyyy-MM");
		if (starttime.getTime() > endtime.getTime()) {
			logger.error("【RerunReportTask】：SERVICE_CODE_CONSUMPTION_REPORT_MONTH队列起始时间" + string + "大于结束时间" + endMonth);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(starttime);
		for (int i = 0; i <= poor; i++) {
			if (i != 0) {
				cal.add(Calendar.MONTH, 1);
			}
			Date time = cal.getTime();
			String scmonth = DateUtil.toString(time, "yyyy-MM");
			if (time.before(date)) {
				fmsServicecodeConsumptionMonthService.deleteMonth(scmonth);
				currentPage = 1;
				for (;;) {
					String s = DateUtil.toString(DateUtil.getTheMonthLastDay(time), "yyyy-MM-dd");
					Date end = DateUtil.parseDate(s + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
					List<FmsServicecodeConsumptionMonth> report = fmsServicecodeConsumptionDayService.findReportMonth(time, end, currentPage, pageSize);
					if (null != report && !report.isEmpty()) {
						List<FmsServicecodeConsumptionMonth> insertList = new ArrayList<FmsServicecodeConsumptionMonth>();
						for (FmsServicecodeConsumptionMonth insert : report) {
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
		}
		logger.info("【RerunReportTask】--" + DateUtil.toString(date, "yyyy-MM") + "月服务号消费统计报表重跑执行成功");

	}

	private void serviceCodeYear(String consumptionYear, int currentPage, int pageSize, Date date) {

		Integer poor = this.getDatePoor(consumptionYear, "year");
		Map<String, String> map = this.day(consumptionYear);
		if (map.isEmpty()) {
			logger.error("【RerunReportTask】：SERVICE_CODE_CONSUMPTION_REPORT_YEAR队列输入时间格式有误");
		}
		String startyear = map.get("start");
		String endtyear = map.get("end");
		Date parseDate = DateUtil.parseDate(startyear, "yyyy");
		Date endDate = DateUtil.parseDate(endtyear, "yyyy");
		if (parseDate.getTime() > endDate.getTime()) {
			logger.error("【RerunReportTask】：SERVICE_CODE_CONSUMPTION_REPORT_YEAR队列起始时间" + startyear + "大于结束时间" + endtyear);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate);
		for (int i = 0; i <= poor; i++) {
			if (i != 0) {
				cal.add(Calendar.YEAR, 1);
			}
			Date time = cal.getTime();
			String scyear = DateUtil.toString(time, "yyyy");
			if (time.before(date)) {
				fmsServicecodeConsumptionYearService.delete(scyear);
				currentPage = 1;
				for (;;) {
					List<FmsServicecodeConsumptionYear> list = fmsServicecodeConsumptionMonthService.findReportYear(scyear, currentPage, pageSize);
					if (null != list && !list.isEmpty()) {
						List<FmsServicecodeConsumptionYear> insertList = new ArrayList<FmsServicecodeConsumptionYear>();
						for (FmsServicecodeConsumptionYear insert : list) {
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
			}

		}
		logger.info("【RerunReportTask】--" + DateUtil.toString(date, "yyyy") + "年服务号消费 统计报表重跑执行成功");

	}

	private void contentDay(String contentDay, int currentPage, int pageSize, Date date) {

		Map<String, String> map = this.day(contentDay);
		if (map.isEmpty()) {
			logger.error("【RerunReportTask】：SERVICE_CODE_CONSUMPTION_REPORT_DAY队列输入时间格式有误");
		}
		String string = map.get("start");
		String endday = map.get("end");

		Date parseDate = DateUtil.parseDate(string, "yyyy-MM-dd");
		Date end = DateUtil.parseDate(endday, "yyyy-MM-dd");
		if (parseDate.getTime() > end.getTime()) {
			logger.error("【RerunReportTask】：SERVICE_CODE_CONSUMPTION_REPORT_DAY队列起始时间" + string + "大于结束时间" + end);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate);
		long beginTime = System.currentTimeMillis();
		Integer poor = this.getDatePoor(contentDay, "day");
		for (int i = 0; i <= poor; i++) {
			if (i != 0) {
				cal.add(Calendar.DAY_OF_MONTH, 1);
			}
			currentPage = 1;
			Date time = cal.getTime();
			String cday = DateUtil.toString(time, "yyyy-MM-dd");
			if (time.before(date)) {
				fmsEnterpriseContentTypeDayService.deleteByTime(cday);
				for (;;) {
					List<FmsEnterpriseContentTypeDay> list = fmsMessageService.findContentTypeReport(cday, time, currentPage, pageSize);
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
			} else {
				logger.error("日期错误");
			}
		}

	}

	private void contentMonth(String contentMonth, int currentPage, int pageSize, Date date) {

		Integer poor = this.getDatePoor(contentMonth, "month");
		Map<String, String> map = this.day(contentMonth);
		if (map.isEmpty()) {
			logger.error("【RerunReportTask】:SERVICE_CODE_CONSUMPTION_REPORT_MONTH队列：输入时间格式有误");
		}
		String string = map.get("start");
		String endMonth = map.get("end");
		Date starttime = DateUtil.parseDate(string, "yyyy-MM");
		Date endtime = DateUtil.parseDate(endMonth, "yyyy-MM");
		if (starttime.getTime() > endtime.getTime()) {
			logger.error("【RerunReportTask】：SERVICE_CODE_CONSUMPTION_REPORT_MONTH队列起始时间" + string + "大于结束时间" + endMonth);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(starttime);
		for (int i = 0; i <= poor; i++) {
			currentPage = 1;
			if (i != 0) {
				cal.add(Calendar.MONTH, 1);
			}
			Date time = cal.getTime();
			String cmonth = DateUtil.toString(time, "yyyy-MM");
			if (time.before(date)) {
				fmsEnterpriseContentTypeMonthService.deleteMonth(cmonth);
				for (;;) {
					String s = DateUtil.toString(DateUtil.getTheMonthLastDay(time), "yyyy-MM-dd");
					Date end = DateUtil.parseDate(s + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
					List<FmsEnterpriseContentTypeMonth> report = fmsEnterpriseContentTypeDayService.findMonth(time, end, currentPage, pageSize);
					if (null != report && !report.isEmpty()) {
						List<FmsEnterpriseContentTypeMonth> insertList = new ArrayList<FmsEnterpriseContentTypeMonth>();
						for (FmsEnterpriseContentTypeMonth insert : report) {
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
		}
		logger.info("【RerunReportTask】--" + DateUtil.toString(date, "yyyy-MM") + "月服务号消费统计报表重跑执行成功");

	}

	private void contentYear(String contentYear, int currentPage, int pageSize, Date date) {

		Integer poor = this.getDatePoor(contentYear, "year");
		Map<String, String> map = this.day(contentYear);
		if (map.isEmpty()) {
			logger.error("【RerunReportTask】：SERVICE_CODE_CONSUMPTION_REPORT_YEAR队列输入时间格式有误");
		}
		String startyear = map.get("start");
		String endtyear = map.get("end");
		Date parseDate = DateUtil.parseDate(startyear, "yyyy");
		Date endDate = DateUtil.parseDate(endtyear, "yyyy");
		if (parseDate.getTime() > endDate.getTime()) {
			logger.error("【RerunReportTask】：SERVICE_CODE_CONSUMPTION_REPORT_YEAR队列起始时间" + startyear + "大于结束时间" + endtyear);
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(parseDate);
		long beginyearTime = System.currentTimeMillis();
		for (int i = 0; i <= poor; i++) {
			if (i != 0) {
				cal.add(Calendar.YEAR, 1);
			}
			Date time = cal.getTime();
			String cyear = DateUtil.toString(time, "yyyy");
			if (time.before(date)) {
				fmsEnterpriseContentTypeYearService.deleteYear(cyear);
				currentPage = 1;
				for (;;) {
					List<FmsEnterpriseContentTypeYear> list = fmsEnterpriseContentTypeMonthService.findYear(cyear, currentPage, pageSize);
					if (null != list && !list.isEmpty()) {
						List<FmsEnterpriseContentTypeYear> insertList = new ArrayList<FmsEnterpriseContentTypeYear>();
						for (FmsEnterpriseContentTypeYear insert : list) {
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

		}
		logger.info("【RerunReportTask】--" + DateUtil.toString(date, "yyyy") + "年服务号消费 统计报表重跑执行成功");

	}

	private Map<String, String> day(String day) {
		Map<String, String> map = new HashMap<String, String>();
		String[] split = day.split(",");
		if (null == split[0]) {
			return map;
		}
		if (null == split[1]) {
			return map;
		}
		String startday = split[0].toString();
		String endday = split[1].toString();

		map.put("start", startday);
		map.put("end", endday);

		return map;
	}

	private Integer getDatePoor(String day, String type) {
		Map<String, String> map = this.day(day);
		String start = map.get("start");
		String end = map.get("end");
		int number = 0;
		if ("day".equals(type)) {
			Date startDate = DateUtil.parseDate(start, "yyyy-MM-dd");
			Date endDate = DateUtil.parseDate(end, "yyyy-MM-dd");
			long nd = 1000 * 24 * 60 * 60;
			long diff = endDate.getTime() - startDate.getTime();
			// 计算差多少天
			Long daynumber = diff / nd;
			number = daynumber.intValue();
		}
		if ("month".equals(type)) {
			Date startDate = DateUtil.parseDate(start, "yyyy-MM");
			Date endDate = DateUtil.parseDate(end, "yyyy-MM");
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(startDate);
			c2.setTime(endDate);
			int year1 = c1.get(Calendar.YEAR);
			int year2 = c2.get(Calendar.YEAR);
			int month1 = c1.get(Calendar.MONTH);
			int month2 = c2.get(Calendar.MONTH);
			int monthnumber = 0;
			if (year1 == year2) {
				monthnumber = month2 - month1;
			} else {
				int year = year2 - year1;
				monthnumber = (month2 - month1) * year * 12;
			}
			number = monthnumber;
		}
		if ("year".equals(type)) {
			Date startDate = DateUtil.parseDate(start, "yyyy");
			Date endDate = DateUtil.parseDate(end, "yyyy");
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();
			c1.setTime(startDate);
			c2.setTime(endDate);
			int year1 = c1.get(Calendar.YEAR);
			int year2 = c2.get(Calendar.YEAR);
			int yearnumber = year2 - year1;
			number = yearnumber;
		}
		return number;
	}
}
