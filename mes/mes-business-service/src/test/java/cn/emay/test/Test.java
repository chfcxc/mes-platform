package cn.emay.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.eucp.task.multiple.util.DateUtil;

public class Test {

	// appId
	static String appId = "EUCe-EMt-SMS9-XXXXX";// 请联系销售，或者在页面中 获取
	// 密钥
	static String secretKey = "1234567893214567";// 请联系销售，或者在页面中 获取

	private static Integer REPORTTIMEOUT = 48;
	public static final String LEFT_SIGN_STRING = "${";
	public static final String RIGHT_SIGN_STRING = "}";

	public static void main(String[] args) {
		// long a = System.nanoTime();
		/*
		 * // List<String> list = getVarString("as{dui}das${姓名}*838}名}哈$哈哈${性别}嘿嘿${年龄}"); String json = "{姓名:小丁,性别:女,年龄:22}";
		 * 
		 * @SuppressWarnings("rawtypes") Map map = JsonHelper.fromJson(Map.class, json); String str = getStr("das${姓名}*838名}哈$哈哈${性别}嘿嘿${年龄}", map); long b = System.nanoTime();
		 * System.out.println("耗时：" + (b - a) / 1000 + "微秒"); System.out.println(str);
		 */
		String str = "2019-05";
		Date parseDate = DateUtil.parseDate(str + "-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
		String string = DateUtil.toString(parseDate, "yyyy-MM-dd HH:mm:ss");
		System.out.println(string);

	}

	/**
	 * 自己测试，初始化参数
	 */
	public static List<Date> getReportDays() {
		int days = REPORTTIMEOUT / 24 + 1;
		if (REPORTTIMEOUT % 24 != 0) {
			days++;
		}
		Date d = new Date();
		// 初始化每天的数据
		Date startParseDate = DateUtil.getOtherDayStart(d, -days);
		Date endParseDate = DateUtil.getOtherDayStart(d, -1);
		List<Date> listDate = execDateList(startParseDate, endParseDate);
		return listDate;
	}

	public static List<Date> execDateList(Date startDate, Date endDate) {
		if (endDate.before(startDate)) {
			throw new IllegalStateException("结束时间比开始小.");
		}
		List<Date> dates = new ArrayList<Date>();
		int i = 0;
		for (;;) {
			Date otherStartDay = DateUtil.getOtherDayEnd(startDate, i);
			dates.add(otherStartDay);
			i++;
			if (otherStartDay.after(endDate)) {
				break;
			}
		}
		return dates;
	}

	public static List<String> getVarString(String s) {
		List<String> list = new ArrayList<String>();
		int startIndex = 0;
		int endIndex = 0;
		for (;;) {
			int start = s.indexOf(LEFT_SIGN_STRING, startIndex);
			if (start >= 0) {
				startIndex = start + 1;
			} else {
				break;
			}
			for (;;) {
				int end = s.indexOf(RIGHT_SIGN_STRING, endIndex);
				if (end >= 0) {
					endIndex = end + 1;
					if (endIndex > startIndex) {
						break;
					} else {
						continue;
					}
				} else {
					break;
				}
			}
			String varString = s.substring(startIndex + 1, endIndex - 1);
			list.add(varString);
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	public static String getStr(String s, Map map) {
		Set entrySet = map.keySet();
		int startIndex = 0;
		int endIndex = 0;
		String replace = "";

		for (Object object : entrySet) {
			int start = s.indexOf(LEFT_SIGN_STRING, startIndex);
			if (start >= 0) {
				startIndex = start + 1;
			} else {
				break;
			}

			for (;;) {
				int end = s.indexOf(RIGHT_SIGN_STRING, endIndex);
				if (end >= 0) {
					endIndex = end + 1;
					if (endIndex > startIndex) {
						break;
					} else {
						continue;
					}
				} else {
					break;
				}
			}
			String varString = s.substring(startIndex - 1, endIndex);
			String string = map.get(object).toString();
			replace = s.replace(varString, string);
			s = replace;
		}
		return replace;

	}
}
