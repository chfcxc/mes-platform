package cn.emay.eucp.common.util;

import java.io.UnsupportedEncodingException;

import cn.emay.common.encryption.Md5;

/**
 * 模板id生成工具类
 * 
 * @author dinghaijiao
 *
 */

public class TemplateIdUtil {

	public static String getTemplateId(String content, Integer templateType, Integer i) throws UnsupportedEncodingException {

		if (i == null) {
			i = 0;
		}
		String templateId = Md5.md5(content.getBytes("utf-8")) + templateType + paratope(Integer.toHexString(i));
		return templateId;
	}

	private static String paratope(String num) {
		String format = "0000";
		int length = 4 - num.length();
		num = format.substring(0, length) + num;
		return num;
	}

	public static int getTemplateType(String templateId) {
		return Integer.parseInt(templateId.charAt(32) + "");
	}
}
