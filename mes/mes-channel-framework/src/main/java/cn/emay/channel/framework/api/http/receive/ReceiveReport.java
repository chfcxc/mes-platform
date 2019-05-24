package cn.emay.channel.framework.api.http.receive;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.emay.channel.framework.dto.FmsChannelReport;

public interface ReceiveReport {

	List<FmsChannelReport> receiveReports(HttpServletRequest request, HttpServletResponse response);

	void buildResponse(HttpServletRequest request, HttpServletResponse response);

}
