package cn.emay.eucp.task.multiple.fms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.spring.BeanFactoryUtils;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.moudle.db.fms.FmsAccount;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCode;
import cn.emay.eucp.data.service.fms.FmsAccountService;
import cn.emay.eucp.task.multiple.constant.CacheConstant;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;

public class ServiceCodeBalanceUpdateTask implements PeriodTask {
	private Logger logger = Logger.getLogger(ServiceCodeBalanceUpdateTask.class);
	private static RedisClient redis = BeanFactoryUtils.getBean(RedisClient.class);
	private static FmsAccountService fmsAccountService = BeanFactoryUtils.getBean(FmsAccountService.class);

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		logger.info("服务号余额更新任务开始");
		long millis = System.currentTimeMillis();
		try {
			Map<String, FmsServiceCode> map = CacheConstant.serviceCodeMap;
			Map<String, Long> appIdMap = new HashMap<String, Long>();
			List<FmsAccount> updateList = new ArrayList<FmsAccount>();
			for (FmsServiceCode dto : map.values()) {
				appIdMap.put(dto.getAppId(), dto.getId());
				if (appIdMap.size() == 1000) {
					updateBalance(appIdMap, updateList);
				}
			}
			if (appIdMap.size() > 0) {
				updateBalance(appIdMap, updateList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("服务号余额更新任务结束，耗时:" + (System.currentTimeMillis() - millis) + "ms");
		return TaskResult.doBusinessSuccessResult();
	}

	private void updateBalance(Map<String, Long> appIdMap, List<FmsAccount> updateList) {
		List<String> list = new ArrayList<String>(appIdMap.keySet());
		List<String> balanceList = redis.hmget(RedisConstants.FMS_SERVICE_CODE_BALANCE_HASH, list.toArray(new String[list.size()]));
		for (int i = 0; i < list.size(); i++) {
			String appId = list.get(i);
			String balanceString = balanceList.get(i);
			if (null == balanceString) {
				continue;
			}
			FmsAccount account = new FmsAccount();
			account.setServiceCodeId(appIdMap.get(appId));
			Long balance = Long.valueOf(balanceString);
			account.setBalance(balance);
			updateList.add(account);
		}
		fmsAccountService.updateBalance(updateList);
		updateList.clear();
		appIdMap.clear();
	}

	@Override
	public long period() {
		return 60 * 1000L;
	}

}
