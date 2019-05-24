package cn.emay.eucp.web.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import cn.emay.util.DateUtil;
import cn.emay.util.RequestUtils;

public class RequestGetParamUtils {

	public static Map<String, Date> getParams(HttpServletRequest request, String rate,Date time) {
		Map<String, Date> map = new HashMap<String, Date>();
		Date startTime = null;
		Date endTime = null;
		if (!StringUtils.isEmpty(rate)) {
			if ("DAY".equals(rate)) {
				startTime = RequestUtils.getDateParameter(request, "startTime", "yyyy-MM-dd", time);
				endTime = RequestUtils.getDateParameter(request, "endTime", "yyyy-MM-dd", null);
			} else if ("MONTH".equals(rate)) {
				startTime = RequestUtils.getDateParameter(request, "startTime", "yyyy-MM", time);
				String end = request.getParameter("endTime");
				if (!StringUtils.isEmpty(end)) {
					endTime = DateUtil.getTheMonthLastDay(DateUtil.parseDate(end, "yyyy-MM"));
				}
			} else if ("YEAR".equals(rate)) {
				startTime = RequestUtils.getDateParameter(request, "startTime", "yyyy", time);
				String end = request.getParameter("endTime");
				if (!StringUtils.isEmpty(end)) {
					endTime = DateUtil.parseDate(end + "-12-31", "yyyy-MM-dd");
				}
			}
		}
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return map;
	}

	public static boolean checkReportRateParam(String rate) {
		boolean result = false;
		if ("YEAR".equals(rate) || "MONTH".equals(rate) || "DAY".equals(rate)) {
			result = true;
		}
		return result;
	}
	
	/**
	 * oracle 查询默认7天查询
	 */
	public static Map<String,Object> getOracleInputDateTime(Date startTime, Date endTime){
		//数据最多查询到前90天
		Date now=new Date();
		Date time=getFirstTime(now,-90);
		Map<String,Object> map=new HashMap<String, Object>();
		if (null == startTime && null == endTime) {
			map.put("error","请输入提交时间");
			return map;
		}else if(null!=startTime&&null==endTime){//默认7天查询
			if(startTime.getTime() < time.getTime()){
				map.put("error","开始时间范围超过90天，暂不支持");
				return map;
			}
			endTime=getEndTime(startTime,6);
			Date lastDay=getEndTime(now,0);
			if(endTime.getTime()>lastDay.getTime()){
				endTime=lastDay;
			}
		}else if(null==startTime&&null!=endTime){
			if(endTime.getTime() < time.getTime()){
				map.put("error","开始时间范围超过90天，暂不支持");
				return map;
			}
			Date beforeTime=getFirstTime(endTime,-6);
			if(beforeTime.getTime()<time.getTime()){
				startTime=time;
			}else{
				startTime=beforeTime;
			}
		}else if(null!=startTime&&null!=endTime){
			if(startTime.getTime() < time.getTime()){
				map.put("error","开始时间范围超过90天，暂不支持");
				return map;
			}
			if(endTime.getTime()-startTime.getTime()>7*24*60*60*1000l){
				map.put("error","查询时间范围限定为7天");
				return map;
			}
		}
		map.put("error","");
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return map;
	}
	
	
	public static Date getFirstTime(Date date,int amount){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, amount);
		String day = DateUtil.toString(cal.getTime(), "yyyy-MM-dd") + " 00:00:00";
		Date time = DateUtil.parseDate(day, "yyyy-MM-dd HH:mm:ss");
		return time;
	}
	
	public static Date getEndTime(Date date,int amount){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, amount);
		String day = DateUtil.toString(cal.getTime(), "yyyy-MM-dd") + " 23:59:59";
		Date time = DateUtil.parseDate(day, "yyyy-MM-dd HH:mm:ss");
		return time;
	}
	
	public static Set<String> getRedDate(int reportTimeout){
		Set<String> set = new HashSet<String>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -1);// 统计开始时间
		int countTime = reportTimeout % 24 == 0 ? reportTimeout / 24 + 1 : reportTimeout / 24 + 1 + 1;// 统计时间(状态报告超时时间天数加1)
		for (int i = 1; i < countTime; i++) {
			String reportday = DateUtil.toString(cal.getTime(), "yyyy-MM-dd");
			set.add(reportday);
			cal.add(Calendar.DAY_OF_YEAR, -1);
		}
		return set;
	}
	
	public static String getReportformat(String rate){
		String format=null;
		if ("DAY".equalsIgnoreCase(rate)) {
			format = "yyyy-MM-dd";
		} else if ("MONTH".equals(rate)) {
			format = "yyyy-MM";
		} else {
			format = "yyyy";
		}
		return format;
	}
	
	public static Map<String,Date> getClientSelectTime(Date startTime, Date endTime,Date startClientSelectTime,Date endClientSelectTime){
		//查询时间范围：startTime、endTime；客户端支持查询范围：startClientSelectTime、endClientSelectTime
		Map<String, Date> map = new HashMap<String, Date>();
		Date now=new Date();
		Date ninetyTime=getFirstTime(now,-90);
		Date nowEndTime=getEndTime(now,0);
		//支持查询时间范围为空的情况；时间范围为空则默认查询90的数据
		if(startTime == null) {
			startTime = ninetyTime;
		}
		if(endTime == null) {
			endTime = now;
		}
		if(startTime.getTime() < ninetyTime.getTime()) {
			startTime = ninetyTime;
		}
		if(endTime.getTime() > nowEndTime.getTime()) {
			endTime = nowEndTime;
		}
		//startClientSelectTime、endClientSelectTime
		if(startClientSelectTime == null) {
			startClientSelectTime = ninetyTime;
		}
		if(endClientSelectTime == null) {
			endClientSelectTime = now;
		}
		endClientSelectTime = getEndTime(endClientSelectTime,0);
		
		if(startClientSelectTime.getTime() < ninetyTime.getTime()) {
			startClientSelectTime = ninetyTime;
		}
		
		if(endClientSelectTime.getTime() > nowEndTime.getTime()) {
			endClientSelectTime = nowEndTime;
		}
		
		if(startClientSelectTime.getTime()>= startTime.getTime() && startClientSelectTime.getTime() <= endTime.getTime() && endClientSelectTime.getTime() >= endTime.getTime()) {
			startTime = startClientSelectTime;
		} else if (endClientSelectTime.getTime()>= startTime.getTime() && endClientSelectTime.getTime() <= endTime.getTime() && startClientSelectTime.getTime() <= startTime.getTime()) {
			endTime = endClientSelectTime;
		} else if(startClientSelectTime.getTime()<= startTime.getTime() && endClientSelectTime.getTime() >= endTime.getTime()) {
		} else if(startClientSelectTime.getTime()>= startTime.getTime() && endClientSelectTime.getTime() <= endTime.getTime()) {
			startTime = startClientSelectTime;
			endTime = endClientSelectTime;
		} else {//查询数据范围不在客户端配置的查询范围内
			startTime = null;
			endTime = null;
		}
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return map;
	}
	
}
