package cn.emay.eucp.common.util;
import java.util.ArrayList;
import java.util.List;

public class TemplateInfoParseUtil {

	public static final String LEFT_SIGN_STRING = "${";
	public static final String RIGHT_SIGN_STRING = "}";

	public static List<String> getVarList(String s) {
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
			if (endIndex > startIndex) {
				String varString = s.substring(startIndex + 1, endIndex - 1);
				list.add(varString);
			}
		}
		return list;
	}

	public static String getVarString(String s) {
		List<String> list = getVarList(s);
		String result = org.apache.commons.lang3.StringUtils.join(list.toArray(), ",");
		return result;
	}

	public static void main(String[] args) {
		long a = System.nanoTime();
		List<String> list = getVarList("as{dui}das${姓名}}}}名}哈$哈哈${性别}嘿嘿${年龄}");
		long b = System.nanoTime();
		System.out.println("耗时：" + (b - a) / 1000 + "微秒");
		for (String s : list) {
			System.out.println(s);
		}

		String result = getVarString("as{dui}das}}}名}哈$哈哈${");
		System.out.println("sss:" + result);
	}
}
