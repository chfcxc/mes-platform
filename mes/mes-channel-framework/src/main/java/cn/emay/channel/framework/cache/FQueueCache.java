package cn.emay.channel.framework.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import cn.emay.filestore.queue.FileQueue;

public class FQueueCache {

	private static Logger logger = Logger.getLogger(FQueueCache.class);

	private static Map<String, FileQueue> FQueueMap = new ConcurrentHashMap<String, FileQueue>();

	public static void putFqueue(String key, FileQueue fq) {
		FQueueMap.put(key, fq);
	}

	public static String getValue(String key) {
		String b = "";
		try {
			b = FQueueMap.get(key).poll();
		} catch (Exception e) {
			logger.error("[FQueueCache] getValue exception!", e);
			return null;
		}
		return b;
	}

	public static void putValue(String key, String value) {
		if (value != null && !value.equals("")) {
			try {
				FQueueMap.get(key).offer(value);
			} catch (Exception e) {
				logger.error("[FQueueCache] putValue exception!", e);
			}
		}
	}

	public static int getSize(String key) {
		if (key == null || key.equals("")) {
			return 0;
		} else {
			return FQueueMap.get(key).size();
		}
	}

}
