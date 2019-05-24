package cn.emay.channel.framework.core;

import java.util.List;
import java.util.Map;

import cn.emay.channel.framework.dto.ChannelReportDTO;
import cn.emay.channel.framework.dto.WaitReportDTO;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.dto.fms.mt.FmsSendDto;
import cn.emay.eucp.common.dto.report.FmsReportDTO;
import cn.emay.eucp.common.dto.report.UpdateFmsDTO;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;
import cn.emay.eucp.common.moudle.db.fms.FmsChannelInfo;

/** @author 通道服务 */
public class ChannelFrameWorkService {

	private ChannelFrameWorkStore store;

	public ChannelFrameWorkService(ChannelFrameWorkStore store) {
		this.store = store;
	}

	/** 检查心跳 */
	public void heartBeat() {
		store.heartBeat();
	}

	/** 获取通道指令
	 *
	 * @param id
	 * @return 指令 */
	public String getCommand(Long id) {
		if (id == null) {
			return null;
		}
		return store.getCommand(id);
	}

	/** 获取通道指令
	 *
	 * @param id
	 * @return 指令 */
	public void delCommand(Long id) {
		if (id == null) {
			return;
		}
		store.delCommand(id);
		return;
	}

	public void saveReport() {
	}

	// public void pushReport(ChannelReportDTO report) {
	// }
	//
	// public void saveChannelReport(ChannelReportDTO taskReportDTO) {
	// store.saveChannelReport(taskReportDTO);
	// }
	//
	// public void saveChannelReportInDB(ImsChannelReportInfo taskReportDTO) {
	// store.saveChannelReportInDB(taskReportDTO);
	// }

	/** 取出文件队列中的上游状态报告
	 *
	 * @param currentChannelId
	 * @return */
	// public String popChannelFqReport(Long currentChannelId) {
	// return store.popChannelFqReport(currentChannelId);
	// }

	/** 保存上游返回来的状态报告
	 *
	 * @param currentChannelId
	 * @param json
	 */
	// public void saveChannelFqReport(Long currentChannelId, String json) {
	// store.saveChannelFqReport(currentChannelId, json);
	// }

	/** 保存需要主动查询的订单 文件队列
	 *
	 * @param currentChannelId
	 * @param json
	 */
	// public void saveQueryFq(Long currentChannelId, String json) {
	// store.saveQueryFq(currentChannelId, json);
	// }

	/** 存储到给下游推送状态报告的队列
	 *
	 * @param report
	 */
	// public void saveClientReprot(FlowTaskRedisDTO report) {
	// store.putClientReportHash(report);
	// }
	//
	// public void updateTask(FlowTaskRedisDTO task) {
	// store.updateTask(task);
	// }
	//
	// public void saveFlowQuery(QueryResponse res) {
	// store.saveFlowQuery(res);
	// }
	//
	// public void saveFail(FlowTaskRedisDTO task) {
	// store.saveFail(task);
	// }
	//
	// public void saveWaitCompareTask(String taskNo, FlowTaskRedisDTO task) {
	// store.saveWaitCompareTask(taskNo, task);
	// }
	//
	// public FlowTaskRedisDTO getWaitCompareTask(String taskNo) {
	// return JsonHelper.fromJson(FlowTaskRedisDTO.class, store.getWaitCompareTask(taskNo));
	// }

	// public void delWaitCompareTask(String taskNo) {
	// store.delWaitCompareTask(taskNo);
	// }
	//
	// public String findHandHash(String taskNo) {
	// return store.findHandHash(taskNo);
	// }
	//
	// public void delHandHash(String taskNo) {
	// store.delHandHash(taskNo);
	// }

	public ChannelReportDTO getWaitCompareReportQueue() {
		return JsonHelper.fromJson(ChannelReportDTO.class, store.getWaitCompareReportQueue());
	}

	// public List<ChannelReportDTO> getWaitCompareReportQueue(int num) {
	// List<ChannelReportDTO> list = new ArrayList<ChannelReportDTO>();
	// for (String s : store.getWaitCompareReportQueue(num)) {
	// list.add(JsonHelper.fromJson(ChannelReportDTO.class, s));
	// }
	// return list;
	// }

	public FmsChannel getChannelByStore(Long currentChannelId) {
		return store.getChannel(currentChannelId);
	}

	public List<FmsChannelInfo> getChannelParamByStore(Long currentChannelId) {
		return store.getChannelParam(currentChannelId);
	}

	public void saveFmsReport(String appId, FmsReportDTO buildReportDTO) {

	}

	public FmsSendDto getSendData(String key) {
		return store.getSendData(key);
	}

	public List<WaitReportDTO> getAllWaitReportByImsIds(String[] strings) {
		return null;
	}

	public void saveUpdateImsState(List<UpdateFmsDTO> updateFmsDTOs) {

	}

	public void saveChannelFqReport(Long channelId, List<ChannelReportDTO> reports) {

	}

	public void sendEnd(FmsChannel fmsChannel, List<FmsSendDto> successList, List<UpdateFmsDTO> updateList, List<FmsSendDto> sendList, Map<String, List<FmsReportDTO>> reportMap,
			Map<String, Double> zestMap, Map<String, Object> hasResponseMap) {
		store.sendEnd(fmsChannel, successList, updateList, sendList, reportMap, zestMap, hasResponseMap);
	}

}
