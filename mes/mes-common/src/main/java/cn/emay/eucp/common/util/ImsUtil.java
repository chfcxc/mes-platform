package cn.emay.eucp.common.util;

import org.apache.commons.lang3.StringUtils;

public class ImsUtil {
	// 计算长短信条数
	public static int getSmsCount(String content, int messageLength) {
		if (StringUtils.isEmpty(content)) {
			return 0;
		}
		int result = 1;
		if (!isEnTxt(content)) {
			if (content.length() > messageLength) {
				result = content.length() % (messageLength - 3) == 0 ? content.length() / (messageLength - 3) : (content.length() / (messageLength - 3)) + 1;
			}
		} else {
			if (content.length() > messageLength * 2) {
				result = content.length() % (messageLength * 2 - 6) == 0 ? content.length() / (messageLength * 2 - 6) : (content.length() / (messageLength * 2 - 6)) + 1;
			}
		}
		return result;
	}

	public static boolean isEnTxt(String content) {
		boolean flag = false;
		if (null != content) {
			char[] chars = content.toCharArray();
			for (int i : chars) {
				if (i < 32 || i > 126) {
					return flag;
				}
			}
			flag = true;
		}
		return flag;
	}

	public static void main(String[] args) {
		String s = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890";
		String s1 = "12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890a";
		String s2 = s + s;
		String s3 = "12345678901234567890123456789012345678901234567890123456789012345678901234567890好";
		String s4 = "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678";
		s4 += "|";
		System.out.println(getSmsCount(s, 70));
		System.out.println(getSmsCount(s1, 70));
		System.out.println(getSmsCount(s2, 70));
		System.out.println(getSmsCount(s3, 70));
		System.out.println(getSmsCount(s4, 70));
	}
}
