package cn.emay.channel.framework.task;

import java.util.Map;

import org.apache.log4j.Logger;

import cn.emay.channel.framework.core.ChannelFrameWork;
import cn.emay.task.core.common.TaskResult;
import cn.emay.task.core.define.PeriodTask;

/** @项目名称：eucp-fms-channel-framework  @类描述：   @创建人：dejun  
 * 
 * @创建时间：2019年4月24日 下午5:01:56   @修改人：dejun  
 * @修改时间：2019年4月24日 下午5:01:56   @修改备注： */
public class HeartBeatTask implements PeriodTask {
	private static Logger log = Logger.getLogger(HeartBeatTask.class);
	Long period = 3000L;

	@Override
	public TaskResult exec(Map<String, String> initParams) {
		return heartBeat();
	}

	public TaskResult heartBeat() {
		log.debug("HeartBeatTask begin");
		ChannelFrameWork.getInstance().getService().heartBeat();
		return TaskResult.doBusinessSuccessResult();
	}

	@Override
	public long period() {
		return period;
	}

}
