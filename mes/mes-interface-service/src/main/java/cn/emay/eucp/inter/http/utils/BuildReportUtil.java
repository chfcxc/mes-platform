package cn.emay.eucp.inter.http.utils;

import java.util.List;

import cn.emay.eucp.common.dto.report.FmsReportDTO;
import cn.emay.eucp.inter.http.dto.response.ReportResponse;

public class BuildReportUtil {

	public static void buildReport(List<FmsReportDTO> receives, List<ReportResponse> reports) {
		if (null != receives) {
			for (FmsReportDTO dto : receives) {
				ReportResponse reportDto = new ReportResponse();
				reportDto.setCustomFmsId(dto.getCustomFmsId());
				reportDto.setDesc(dto.getDesc());
				reportDto.setMobile(dto.getMobile());
				reportDto.setFmsId(dto.getFmsId());
				reportDto.setState(dto.getState());
				reportDto.setReceiveTime(dto.getReceiveTime().substring(0, 19));
				reportDto.setSubmitTime(dto.getSubmitTime().substring(0, 19));
				reports.add(reportDto);
			}
		}
	}


}
