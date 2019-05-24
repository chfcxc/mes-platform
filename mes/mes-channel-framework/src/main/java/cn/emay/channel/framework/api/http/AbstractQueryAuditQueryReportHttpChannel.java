package cn.emay.channel.framework.api.http;

import java.util.Map;

import org.apache.log4j.Logger;

import cn.emay.channel.framework.api.AbstractChannel;
import cn.emay.channel.framework.api.http.query.QueryAudit;
import cn.emay.channel.framework.api.http.query.QueryReport;
import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;

/**
 * @项目名称：eucp-fms-channel-framework  @类描述：   @创建人：dejun  
 * 
 * @创建时间：2019年5月5日 下午3:10:38   @修改人：dejun  
 * @修改时间：2019年5月5日 下午3:10:38   @修改备注：
 */
public abstract class AbstractQueryAuditQueryReportHttpChannel extends AbstractChannel implements QueryReport, QueryAudit {

	private static Logger log = Logger.getLogger(AbstractQueryAuditQueryReportHttpChannel.class);

	@Override
	public Result init(FmsChannel mmsChannel, Map<String, String> fmsChannelInfoMap) {
		this.setFmsChannel(mmsChannel);
		this.setFmsChannelInfoMap(fmsChannelInfoMap);
		return Result.rightResult();
	}

	@Override
	public void start() {
		try {
			log.info("channel start");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void stop() {
		try {
			log.info("channel stop");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void reload(FmsChannel mmsChannel, Map<String, String> fmsChannelInfoMap) {
		this.setFmsChannel(mmsChannel);
		this.setFmsChannelInfoMap(fmsChannelInfoMap);
	}

}
