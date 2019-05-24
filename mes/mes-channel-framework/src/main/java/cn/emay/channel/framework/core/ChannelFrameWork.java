package cn.emay.channel.framework.core;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import cn.emay.channel.framework.api.AbstractChannel;
import cn.emay.common.Result;
import cn.emay.common.cache.redis.RedisClient;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.task.core.TaskScheduler;

/** @author dejun 通道框架 */
public class ChannelFrameWork {

	private Logger log = Logger.getLogger(ChannelFrameWork.class);

	private static ChannelFrameWork cfw = new ChannelFrameWork();

	private ChannelFrameWork() {
		super();
	}

	public static ChannelFrameWork getInstance() {
		return cfw;
	}

	TaskScheduler scheduler;

	private ChannelFrameWorkService service;

	private Long channelId;

	// 通道实例
	private AbstractChannel channel;

	private boolean isStart;

	public void init(RedisClient redis, DataSource dataSource, String fileStorePath, Long channelId, AbstractChannel channel) {
		this.service = new ChannelFrameWorkService(new ChannelFrameWorkStore(redis, dataSource, fileStorePath));
		this.channelId = channelId;
		this.channel = channel;
	}

	public void initChannel(FmsChannel mmsChannel, Map<String, String> map) {
		Result result = this.channel.init(mmsChannel, map);
		if (!result.getSuccess()) {
			throw new RuntimeException(result.getMessage());
		}
	}

	public void reloadChannel(FmsChannel mmsChannel, Map<String, String> map) {
		this.channel.reload(mmsChannel, map);
	}

	public synchronized void start() {
		log.info("ChannelFrameWork begin start ...");
		isStart = true;
	}

	public synchronized void stop() {
		log.info("ChannelFrameWork begin stop ...");
		try {
			isStart = false;
		} catch (Exception e) {
			throw new IllegalArgumentException("停止异常！");
		}
		log.info("ChannelFrameWork stoped ");

	}

	public ChannelFrameWorkService getService() {
		return service;
	}

	public AbstractChannel getchannel() {
		return channel;
	}

	public FmsChannel getFmsChannel() {
		return channel.getFmsChannel();
	}

	public Map<String, String> getFmsChannelParamMap() {
		return channel.getFmsChannelInfoMap();
	}

	public Long getChannelId() {
		return channelId;
	}

	public boolean isStart() {
		return isStart;
	}
}
