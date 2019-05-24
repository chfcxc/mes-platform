package cn.emay.eucp.web.util;

import java.util.Random;

public class MasksUtil {

	public static String getMobileMasks(String mobile) {
		if (null != mobile) {
			StringBuffer mobileSb = new StringBuffer(mobile.trim());
			if (mobileSb.length() >= 7) {
				return mobileSb.replace(3, 7, "****").toString();
			}
		}
		return mobile;
	}

	public static String getContentMasksWithRegular(String content) {
		if (null != content) {
			return content.replaceAll("[(0-9a-zA-Z)]", "*");
		}
		return content;
	}

	public static String getContentMasksWithAscii(String content) {
		if (null != content) {
			StringBuffer sb = new StringBuffer();
			char[] chars = content.toCharArray();
			for (char c : chars) {
				int i = c;
				if ((i >= 48 && i <= 57) || (i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
					sb.append("*");
				} else {
					sb.append(c);
				}
			}
			return sb.toString();
		}
		return content;
	}

	public static void main(String[] args) {
		String s = "";
		for (int i = 0; i < 20000; i++) {
			if (i % 2 == 0) {
				s += "a";
			} else {
				s += new Random().nextInt(9) + "";
			}
		}
		System.out.println(s.length());
		getContentMasksWithRegular(s);
		getContentMasksWithAscii(s);
		long a = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			getContentMasksWithRegular(s);
		}
		System.out.println(System.currentTimeMillis() - a);
		a = System.currentTimeMillis();
		for (int i = 0; i < 1000; i++) {
			getContentMasksWithAscii(s);
		}
		System.out.println(System.currentTimeMillis() - a);
		s = "!@#456QWErty!@#$%^哈哈";
		System.out.println(getContentMasksWithRegular(s));
		System.out.println(getContentMasksWithAscii(s));

	}
}
