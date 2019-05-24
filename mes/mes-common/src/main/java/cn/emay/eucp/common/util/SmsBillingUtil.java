package cn.emay.eucp.common.util;

import java.io.UnsupportedEncodingException;

public class SmsBillingUtil {
	/**
	 * 按照短信内容，计算短信条数
	 * 
	 * @param content
	 * @param SmsSplitLength
	 * @return
	 */
	public static int getSmsCount(String content, int shortMessageLength, int longMessageLength) {
		if (isEmpty(content)) {
			return 0;
		}
		int result = 1;
		if (content.length() > shortMessageLength) {
			result = content.length() % (longMessageLength) == 0 ? content.length() / (longMessageLength) : (content.length() / (longMessageLength)) + 1;
		}
		return result;
	}

	/**
	 * 按照短信长度，计算短信条数
	 * 
	 * @param contentLength
	 * @param SmsSplitLength
	 * @return
	 */
	public static int getSmsCount(int contentLength, int shortMessageLength, int longMessageLength) {
		int result = 1;
		if (contentLength > shortMessageLength) {
			result = contentLength % (longMessageLength) == 0 ? contentLength / (longMessageLength) : (contentLength / (longMessageLength)) + 1;
		}
		return result;
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
}
