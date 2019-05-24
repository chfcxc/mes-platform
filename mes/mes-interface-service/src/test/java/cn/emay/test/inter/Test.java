package cn.emay.test.inter;

import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.serviceCode.FmsServiceCodeDto;
import cn.emay.eucp.common.moudle.db.fms.FmsServiceCodeParam;
import cn.emay.mis.core.Main;
import cn.emay.mis.support.MicroServiceSupport;
import cn.emay.store.redis.RedisClient;

public class Test {

	// appId
	static String appId = "EUCP-EMY-FMS1-E9BQE";
	// 密钥
	static String secretKey = "6636933421A24EB0";

	public static void main(String[] args) {
		Main.main(args);

		RedisClient redis = MicroServiceSupport.getRedisClient("Redis");
		initParams(redis);
	}

	private static void initParams(RedisClient redis) {
		FmsServiceCodeDto dto = new FmsServiceCodeDto();
		dto.setAppId(appId);
		dto.setState(0);
		dto.setSecretKey(secretKey);
		dto.setIpConfiguration("127.0.0.1");
		redis.hset(RedisConstants.FMS_SERVICE_CODE_HASH, appId, dto, -1);
		FmsServiceCodeParam param = new FmsServiceCodeParam();
		param.setAppId(appId);
		param.setGetReportType(FmsServiceCodeParam.NOT_GET_REPORT_TYPE);
		param.setIpConfiguration("127.0.0.1");
		redis.hset(RedisConstants.FMS_SERVICE_CODE_PARAM_HASH, appId, param, -1);

	}
}
