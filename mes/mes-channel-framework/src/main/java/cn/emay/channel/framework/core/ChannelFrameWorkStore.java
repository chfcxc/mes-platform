package cn.emay.channel.framework.core;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.google.gson.reflect.TypeToken;

import cn.emay.channel.framework.dto.WaitReportDTO;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.constant.FileConstants;
import cn.emay.eucp.common.constant.RedisConstants;
import cn.emay.eucp.common.dto.fms.mt.FmsSendDto;
import cn.emay.eucp.common.dto.report.FmsReportDTO;
import cn.emay.eucp.common.dto.report.UpdateFmsDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsChannelInfo;
import cn.emay.filestore.map.FileMap;
import cn.emay.filestore.queue.FileQueue;

/** @author 通道数据存取服务 */
public class ChannelFrameWorkStore {

	private Map<String, FileQueue> emayQueues = new ConcurrentHashMap<String, FileQueue>();

	private Map<String, FileMap> maps = new ConcurrentHashMap<String, FileMap>();

	final long syncPeriod = 5000L;

	private RedisClient redis;

	private String fileStorePath;

	private JdbcTemplate jdbcTemplate;

	public ChannelFrameWorkStore(RedisClient redis, DataSource dataSource, String fileStorePath) {
		this.redis = redis;
		this.fileStorePath = fileStorePath;
		if (dataSource != null) {
			this.setJdbcTemplate(new JdbcTemplate(dataSource));
		}
	}

	public FileQueue getFqQueue(String queueName) {
		if (emayQueues.containsKey(queueName)) {
			return emayQueues.get(queueName);
		}
		FileQueue fileQueue = null;
		try {
			fileQueue = new FileQueue(new File(fileStorePath, queueName).getAbsolutePath(), syncPeriod, 20L * 1024L * 1024L);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null != fileQueue) {
			emayQueues.put(queueName, fileQueue);
		}
		return fileQueue;
	}

	public FileMap getFqHash(String hashName) {
		if (maps.containsKey(fileStorePath + File.separator + hashName)) {
			return maps.get(fileStorePath + File.separator + hashName);
		}
		FileMap fileMap = null;
		try {
			fileMap = new FileMap(fileStorePath + File.separator + hashName, syncPeriod, 20L * 1024L * 1024L);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (null != fileMap) {
			maps.put(fileStorePath + File.separator + hashName, fileMap);
		}
		return fileMap;
	}

	public void putHeartbeatHash(String key, String json) {
		FileMap fileMap = getFqHash(RedisConstants.FMS_CHANNEL_HEARTBEAT_HASH);
		try {
			fileMap.put(RedisConstants.FMS_CHANNEL_HEARTBEAT_HASH, json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void heartBeat() {
		redis.hset(RedisConstants.FMS_CHANNEL_HEARTBEAT_HASH, ChannelFrameWork.getInstance().getFmsChannel().getId().toString(), System.currentTimeMillis(), -1);
	}

	public String getCommand(Long id) {
		return redis.hget(RedisConstants.FMS_CHANNEL_COMMAND_HASH, id.toString());
	}

	public List<WaitReportDTO> getAllWaitReportByVoiceIds(String[] strings) {
		return redis.hmget(RedisConstants.FMS_WAIT_REPORT_HASH, WaitReportDTO.class, strings);
	}

	public FmsSendDto getSendData(String key) {
		FmsSendDto dto = redis.rpop(key, FmsSendDto.class);
		return dto;
	}

	public String getWaitCompareReportQueue() {
		FileQueue fileQueue = getFqQueue(FileConstants.FMS_CHANNEL_REPORT_FQ_QUEUE + ChannelFrameWork.getInstance().getFmsChannel().getId().toString());
		try {
			return fileQueue.poll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public FmsChannel getChannel(Long currentChannelId) {
		return redis.hget(RedisConstants.FMS_CHANNEL_HASH, currentChannelId.toString(), FmsChannel.class);
	}

	public List<FmsChannelInfo> getChannelParam(Long currentChannelId) {
		String json = redis.hget(RedisConstants.FMS_CHANNEL_PARAM_HASH, currentChannelId.toString());
		if (json != null) {
			return JsonHelper.fromJson(new TypeToken<List<FmsChannelInfo>>() {
			}, json);
		}
		return null;
	}

	public void delCommand(Long id) {
		redis.hdel(RedisConstants.FMS_CHANNEL_COMMAND_HASH, id.toString());
		return;
	}

	public void sendEnd(FmsChannel mmsChannel, List<FmsSendDto> successList, List<UpdateFmsDTO> updateList, List<FmsSendDto> sendList, Map<String, List<FmsReportDTO>> reportMap,
			Map<String, Double> zestMap, Map<String, Object> hasResponseMap) {
		// 成功数据删除
		if (successList.size() > 0) {
			for (FmsSendDto sendDto : successList) {
				sendList.remove(sendDto);
			}
			successList.clear();
		}
		if (updateList.size() > 0) {
			redis.lpush(RedisConstants.FMS_UPDATE_DB_QUEUE, -1, updateList.toArray());
			updateList.clear();
		}
		if (reportMap.size() > 0) {
			for (Entry<String, List<FmsReportDTO>> entry : reportMap.entrySet()) {
				redis.lpush(RedisConstants.FMS_REPORT_QUEUE + entry.getKey(), -1, entry.getValue().toArray());
			}
			reportMap.clear();
		}
		if (hasResponseMap.size() > 0) {
			redis.hmset(RedisConstants.FMS_WAIT_REPORT_HASH + mmsChannel.getId(), hasResponseMap, -1);
			hasResponseMap.clear();
		}
		if (zestMap.size() > 0) {
			redis.zadd(RedisConstants.FMS_WAIT_REPORT_ZSET + mmsChannel.getId(), zestMap);
			zestMap.clear();
		}
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
}
