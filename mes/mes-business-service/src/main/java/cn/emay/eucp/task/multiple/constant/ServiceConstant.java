package cn.emay.eucp.task.multiple.constant;

public class ServiceConstant {

	public static final String MOQUEUE = "SMS_BAK_MO";

	public static final String REPORTQUEUE = "REPORT_BAK_QUEUE";

	public static Long fqFileSize = 256L * 1024L * 1024L;

	public static String FqPath;

	public static String COMMA = ",";

	// fq文件夹时间格式
	public final static String FQ_NAME_FORMAT = "yyyyMMdd";

	public static int fqPersistTime = 2;
	
	/**
	 * 黑词 间隔规则
	 */
	public static char[] rules = ",.~!@#$%^&*(){}[]; '，:-_\\/".toCharArray();

}
