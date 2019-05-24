package cn.emay.channel.framework.api;

import java.util.List;
import java.util.Map;

import cn.emay.common.Result;
import cn.emay.eucp.common.dto.fms.mt.FmsSendDto;
import cn.emay.eucp.common.dto.fms.template.ReportFmsTemplateDto;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;

/** @author 通道抽象类 */
public abstract class AbstractChannel {

	private FmsChannel fmsChannel;

	private Map<String, String> fmsChannelInfoMap;

	public abstract Result init(FmsChannel ussdChannel, Map<String, String> ussdChannelInfoMap);

	public abstract void reload(FmsChannel fmsChannel, Map<String, String> map);

	public abstract void start();

	public abstract void stop();

	public abstract long period();

	public abstract Result send(List<FmsSendDto> task);

	public abstract Result sendAudit(ReportFmsTemplateDto reportFmsTemplateDto);

	public abstract int needConcurrent(int aliveNodeNumber, int min, int maxNodeNumber);

	public FmsChannel getFmsChannel() {
		return fmsChannel;
	}

	public void setFmsChannel(FmsChannel fmsChannel) {
		this.fmsChannel = fmsChannel;
	}

	public Map<String, String> getFmsChannelInfoMap() {
		return fmsChannelInfoMap;
	}

	public void setFmsChannelInfoMap(Map<String, String> fmsChannelInfoMap) {
		this.fmsChannelInfoMap = fmsChannelInfoMap;
	}

}
