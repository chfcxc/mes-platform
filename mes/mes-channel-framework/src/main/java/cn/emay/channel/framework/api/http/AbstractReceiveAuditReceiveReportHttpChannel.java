package cn.emay.channel.framework.api.http;

import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import cn.emay.channel.framework.api.AbstractChannel;
import cn.emay.channel.framework.api.http.receive.ReceiveAudit;
import cn.emay.channel.framework.api.http.receive.ReceiveAuditStateServlet;
import cn.emay.channel.framework.api.http.receive.ReceiveReport;
import cn.emay.channel.framework.api.http.receive.ReceiveReportServlet;
import cn.emay.channel.framework.core.ChannelFrameWork;
import cn.emay.common.Result;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;

/**
 * @项目名称：eucp-fms-channel-framework  @类描述：   @创建人：dejun  
 * 
 * @创建时间：2019年5月5日 下午3:10:38   @修改人：dejun  
 * @修改时间：2019年5月5日 下午3:10:38   @修改备注：
 */
public abstract class AbstractReceiveAuditReceiveReportHttpChannel extends AbstractChannel implements ReceiveReport, ReceiveAudit {

	private static Logger log = Logger.getLogger(AbstractReceiveAuditReceiveReportHttpChannel.class);

	private Server server;

	@Override
	public Result init(FmsChannel mmsChannel, Map<String, String> fmsChannelInfoMap) {
		this.setFmsChannel(mmsChannel);
		this.setFmsChannelInfoMap(fmsChannelInfoMap);
		// 初始化监听状态报告服务
		Integer jettyPort;
		if (fmsChannelInfoMap.get("jettyPort") == null) {
			log.error("通道Id：" + ChannelFrameWork.getInstance().getChannelId() + "未配置回调端口号！");
			return Result.badResult("未配置回调端口号！");
		}
		try {
			jettyPort = Integer.parseInt(fmsChannelInfoMap.get("jettyPort"));
		} catch (Exception e) {
			log.error("通道Id：" + ChannelFrameWork.getInstance().getChannelId() + "端口号配置错误！");
			return Result.badResult("端口号配置错误！");
		}
		server = new Server(jettyPort);
		ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
		servletContextHandler.setContextPath("/");
		servletContextHandler.setMaxFormContentSize(10000000);
		if (fmsChannelInfoMap.get("reportUrl") == null) {
			log.error("通道Id：" + ChannelFrameWork.getInstance().getChannelId() + "未配置状态报告地址！");
			return Result.badResult("未配置状态报告地址！");
		}

		if (fmsChannelInfoMap.get("auditUrl") == null) {
			log.error("通道Id：" + ChannelFrameWork.getInstance().getChannelId() + "未配置状态报告地址！");
			return Result.badResult("未配置推送审核报告地址！");
		}
		servletContextHandler.addServlet(new ServletHolder(new ReceiveReportServlet(this, this.getFmsChannel())), fmsChannelInfoMap.get("reportUrl"));
		servletContextHandler.addServlet(new ServletHolder(new ReceiveAuditStateServlet(this, this.getFmsChannel())), fmsChannelInfoMap.get("auditUrl"));
		server.setHandler(servletContextHandler);
		try {
			server.start();
		} catch (Exception e) {
			log.error("通道Id：" + ChannelFrameWork.getInstance().getChannelId() + "接收状态报告服务或者推送审核报告启动失败", e);
			return Result.badResult("接收状态报告服务或推送审核服务启动失败");
		}
		log.info("接收状态报告服务启动成功，端口为：" + fmsChannelInfoMap.get("jettyPort") + "状态报告地址为：" + fmsChannelInfoMap.get("reportUrl") + "推送审核报告地址为：" + fmsChannelInfoMap.get("auditUrl"));

		return Result.rightResult();
	}

	@Override
	public void start() {
		try {
			// server.start();
			log.info("channel start");
		} catch (Exception e) {
			// this.stop();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void stop() {
		try {
			// server.stop();
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
