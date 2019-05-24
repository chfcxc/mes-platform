package cn.emay.eucp.web.manage.controller.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import cn.emay.util.DateUtil;

public class StringUtil {
	private static final Logger log = Logger.getLogger(StringUtil.class);
	private static Pattern checkPattern = Pattern
			.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");

	public static String[] getMobileAdd86(String[] mobiles) {
		String[] result = null;
		try {
			if (mobiles == null || mobiles.length == 0) {
				return result;
			}
			result = new String[mobiles.length];
			for (int len = 0; len < mobiles.length; len++) {
				result[len] = mobiles[len];
			}
		} catch (Exception e) {
			log.error("Exception is happened!", e);
		}
		return result;
	}

	private static byte salts[] = new byte[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
			'4', '5', '6', '7', '8', '9' };

	private static Calendar calendar = Calendar.getInstance();

	public static String formateDeliverTime(String date) {
		String callback = null;
		Date result = null;
		if (isEmpty(date)) {
			return callback;
		}
		try {
			result = new SimpleDateFormat("yyyyMMddHHmm").parse(String.valueOf(calendar.get(Calendar.YEAR)).substring(0, 2) + date);
		} catch (Exception e) {
			try {
				result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
			} catch (ParseException e1) {
				log.error("Exception is happened!", e1);
			}
		}
		if (result != null) {
			callback = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(result);
		}
		return callback;
	}

	public static String date2String(Date d) {
		if (d == null) {
			return null;
		} else {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d);
		}
	}

	public static String date2String() {

		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

	}

	public static String date2String(Date date, String format) {
		if (date == null) {
			return null;
		} else {
			SimpleDateFormat sdateFormat = new SimpleDateFormat(format);
			return sdateFormat.format(date);
		}
	}

	public static StringBuilder fixArrayIds(Integer ids[]) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ids.length; i++) {
			sb.append(ids[i]).append(",");
		}

		if (sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
			return sb;
		} else {
			return null;
		}

	}

	/**
	 * 日期转换
	 * 
	 * @param val
	 * @return
	 */
	public static Date parseDate(String val) {
		try {
			if (StringUtil.isEmpty(val)) {
				return null;
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.parse(val);
		} catch (Exception ex) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				return sdf.parse(val);
			} catch (Exception ex2) {
				return null;
			}
		}
	}

	public static boolean checkSendTime(String sendtime) {
		try {
			// Pattern a = Pattern
			// .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s((([0-1][0-9])|(2?[0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
			return checkPattern.matcher(sendtime).matches();
		} catch (Exception e) {
			log.error("Exception is happened!", e);
			return false;
		}
	}

	public static StringBuilder implode(String delimiter, List<String> list) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; list != null && i < list.size(); i++) {
			sb.append(list.get(i)).append(delimiter);
		}
		int len = sb.length();
		if (len > 0) {
			sb.replace(len - delimiter.length(), len, "");
		}
		return sb;
	}

	/**
	 * 加入分隔符
	 * 
	 * @param delimiter
	 * @param list
	 * @return
	 */
	public static StringBuilder implode(String delimiter, String[] strs) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; strs != null && i < strs.length; i++) {
			sb.append(strs[i]).append(delimiter);
		}
		int len = sb.length();
		if (len > 0) {
			sb.replace(len - delimiter.length(), len, "");
		}
		return sb;
	}

	public static List<String> toList(String str) {
		List<String> list = new ArrayList<String>();
		if (str == null) {
			return list;
		}
		StringTokenizer st = new StringTokenizer(str, ",");
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			if (!isEmpty(s)) {
				list.add(s);
			}
		}
		return list;

	}

	public static Set<String> toSet(String str) {
		Set<String> set = new HashSet<String>();
		if (str == null) {
			return set;
		}
		StringTokenizer st = new StringTokenizer(str, ",");
		while (st.hasMoreTokens()) {
			String s = st.nextToken();
			if (!isEmpty(s)) {
				set.add(s);
			}
		}
		return set;
	}

	public static Date getNowDate() {
		try {
			Date currentTime = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
			String dateString = formatter.format(currentTime);
			return formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String updateSqlEscape(String s) {
		return s.replace("'", "\\'");
	}

	/**
	 * 转换
	 * 
	 * @param val
	 * @return
	 */
	public static String valueOf(Object obj, String defaultValue) {
		if (obj == null) {
			return defaultValue;
		} else {
			return String.valueOf(obj);
		}
	}

	public static String valueOf(String str, String defaultValue) {
		if (str == null) {
			return defaultValue;
		} else {
			return str;
		}
	}

	public static Integer toInteger(String str, Integer defaultValue) {
		try {
			return Integer.valueOf(str);
		} catch (Exception ex) {
			return defaultValue;
		}
	}

	public static Long toLong(String str, Long defaultValue) {
		try {
			return Long.valueOf(str);
		} catch (Exception ex) {
			return defaultValue;
		}
	}

	public static String generateSalt(int size) {
		Random rseed = new Random(System.currentTimeMillis());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append((char) salts[rseed.nextInt(salts.length)]);
		}

		return sb.toString();
	}

	public static String zerofill(long number, int length) {
		String str = String.format("%0" + length + "d", number);
		return str;
	}

	public static String filterHtml(String msg) {
		if (msg == null) {
			return null;
		}
		msg = msg.replace("<", "&lt;");
		msg = msg.replace(">", "&gt;");
		return msg;
	}

	public static String hashPassword(String password, String salt) {
		String encodedPwd = null;
		if (salt == null) {
			salt = generateSalt(16);
		}

		encodedPwd = md5(password + salt);

		return encodedPwd;
	}

	public static String md5(String msg) {
		try {
			StringBuilder digMsg = new StringBuilder();
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bmd = md.digest(msg.getBytes());

			for (int i = 0; i < bmd.length; i++) {

				int tmp = bmd[i];
				tmp = tmp & 0xFF;
				if (tmp < 0x10) {
					digMsg.append("0");
				}
				digMsg.append(Integer.toHexString(tmp));
			}

			return digMsg.toString();
		} catch (Exception e) {
			log.error("Exception is happened!", e);
			return null;
		}
	}

	public static byte OP_TYPE_CM = 1;
	public static byte OP_TYPE_UN = 2;
	public static byte OP_TYPE_TC = 3;

	public static byte getOperationType(String number) {
		try {
			number = number.trim();
			number = number.replace("+86", "");

			int flag = Integer.parseInt(number.substring(0, 3));
			if (flag == 134 || flag == 135 || flag == 136 || flag == 137 || flag == 138 || flag == 139 || flag == 147 || flag == 150 || flag == 151 || flag == 152 || flag == 157 || flag == 158
					|| flag == 159 || flag == 182 || flag == 187 || flag == 188 || flag == 183) {
				return OP_TYPE_CM;
			} else if (flag == 130 || flag == 131 || flag == 132 || flag == 155 || flag == 156 || flag == 185 || flag == 186 || flag == 145) {
				return OP_TYPE_UN;
			} else if (flag == 180 || flag == 189 || flag == 133 || flag == 153) {
				return OP_TYPE_TC;
			} else {
				return -1;
			}

		} catch (Exception ex) {
			return -1;
		}
	}

	// 根据内容获取短信条数
	public static int splitContent(String content, int longSmsSplitLength) {
		if (isEmpty(content)) {
			return 0;
		}
		// 短信最大拆分长度,以字节为单位 140，如果为长短信按140-6=134分割，长短信预留6字节消息头
		byte[] tmpContent = null;
		try {
			tmpContent = content.getBytes("UnicodeBigUnmarked");
		} catch (UnsupportedEncodingException e) {
			return 0;
		} // 长短信统一编码
		int result = 1;
		if (tmpContent.length > longSmsSplitLength * 2) {
			int smsSplitLen = longSmsSplitLength * 2 - 6;
			result = tmpContent.length % smsSplitLen == 0 ? tmpContent.length / smsSplitLen : tmpContent.length / smsSplitLen + 1;
		}
		return result;
	}

	// 不转字节，计算短信字数
	public static int getSmsCount(String content, int smsSplitLength) {
		if (isEmpty(content)) {
			return 0;
		}
		int result = 1;
		if (content.length() > smsSplitLength) {
			result = content.length() % (smsSplitLength - 3) == 0 ? content.length() / (smsSplitLength - 3) : (content.length() / (smsSplitLength - 3)) + 1;
		}
		return result;
	}

	// 不转字节，计算短信字数
	public static int getSmsCount(int contentLength, int smsSplitLength) {
		int result = 1;
		if (contentLength > smsSplitLength) {
			result = contentLength % (smsSplitLength - 3) == 0 ? contentLength / (smsSplitLength - 3) : (contentLength / (smsSplitLength - 3)) + 1;
		}
		return result;
	}

	public static String getBeforetime(int type, int minute, int day) {

		String time = "";
		try {
			Date dNow = new Date(); // 当前时间
			Calendar calendar = Calendar.getInstance(); // 得到日历
			calendar.setTime(dNow);// 把当前时间赋给日历
			if (type == 1) {
				calendar.add(Calendar.MINUTE, -minute); // 10分钟之前
			} else {
				calendar.add(Calendar.DAY_OF_MONTH, -day); // 设置为前一天
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置时间格式
			time = sdf.format(calendar.getTime()); // 格式化当前时间
		} catch (Exception e) {
			log.error("getBeforetime function Exception is happened!", e);
		}
		return time;
	}

	public static Date getBeforeDateTime(int type, int minute, int day) {
		Date time = null;
		try {
			Date dNow = new Date(); // 当前时间
			Calendar calendar = Calendar.getInstance(); // 得到日历
			calendar.setTime(dNow);// 把当前时间赋给日历
			if (type == 1) {
				calendar.add(Calendar.MINUTE, -minute); // 10分钟之前
			} else {
				calendar.add(Calendar.DAY_OF_MONTH, -day); // 设置为前一天
			}
			time = calendar.getTime(); // 格式化当前时间
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	public static Date getBeforeDaytime(int type, int minute, int day) {
		Date time = null;
		try {
			Date dNow = new Date(); // 当前时间
			Calendar calendar = Calendar.getInstance(); // 得到日历
			calendar.setTime(dNow);// 把当前时间赋给日历
			if (type == 1) {
				calendar.add(Calendar.MINUTE, -minute); // 10分钟之前
			} else {
				calendar.add(Calendar.DAY_OF_MONTH, -day); // 设置为前一天
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = formatter.format(calendar.getTime());
			time = formatter.parse(dateString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	public static String getTomorrowtime(int type, int minute, int day) {
		String dateString = null;
		try {
			Date dNow = new Date(); // 当前时间
			Calendar calendar = Calendar.getInstance(); // 得到日历
			calendar.setTime(dNow);// 把当前时间赋给日历
			if (type == 1) {
				calendar.add(Calendar.MINUTE, minute); // 10分钟之前
			} else {
				calendar.add(Calendar.DAY_OF_MONTH, day); // 设置为前一天
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			dateString = formatter.format(calendar.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dateString;
	}

	public static Date getTomorrowDateTime(int type, int minute, int day) {
		Date time = null;
		try {
			Date dNow = new Date(); // 当前时间
			Calendar calendar = Calendar.getInstance(); // 得到日历
			calendar.setTime(dNow);// 把当前时间赋给日历
			if (type == 1) {
				calendar.add(Calendar.MINUTE, minute); // 10分钟之前
			} else {
				calendar.add(Calendar.DAY_OF_MONTH, day); // 设置为前一天
			}
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = formatter.format(calendar.getTime());
			time = formatter.parse(dateString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	public static String calculateDateToString(Date date, int field, int amount, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return date2String(calendar.getTime(), format);
	}

	public static Date calculateDateToDate(Date date, int field, int amount, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 判断是否为空
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isEmpty(String val) {
		if (val == null || (val.trim()).length() == 0 || "null".equals(val.trim())) {
			return true;
		} else {
			return false;
		}
	}

	public static Long getMinute(Date date) {
		Long minute = Long.parseLong(DateUtil.toString(date, "yyyyMMddHHmm"));
		return minute;
	}

	public static Date getMinuteDate(Date date) {
		Date minuteTime = DateUtil.parseDate(DateUtil.toString(date, "yyyyMMddHHmm"), "yyyyMMddHHmm");
		return minuteTime;
	}

	public static Date getDate(Long date) {
		return DateUtil.parseDate(String.valueOf(date), "yyyyMMddHHmm");
	}

	public static Date getDayDate() {
		Date time = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String dateString = formatter.format(new Date());
			time = formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	public static String htmlDecode(String encodeStr) {
		return encodeStr.replaceAll("&amp;", "&").replaceAll("&lt;", "<").replaceAll("&gt;", ">").replaceAll("&quot;", "\"").replaceAll("&#x27;", "`").replaceAll("&#39;", "\'");
	}

}
