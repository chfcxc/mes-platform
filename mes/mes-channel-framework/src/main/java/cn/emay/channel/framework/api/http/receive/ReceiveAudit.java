package cn.emay.channel.framework.api.http.receive;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.emay.eucp.common.dto.fms.template.FmsTemplateChannelResponseDto;

public interface ReceiveAudit {

	List<FmsTemplateChannelResponseDto> receiveAudit(HttpServletRequest request, HttpServletResponse response);

	void buildAuditResponse(HttpServletRequest request, HttpServletResponse response);
}
