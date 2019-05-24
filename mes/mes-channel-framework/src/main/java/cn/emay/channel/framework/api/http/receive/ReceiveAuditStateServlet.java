package cn.emay.channel.framework.api.http.receive;

import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.emay.channel.framework.cache.FQueueCache;
import cn.emay.channel.framework.dto.Constants;
import cn.emay.common.json.JsonHelper;
import cn.emay.eucp.common.dto.fms.template.FmsTemplateChannelResponseDto;
import cn.emay.eucp.common.moudle.db.fms.FmsChannel;

public class ReceiveAuditStateServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(ReceiveAuditStateServlet.class);

	private ReceiveAudit receive;
	private FmsChannel fmsChannel;

	public ReceiveAuditStateServlet(ReceiveAudit receive, FmsChannel fmsChannel) {
		this.setReceive(receive);
		this.setFmsChannel(fmsChannel);
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) {
		// 接收到解析
		try {
			List<FmsTemplateChannelResponseDto> auditCheck = receive.receiveAudit(request, response);
			if (null != auditCheck && auditCheck.size() > 0) {
				for (FmsTemplateChannelResponseDto fmsTemplateChannelResponseDto : auditCheck) {
					String json = JsonHelper.toJsonString(fmsTemplateChannelResponseDto);
					FQueueCache.putValue(Constants.AUDITREPORTQUEUE, json);
				}
			}
		} catch (Exception e) {
			log.error("ReceiveAuditStateServlet被动接收审批推送异常！", e);
		}
		receive.buildAuditResponse(request, response);
	}

	public ReceiveAudit getReceive() {
		return receive;
	}

	public void setReceive(ReceiveAudit receive) {
		this.receive = receive;
	}

	public FmsChannel getFmsChannel() {
		return fmsChannel;
	}

	public void setFmsChannel(FmsChannel fmsChannel) {
		this.fmsChannel = fmsChannel;
	}

}