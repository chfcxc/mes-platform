package cn.emay.channel.framework.api.http.receive;

import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.emay.channel.framework.cache.FQueueCache;
import cn.emay.channel.framework.dto.Constants;
import cn.emay.channel.framework.dto.FmsChannelReport;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;

public class ReceiveReportServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ReceiveReportServlet.class);

	private ReceiveReport receive;

	private FmsChannel fmsChannel;

	public ReceiveReportServlet(ReceiveReport receive, FmsChannel fmsChannel) {
		this.setReceive(receive);
		this.setFmsChannel(fmsChannel);
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) {
		// 接收到解析
		try {
			List<FmsChannelReport> reports = receive.receiveReports(request, response);
			// 存储
			if (reports != null && reports.size() > 0) {
				for (FmsChannelReport report : reports) {
					String json = JsonHelper.toJsonString(report);
					FQueueCache.putValue(Constants.REPORTQUEUE, json);
				}
			}
		} catch (Exception e) {
			log.error("ReceiveReportServlet被动接收状态报告异常！", e);
		}
		receive.buildResponse(request, response);
	}

	public ReceiveReport getReceive() {
		return receive;
	}

	public void setReceive(ReceiveReport receive) {
		this.receive = receive;
	}

	public FmsChannel getFmsChannel() {
		return fmsChannel;
	}

	public void setFmsChannel(FmsChannel fmsChannel) {
		this.fmsChannel = fmsChannel;
	}

}