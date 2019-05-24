package cn.emay.eucp.inter.http.utils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.inter.framework.constant.EucpInterFmsReponseCode;
import cn.emay.eucp.inter.framework.core.FmsInterfaceFrameWork;
import cn.emay.eucp.inter.http.constant.HttpInterfaceConstant;
import cn.emay.eucp.inter.http.dto.CheckResult;
import cn.emay.eucp.inter.http.dto.request.FmsBaseRequest;
import cn.emay.json.JsonHelper;

public class CheckUtils {

	// 限速缓存
	private static Map<String, Set<Long>> limitMap = new ConcurrentHashMap<String, Set<Long>>();

	/** 数据基础校验
	 * 
	 * @param datajson
	 * @param clazz
	 * @return */
	public static <T extends FmsBaseRequest> CheckResult<T> checkData(String datajson, Class<T> clazz) {
		if (datajson == null) {
			return new CheckResult<T>(EucpInterFmsReponseCode.ERROR_PARAMS, null, "pamras error");
		}
		T parmas = JsonHelper.fromJson(clazz, datajson);
		if (parmas == null) {
			return new CheckResult<T>(EucpInterFmsReponseCode.ERROR_PARAMS, null, "pamras error");
		}
		return new CheckResult<T>(EucpInterFmsReponseCode.SUCCESS, parmas, null);
	}

	/**
	 * 基础信息校验
	 * 
	 * @param appId
	 * @param remoteIp
	 * @param sign
	 * @param timestamp
	 * @return
	 */
	public static CheckResult<FmsServiceCodeDto> checkBase(String appId, String remoteIp, Long requestTime, Integer requestValidPeriod, String speedSign) {
		// 检验服务号信息
		if (appId == null || appId.equals("")) {
			return new CheckResult<FmsServiceCodeDto>(EucpInterFmsReponseCode.ERROR_APPID, null, "appid empty");
		}
		FmsServiceCodeDto serviceCode = FmsInterfaceFrameWork.getService().getServiceCodeByAppId(appId);
		if (serviceCode == null || serviceCode.getState().intValue() == FmsServiceCode.STATE_DISABLE) {
			return new CheckResult<FmsServiceCodeDto>(EucpInterFmsReponseCode.ERROR_APPID, null, "appid error");
		}

		// 判断超速
		if (speedSign != null) {
			if (isOverSpeed(serviceCode.getAppId(), speedSign)) {
				return new CheckResult<FmsServiceCodeDto>(EucpInterFmsReponseCode.ERROR_OVER_SPEED, serviceCode, "over speed");
			}
		}

		// 检验IP信息
		if (remoteIp == null) {
			return new CheckResult<FmsServiceCodeDto>(EucpInterFmsReponseCode.ERROR_CLIENT_IP, serviceCode, "ip empty");
		}
		if (!isAllowCustomIp(serviceCode.getIpConfiguration(), remoteIp)) {
			return new CheckResult<FmsServiceCodeDto>(EucpInterFmsReponseCode.ERROR_CLIENT_IP, serviceCode, "ip error");
		}
		if (requestTime == null || requestTime.intValue() == 0) {
			return new CheckResult<FmsServiceCodeDto>(EucpInterFmsReponseCode.ERROR_REQUESTTIME, null, "requestTime empty");
		}
		if (requestValidPeriod == null || requestValidPeriod.intValue() == 0) {
			return new CheckResult<FmsServiceCodeDto>(EucpInterFmsReponseCode.ERROR_REQUESTVALIDPERIOD, null, "requestValidPeriod empty");
		}
		long time = requestTime + requestValidPeriod * 1000;
		if (time < System.currentTimeMillis()) {
			return new CheckResult<FmsServiceCodeDto>(EucpInterFmsReponseCode.ERROR_REQUESTTIME_TIMEOUT, null, "request time out");
		}
		return new CheckResult<FmsServiceCodeDto>(EucpInterFmsReponseCode.SUCCESS, serviceCode, null);
	}

	/**
	 * 基础信息及签名校验
	 * 
	 * @param appId
	 * @param remoteIp
	 * @param sign
	 * @param timestamp
	 * @return
	 */
	public static CheckResult<FmsServiceCodeDto> checkBaseAndSign(String appId, String remoteIp, String speedSign) {
		// 检验服务号信息
		if (appId == null || appId.equals("")) {
			return new CheckResult<FmsServiceCodeDto>(EucpInterFmsReponseCode.ERROR_APPID, null, "appid empty");
		}
		FmsServiceCodeDto serviceCode = FmsInterfaceFrameWork.getService().getServiceCodeByAppId(appId);
		if (serviceCode == null || serviceCode.getState().intValue() == FmsServiceCode.STATE_DISABLE) {
			return new CheckResult<FmsServiceCodeDto>(EucpInterFmsReponseCode.ERROR_APPID, null, "appid error");
		}

		// 判断超速
		if (speedSign != null) {
			if (isOverSpeed(serviceCode.getAppId(), speedSign)) {
				return new CheckResult<FmsServiceCodeDto>(EucpInterFmsReponseCode.ERROR_OVER_SPEED, serviceCode, "over speed");
			}
		}
		// 检验IP信息
		if (remoteIp == null) {
			return new CheckResult<FmsServiceCodeDto>(EucpInterFmsReponseCode.ERROR_CLIENT_IP, serviceCode, "ip empty");
		}
		if (!isAllowCustomIp(serviceCode.getIpConfiguration(), remoteIp)) {
			return new CheckResult<FmsServiceCodeDto>(EucpInterFmsReponseCode.ERROR_CLIENT_IP, serviceCode, "ip error");
		}

		return new CheckResult<FmsServiceCodeDto>(EucpInterFmsReponseCode.SUCCESS, serviceCode, null);
	}

	public static boolean checkMobile(String mobile) {
		if (null == mobile || "".equals(mobile)) {
			return false;
		}
		String regExp = "^1\\d{10}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(mobile);
		return m.matches();
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

	public static CheckResult<Object> check(Long requestTime, Integer requestValidPeriod) {
		if (requestTime == null || requestTime.intValue() == 0) {
			return new CheckResult<Object>(EucpInterFmsReponseCode.ERROR_REQUESTTIME, null, "requestTime empty");
		}
		if (requestValidPeriod == null || requestValidPeriod.intValue() == 0) {
			return new CheckResult<Object>(EucpInterFmsReponseCode.ERROR_REQUESTVALIDPERIOD, null, "requestValidPeriod empty");
		}
		long time = requestTime + requestValidPeriod * 1000;
		if (time < System.currentTimeMillis()) {
			return new CheckResult<Object>(EucpInterFmsReponseCode.ERROR_REQUESTTIME_TIMEOUT, null, "request time out");
		}
		return new CheckResult<Object>(EucpInterFmsReponseCode.SUCCESS, null, null);
	}

	/**
	 * 请求是否超速
	 */
	public static boolean isOverSpeed(String appId, String type) {
		long now = System.currentTimeMillis();
		boolean isOverSpeed = false;
		Integer interval = 0;
		Integer number = 0;
		if ("template".equals(type)) {
			interval = HttpInterfaceConstant.TEMPLATE_LIMIT_INTERVAL;
			number = HttpInterfaceConstant.TEMPLATE_LIMIT_NUM;
		} else if ("report".equals(type)) {
			interval = HttpInterfaceConstant.REPORT_LIMIT_INTERVAL;
			number = HttpInterfaceConstant.REPORT_LIMIT_NUM;
		} else if ("balance".equals(type)) {
			interval = HttpInterfaceConstant.BALANCE_LIMIT_INTERVAL;
			number = HttpInterfaceConstant.BALANCE_LIMIT_NUM;
		} else {
			isOverSpeed = true;
			return isOverSpeed;
		}
		if (interval == null || interval < 0) {
			return isOverSpeed;
		}
		if (number == null || number < 0) {
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
}
