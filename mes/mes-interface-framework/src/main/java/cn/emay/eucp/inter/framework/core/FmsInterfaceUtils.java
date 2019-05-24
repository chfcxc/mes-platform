package cn.emay.eucp.inter.framework.core;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.utils.properties.PropertiesUtils;

public class FmsInterfaceUtils {

	// 限速缓存
	private static Map<String, Set<Long>> limitMap = new ConcurrentHashMap<String, Set<Long>>();

	// 一次获取状态报告数量
	public static int ONCE_GETREPORT_NUM;
	// 一次获取状态报告时间间隔
	public static int ONCE_GETREPORT_TIME;
	// 一次获取余额数量
	public static int ONCE_GETBALACE_NUM;
	// 一次获取余额时间间隔
	public static int ONCE_GETBALACE_TIME;

	static {
		ONCE_GETREPORT_NUM = PropertiesUtils.getIntProperty("isover.getbalace.num", "eucp.service.properties", -1);

		ONCE_GETBALACE_NUM = PropertiesUtils.getIntProperty("isover.getReport.num", "eucp.service.properties", -1);
	}

	/** 请求是否超速 */
	public static boolean isOverSpeed(String appId, FmsServiceCodeDto smsServiceCode, String type) {
		long now = System.currentTimeMillis();
		boolean isOverSpeed = false;
		Integer interval = 1;
		Integer number = 0;
		if ("report".equals(type)) {
			number = ONCE_GETREPORT_NUM;
		} else if ("balance".equals(type)) {
			number = ONCE_GETBALACE_NUM;
		} else {
			isOverSpeed = true;
			return isOverSpeed;
		}
		if (number == null || number <= 0) {
			return isOverSpeed;
		}
		String key = appId + "_" + type;
		if (limitMap.containsKey(key)) {
			Set<Long> visittimes = limitMap.get(key);
			synchronized (visittimes) {
				Set<Long> deletes = new HashSet<Long>();
				for (Long time : visittimes) {
					if (now - time > interval * 1000) {
						deletes.add(time);
					}
				}
				visittimes.removeAll(deletes);
				if (visittimes.size() >= number) {
					isOverSpeed = true;
				} else {
					visittimes.add(now);
				}
			}
		} else {
			Set<Long> visittimes = new HashSet<Long>();
			visittimes.add(now);
			limitMap.put(key, visittimes);
		}
		return isOverSpeed;
	}

	/** 是否正确IP */
	public static boolean isAllowCustomIp(String allowIps, String remoteIp) {
		if (allowIps == null || allowIps.trim().length() == 0) {
			return true;
		}
		String[] ips = allowIps.split(",");
		for (String ip : ips) {
			if (ip.endsWith("*")) {
				if (ip.substring(0, ip.length() - 2).equals(remoteIp.subSequence(0, remoteIp.lastIndexOf(".")))) {
					return true;
				}
			} else {
				if (ip.equals(remoteIp)) {
					return true;
				}
			}
		}
		return false;
	}

	/** 计算短信条数 */
	public static int getSmsCount(String content, int smsSplitLength) {
		if (content == null || (content.trim()).length() == 0) {
			return 0;
		}
		content = content.trim();
		int result = 1;
		if (content.length() > smsSplitLength) {
			result = content.length() % (smsSplitLength - 3) == 0 ? content.length() / (smsSplitLength - 3) : (content.length() / (smsSplitLength - 3)) + 1;
		}
		return result;
	}

	/** 检验扩展码 */
	public static boolean checkExtendedCode(String extendedCode) {
		if (extendedCode == null || extendedCode.length() > 12) {
			return false;
		}
		try {
			long a = Long.parseLong(extendedCode);
			if (a < 0) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/** 校验seqid */
	public static boolean checkCustomId(String seqid) {
		try {
			long a = Long.parseLong(seqid);
			if (a < 0) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean checkSmsContent(String content) {
		try {
			if (content.getBytes("GBK").length > 4096) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean checkRegistKey(String key) {
		try {
			if (key.length() > 100) {
				return false;
			} else {
				char[] chars = key.toCharArray();
				for (char a : chars) {
					int i = a;
					if (i < 32 || i > 126) {
						return false;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean checkVoiceContent(String content) {
		try {
			if (content.length() < 4 || content.length() > 8) {
				return false;
			} else {
				char[] chars = content.toCharArray();
				for (char a : chars) {
					int i = a;
					if (!((i >= 48 && i <= 57) || (i >= 65 && i <= 90) || (i >= 97 && i <= 122))) {
						return false;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
