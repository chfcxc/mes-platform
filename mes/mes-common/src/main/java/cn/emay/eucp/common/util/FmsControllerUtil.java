package cn.emay.eucp.common.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.db.Page;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateDto;

public class FmsControllerUtil {

	public static void fillServiceCodeId(Page<FmsTemplateDto> page, RedisClient redisClient) {
		List<FmsTemplateDto> fmsTemplateDtos = page.getList();
		if (!fmsTemplateDtos.isEmpty()) {
			Set<String> appIdSet = new HashSet<String>();
			for (FmsTemplateDto dto : fmsTemplateDtos) {
				appIdSet.add(dto.getAppId());
			}
			List<FmsServiceCodeDto> dtos = redisClient.hmget(RedisConstants.FMS_SERVICE_CODE_HASH, FmsServiceCodeDto.class, appIdSet.toArray(new String[appIdSet.size()]));
			Map<String, Long> appIdMap = new HashMap<String, Long>();
			for (FmsServiceCodeDto fmsServiceCodeDto : dtos) {
				appIdMap.put(fmsServiceCodeDto.getAppId(), fmsServiceCodeDto.getId());
			}
			for (FmsTemplateDto dto : fmsTemplateDtos) {
				dto.setServiceCodeId(appIdMap.get(dto.getAppId()));
			}
		}
	}

}
