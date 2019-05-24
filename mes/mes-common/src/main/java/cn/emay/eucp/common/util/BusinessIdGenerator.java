package cn.emay.eucp.common.util;

import cn.emay.common.encryption.Md5;
import cn.emay.eucp.common.support.ServiceTargetGenerator;

/**
 * 业务ID生成
 * 
 * @author Frank
 * 
 */
public class BusinessIdGenerator {
	private static String ZERO10 = "0000000000";
	private static long SMS_ID_END_MILLIS = 0L;
	private static int SMS_ID_INDEX = 0;

	public synchronized static String genId() {
		long millis = System.currentTimeMillis();
		if (millis == SMS_ID_END_MILLIS) {
			SMS_ID_INDEX++;
		} else {
			SMS_ID_INDEX = 0;
			SMS_ID_END_MILLIS = millis;
		}
		if (SMS_ID_INDEX > 1000) {
			boolean wait = true;
			while (wait) {
				millis = System.currentTimeMillis();
				if (millis != SMS_ID_END_MILLIS) {
					wait = false;
					SMS_ID_END_MILLIS = millis;
					SMS_ID_INDEX = 0;
				}
			}
		}
		String indexStr = ZERO10 + SMS_ID_INDEX;
		indexStr = indexStr.substring(indexStr.length() - 3, indexStr.length());
		String imsId = Md5.md5For16(ServiceTargetGenerator.getServiceTarget().getBytes()) + millis + indexStr;
		return imsId;
		// return UUID.randomUUID().toString().toUpperCase().replace("-", "");
	}

}
