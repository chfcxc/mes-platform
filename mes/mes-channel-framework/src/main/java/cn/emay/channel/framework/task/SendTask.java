package cn.emay.channel.framework.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.emay.channel.framework.core.ChannelFrameWork;
import cn.emay.common.Result;
import cn.emay.eucp.common.constant.FmsGlobalConstants;
import cn.emay.eucp.common.dto.fms.mt.FmsSendDto;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.ConcurrentPeriodTask;

public class SendTask implements ConcurrentPeriodTask {

	private static Logger log = Logger.getLogger(SendTask.class);

	long period = 10L;

	private int threadSize = 1;

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		long startTime = System.currentTimeMillis();
		TaskResult tr = sendTask();
		long endTime = System.currentTimeMillis();
		if (endTime - startTime < 1000) {
			period = 1000 - (endTime - startTime);
		}
		return tr;
	}

	@Override
	public int needConcurrent(int alive, int min, int max) {
		int num = ChannelFrameWork.getInstance().getchannel().needConcurrent(alive, min, max);
		if (num > 0) {
			if (num > max) {
				threadSize = max;
			} else {
				threadSize = num;
			}
		} else {
			threadSize = 1;
		}
		return threadSize;
	}

	@Override
	public long period() {
		return period;
	}

	public TaskResult sendTask() {
		FmsChannel fmsChannel = ChannelFrameWork.getInstance().getFmsChannel();
		// if (FmsChannel.CHANNEL_STATE_ON != fmsChannel.getState()) {
		// log.error("闪推发送服务暂停-----通道状态值" + fmsChannel.getState() + "关闭");
		// return TaskResult.notDoBusinessResult();
		// }
		// 取数据
		int speed = 1;
		int num = fmsChannel.getSendSpeed() / threadSize;
		if (num > 0) {
			speed = num;
		}
		List<FmsSendDto> sendList = getSendData(speed, fmsChannel.getId());
		if (null != sendList && sendList.size() > 0) {
			try {
				Result result = ChannelFrameWork.getInstance().getchannel().send(sendList);
				if (!result.getSuccess()) {
					log.error("闪推短信发送失败");
				}
			} catch (Exception e) {
				log.error("闪推短信发送异常", e);
				return TaskResult.doBusinessFailResult();
			}

		} else {
			return TaskResult.notDoBusinessResult();
		}
		return TaskResult.doBusinessSuccessResult();
	}

	private List<FmsSendDto> getSendData(int num, Long channelId) {
		List<FmsSendDto> list = new ArrayList<FmsSendDto>();
		for (String s : FmsGlobalConstants.CHANNELS) {
			for (int i = 0; i < num; i++) {
				FmsSendDto dto = ChannelFrameWork.getInstance().getService().getSendData(s + channelId);
				if (null != dto) {
					list.add(dto);
				} else {
					break;
				}
			}
		}
		return list;
	}
}
