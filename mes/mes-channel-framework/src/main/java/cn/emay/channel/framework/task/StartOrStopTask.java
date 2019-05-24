package cn.emay.channel.framework.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.emay.channel.framework.core.ChannelFrameWork;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsChannelInfo;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;

/** @项目名称：eucp-fms-channel-framework  @类描述：   @创建人：dejun  
 * 
 * @创建时间：2019年5月6日 下午3:37:20   @修改人：dejun  
 * @修改时间：2019年5月6日 下午3:37:20   @修改备注： */
public class StartOrStopTask implements PeriodTask {
	private static Logger log = Logger.getLogger(StartOrStopTask.class);
	private Long period = 3000L;

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		return doCommand();
	}

	@Override
	public long period() {
		return period;
	}

	public TaskResult doCommand() {
		String command = ChannelFrameWork.getInstance().getService().getCommand(ChannelFrameWork.getInstance().getChannelId());
		try {
			if (command == null) {
				return TaskResult.notDoBusinessResult();
			}
			int state = Integer.parseInt(command);
			FmsChannel fmsChannel = ChannelFrameWork.getInstance().getService().getChannelByStore(ChannelFrameWork.getInstance().getChannelId());
			List<FmsChannelInfo> list = ChannelFrameWork.getInstance().getService().getChannelParamByStore(ChannelFrameWork.getInstance().getChannelId());
			if (FmsChannel.CHANNEL_STATE_ON == state) {
				if (ChannelFrameWork.getInstance().isStart()) {
					ChannelFrameWork.getInstance().getService().delCommand(fmsChannel.getId());
					return TaskResult.notDoBusinessResult();
				}
				log.info("启动通道开始");
				reloadChannel(fmsChannel, list);
				ChannelFrameWork.getInstance().start();
				log.info("启动通道成功");
				ChannelFrameWork.getInstance().getService().delCommand(fmsChannel.getId());
			} else if (FmsChannel.CHANNEL_STATE_OFF == state) {
				if (!ChannelFrameWork.getInstance().isStart()) {
					ChannelFrameWork.getInstance().getService().delCommand(fmsChannel.getId());
					return TaskResult.notDoBusinessResult();
				}
				log.info("停止通道开始");
				reloadChannel(fmsChannel, list);
				ChannelFrameWork.getInstance().stop();
				log.info("停止通道成功");
				ChannelFrameWork.getInstance().getService().delCommand(fmsChannel.getId());
			}
			return TaskResult.doBusinessSuccessResult();
		} catch (Exception e) {
			period = 5000L;
			e.printStackTrace();
			return TaskResult.doBusinessFailResult();
		}
	}

	private void reloadChannel(FmsChannel fmsChannel, List<FmsChannelInfo> list) {
		Map<String, String> map = new HashMap<String, String>();
		if (list != null && list.size() > 0) {
			for (FmsChannelInfo fmsChannelParam : list) {
				map.put(fmsChannelParam.getPropertieyName(), fmsChannelParam.getPropertieyValue());
			}
		}
		ChannelFrameWork.getInstance().reloadChannel(fmsChannel, map);
	}
}
