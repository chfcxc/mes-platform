package cn.emay.eucp.web.common;

import java.util.Arrays;

public class ExcelErrorArray {
	private ExcelErrorArray(){}
	/**
	 * Excel导入的错误信息数组
	 * @param obj 
	 * @param errorMessage
	 * @return
	 */
	public static String[] copyOfRange(String[]obj,String errorMessage){
		String[] error = Arrays.copyOfRange(obj, 0, obj.length + 1);
		error[obj.length] = errorMessage;
		return error;
	}
}
