package cn.emay.eucp.web.util;

import cn.emay.eucp.web.common.constant.CommonConstants;

public class SpecialSymbolUtil {

	public static String getNormalString(String s) {
		if (null != s) {
			return s.replaceAll(CommonConstants.DOUBLE_QUOTATION_MARKS, "\"").replaceAll(CommonConstants.SINGLE_QUOTATION_MARKS, "'").replaceAll(CommonConstants.LESS_THAN_MARKS, "<")
					.replaceAll(CommonConstants.MORE_THAN_MARKS, ">").replaceAll(CommonConstants.LOGIC_AND_MARKS, "&").replaceAll(CommonConstants.POINT_MARKS, "`");
		}
		return null;
	}

	public static String getNormalStringForMysql(String s) {
		if (null != s) {
			return s.replaceAll(CommonConstants.DOUBLE_QUOTATION_MARKS, "\"").replaceAll(CommonConstants.SINGLE_QUOTATION_MARKS, "'").replaceAll(CommonConstants.LESS_THAN_MARKS, "<")
					.replaceAll(CommonConstants.MORE_THAN_MARKS, ">").replaceAll(CommonConstants.LOGIC_AND_MARKS, "&").replaceAll(CommonConstants.POINT_MARKS, "`").replace("\\", "\\\\");
		}
		return null;
	}
}
