package cn.emay.eucp.common.util;

public class RedisShardingUtils {

	public final static int SMS_SINGLE_REDIS_SHARDING_NUM = 8;
	public final static int SMS_BATCH_REDIS_SHARDING_NUM = 6;

	public final static int SMS_HASH_REDIS_SHARDING_NUM = 6;

	public final static int SMS_TIMER_REDIS_SHARDING_NUM = 8;

	public static long getSinglePositionNumber(String batchNo) {
		try {
			long number = Long.parseLong(batchNo.substring(7, batchNo.length() - 3)) % SMS_SINGLE_REDIS_SHARDING_NUM + 1;
			return number;
		} catch (Exception e) {
			return 1;
		}
	}

	public static long getBatchPositionNumber(String batchNo) {
		try {
			long number = Long.parseLong(batchNo.substring(7, batchNo.length() - 3)) % SMS_BATCH_REDIS_SHARDING_NUM + 1;
			return number;
		} catch (Exception e) {
			return 1;
		}
	}

	public static long getPositionNumber(String batchNo) {
		try {
			long number = Long.parseLong(batchNo.substring(7, batchNo.length() - 3)) % SMS_HASH_REDIS_SHARDING_NUM + 1;
			return number;
		} catch (Exception e) {
			return 1;
		}
	}

	public static long getTimerPositionNumber(String batchNo) {
		try {
			long number = Long.parseLong(batchNo.substring(7, batchNo.length() - 3)) % SMS_TIMER_REDIS_SHARDING_NUM + 1;
			return number;
		} catch (Exception e) {
			return 1;
		}
	}
}
